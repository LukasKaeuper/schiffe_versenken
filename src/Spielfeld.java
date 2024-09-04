import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Die Klasse Spielfeld repräsentiert ein 10x10 Spielfeld
 * Sie verwaltet die Platzierung der Schiffe, die Markierung von Treffern
 */
public class Spielfeld {
    private final int reihe = 10;                         // Anzahl der Reihen des Spielfelds
    private final int spalte = 10;                        // Anzahl der Spalten des Spielfelds
    private final int wasser = 0;                         // Wert für Wasserfelder
    private final int schiff = 1;                         // Wert für Schiffe
    private final int wasser_getroffen = 2;               // Wert für Wasser, das getroffen wurde
    private final int schiff_getroffen = 3;               // Wert für ein Schiff, das getroffen wurde
    private final int komplettes_schiff_getroffen = 4;    // Wert für ein komplett getroffenes Schiff
    private final int unmoeglich = 5;                     // Wert für Felder, auf denen keine Schiffe platziert werden können
    private final int unmoeglich_getroffen = 6;           // Wert für "unmögliche" Felder, die getroffen wurden
    private Spieler spieler;                              // Spieler, dem das Spielfeld gehört

    private final int[][] spielfeld;                      // Das Spielfeld als 2D-Array
    private final ArrayList<Schiff> schiffe;              // Liste der Schiffe auf dem Spielfeld


    /**
     * Konstruktor, der das Spielfeld initialisiert und die Schiffe platziert.
     */
    public Spielfeld() {
        spielfeld = new int[reihe][spalte];
        schiffe = new ArrayList<Schiff>();
        spieler = new Spieler("Spieler");
        initialisiereSpielfeld();
        schiffePlatzieren(5);
        schiffePlatzieren(4);
        schiffePlatzieren(4);
        schiffePlatzieren(3);
        schiffePlatzieren(3);
        schiffePlatzieren(3);
        schiffePlatzieren(2);
        schiffePlatzieren(2);
        schiffePlatzieren(2);
        schiffePlatzieren(2);
        anzeigen();
    }

    /**
     * Initialisiert das Spielfeld, indem alle Felder auf Wasser 0 gesetzt werden.
     */
    public void initialisiereSpielfeld() {
        for (int i = 0; i < reihe; i++) {
            for (int j = 0; j < spalte; j++) {
                spielfeld[i][j] = wasser;
            }
        }
    }

    /**
     * Platziert ein Schiff einer bestimmten Länge zufällig auf dem Spielfeld.
     * @param laenge Die Länge des zu platzierenden Schiffs.
     */
    public void schiffePlatzieren(int laenge){
        Random random = new Random();
        boolean platziert = false;
        LocalDateTime start = LocalDateTime.now();

        while (!platziert) {

            boolean horizontal = random.nextBoolean();
            int x = random.nextInt(spalte);
            int y = random.nextInt(reihe);

            if (checkPlatzierung(x, y, laenge, horizontal) && checkAbstand(x, y, laenge, horizontal)) {
                Schiff neuesSchiff = new Schiff(horizontal, laenge);

                if (horizontal) {
                    for (int i = 0; i < laenge; i++) {
                        spielfeld[y][x + i] = schiff;
                        neuesSchiff.neueKoordinate(y, x + i);
                    }
                } else {
                    for (int i = 0; i < laenge; i++) {
                        spielfeld[y + i][x] = schiff;
                        neuesSchiff.neueKoordinate(y + i, x);
                    }
                }
                schiffe.add(neuesSchiff);
                neuesSchiff.ausgabe();
                platziert = true;
            }

            // Timeout nach 2 Sekunden, Neustart der Platzierung
            if (ChronoUnit.SECONDS.between(start, LocalDateTime.now()) >= 2){
                System.out.println("Timeout bei Initialisierung, Neustart\n");
                schiffe.clear();
                initialisiereSpielfeld();
                schiffePlatzieren(5);
                schiffePlatzieren(4);
                schiffePlatzieren(4);
                schiffePlatzieren(3);
                schiffePlatzieren(3);
                schiffePlatzieren(3);
                schiffePlatzieren(2);
                schiffePlatzieren(2);
                schiffePlatzieren(2);
                schiffePlatzieren(2);
                anzeigen();
                return;
            }
        }
    }

    /**
     * Überprüft, ob ein Schiff an einer bestimmten Position platziert werden kann.
     * @param x Die x-Koordinate der Startposition.
     * @param y Die y-Koordinate der Startposition.
     * @param laenge Die Länge des Schiffs.
     * @param horizontal Ob das Schiff horizontal true oder vertikal false platziert werden soll.
     * @return true, wenn das Schiff platziert werden kann, sonst false.
     */
    private boolean checkPlatzierung(int x, int y, int laenge, boolean horizontal){
        if(horizontal){
            if (x + laenge > spalte){
                for (int i = 0; i < laenge; i++){
                    if(x + i >= spalte || spielfeld[y][x + i] != wasser) {
                        return false;
                    }
                }
                return false;
            }
        } else {
            if (y + laenge > reihe){
                for(int i = 0; i < laenge; i++){
                    if(y + i >= reihe || spielfeld[y + i][x] != wasser){
                        return false;
                    }
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Überprüft, ob um die gewählte Platzierung eines Schiffs genügend Abstand zu anderen Schiffen eingehalten wird.
     * @param x Die x-Koordinate der Startposition.
     * @param y Die y-Koordinate der Startposition.
     * @param laenge Die Länge des Schiffs.
     * @param horizontal Ob das Schiff horizontal (true) oder vertikal (false) platziert werden soll.
     * @return true, wenn der Abstand eingehalten wird, sonst false.
     */
    private boolean checkAbstand(int x, int y, int laenge, boolean horizontal) {
        int startReihe = Math.max(0, y - 1);
        int endReihe = horizontal ? Math.min(reihe - 1, y + 1) : Math.min(reihe - 1, y + laenge);
        int startSpalte = Math.max(0, x - 1);
        int endSpalte = horizontal ? Math.min(spalte - 1, x + laenge) : Math.min(spalte - 1, x + 1);

        for (int i = startReihe; i <= endReihe; i++) {
            for (int j = startSpalte; j <= endSpalte; j++) {
                if (spielfeld[i][j] != wasser) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gibt den aktuellen Wert an einer bestimmten Position auf dem Spielfeld zurück.
     * @param x Die x-Koordinate der Position.
     * @param y Die y-Koordinate der Position.
     * @return Eine Zeichenkette, die den Zustand der Position beschreibt (z.B. "Schiff", "Wasser").
     */
    public String getWert(int x, int y) {
        if (spielfeld[x][y] == schiff) {
            return "Schiff";
        }
        else if (spielfeld[x][y] == wasser) {
            return "Wasser";
        }
        else if (spielfeld[x][y] == wasser_getroffen) {
            return "Wasser_getroffen";
        }
        else if (spielfeld[x][y] == schiff_getroffen) {
            return "Schiff_getroffen";
        }
        else if (spielfeld[x][y] == komplettes_schiff_getroffen) {
            return "Komplettes_Schiff_getroffen";
        }
        else if (spielfeld[x][y] == unmoeglich) {
            return "unmoeglich";
        }
        else if (spielfeld[x][y] == unmoeglich_getroffen) {
            return "unmoeglich_getroffen";
        }
        else {
            return null;
        }
    }

    /**
     * Zeigt das aktuelle Spielfeld in der Konsole an.
     */
    public void anzeigen(){
        for(int i = 0; i < reihe; i++){
            for(int j = 0; j < spalte; j++){
                System.out.print(spielfeld[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Markiert einen Treffer auf dem Spielfeld.
     * @param x Die x-Koordinate der getroffenen Position.
     * @param y Die y-Koordinate der getroffenen Position.
     * @return true, wenn ein Schiff getroffen wurde, sonst false.
     */
    public int trefferMarkieren(int x, int y) {
        if (spielfeld[x][y] == wasser) {
            spielfeld[x][y] = wasser_getroffen;
        }
        if (spielfeld[x][y] == schiff) {
            spielfeld[x][y] = schiff_getroffen;
        }
        return ganzesSchiffGetroffen(x, y);
    }

    /**
     * Prüft, ob ein Schiff komplett versenkt wurde und markiert es entsprechend.
     * @param x Die x-Koordinate einer getroffenen Position.
     * @param y Die y-Koordinate einer getroffenen Position.
     */
    public int ganzesSchiffGetroffen(int x, int y) {
        AtomicInteger laengeVomAbgeschossenenSchiff = new AtomicInteger(0);
        AtomicBoolean alleGetroffen = new AtomicBoolean(true);
        for (Schiff n : schiffe) {
            n.getKoordinaten().forEach((m) -> {
                if (m.x.equals(x) && m.y.equals(y)) {
                    n.getKoordinaten().forEach((k) -> {
                        if (!(spielfeld[k.getX()][k.getY()] == schiff_getroffen)) {
                            alleGetroffen.set(false);
                        }
                    });
                    if (alleGetroffen.get()) {
                        n.getKoordinaten().forEach((l) -> {
                            spielfeld[l.getX()][l.getY()] = komplettes_schiff_getroffen;
                        });
                        //n.setAbgeschossen();
                        laengeVomAbgeschossenenSchiff.set(n.getLaenge());
                    }
                }
            });
        }
        unmoeglicheFelderMarkieren(x, y);
        return laengeVomAbgeschossenenSchiff.get();
    }

    /**
     * Diese Methode markiert Felder rund um ein getroffenes oder versenktes Schiff als "unmöglich",
     * um anzuzeigen, dass an diesen Positionen keine Schiffe platziert werden können.
     *
     * @param x Die x-Koordinate des getroffenen Feldes.
     * @param y Die y-Koordinate des getroffenen Feldes.
     */
    public void unmoeglicheFelderMarkieren(int x, int y) {
        if (spielfeld[x][y] == schiff_getroffen || spielfeld[x][y] == komplettes_schiff_getroffen) {

            if (x-1 >= 0 && y-1 >= 0 && (spielfeld[x-1][y-1] == wasser_getroffen || spielfeld[x-1][y-1] == unmoeglich_getroffen)) {
                spielfeld[x-1][y-1] = unmoeglich_getroffen;
            }else if (x-1 >= 0 && y-1 >= 0 && spielfeld[x-1][y-1] != wasser_getroffen) {
                spielfeld[x - 1][y - 1] = unmoeglich;
            }

            if (x-1 >= 0 && y+1 <= 9 && (spielfeld[x-1][y+1] == wasser_getroffen || spielfeld[x-1][y+1] == unmoeglich_getroffen)) {
                spielfeld[x-1][y+1] = unmoeglich_getroffen;
            } else if (x-1 >= 0 && y+1 <= 9 && spielfeld[x-1][y+1] != wasser_getroffen){
                spielfeld[x-1][y+1] = unmoeglich;
            }

            if (x+1 <= 9 && y-1 >= 0 && (spielfeld[x+1][y-1] == wasser_getroffen || spielfeld[x+1][y-1] == unmoeglich_getroffen)) {
                spielfeld[x+1][y-1] = unmoeglich_getroffen;
            } else if (x+1 <= 9 && y-1 >= 0 && spielfeld[x+1][y-1] != wasser_getroffen) {
                spielfeld[x+1][y-1] = unmoeglich;
            }

            if (x+1 <= 9 && y+1 <= 9 && (spielfeld[x+1][y+1] == wasser_getroffen || spielfeld[x+1][y+1] == unmoeglich_getroffen)) {
                spielfeld[x+1][y+1] = unmoeglich_getroffen;
            }else if (x+1 <= 9 && y+1 <= 9 && spielfeld[x+1][y+1] != wasser_getroffen) {
                spielfeld[x+1][y+1] = unmoeglich;
            }
        }
        if (spielfeld[x][y] == komplettes_schiff_getroffen) {
            for (Schiff n : schiffe) {
                n.getKoordinaten().forEach((m) -> {
                    if (m.x.equals(x) && m.y.equals(y)) {
                        n.getKoordinaten().forEach((k) -> {
                            himmelsrichtungenAlsUnmoeglichMarkieren(k.getX(), k.getY());
                        });
                    }
                });
            }
        }
    }

    /**
     * Diese Methode markiert die Felder direkt in den Himmelsrichtungen (oben, unten, links, rechts)
     * um das gegebene Feld als "unmöglich".
     *
     * @param x Die x-Koordinate des Feldes.
     * @param y Die y-Koordinate des Feldes.
     */
    private void himmelsrichtungenAlsUnmoeglichMarkieren(int x, int y) {
        if (x-1 >= 0 && spielfeld[x-1][y] != schiff_getroffen && spielfeld[x-1][y] != komplettes_schiff_getroffen) {
            if (spielfeld[x-1][y] == wasser_getroffen || spielfeld[x-1][y] == unmoeglich_getroffen){
                spielfeld[x-1][y] = unmoeglich_getroffen;
            } else {
                spielfeld[x-1][y] = unmoeglich;
            }
        }
        if (y-1 >= 0 && spielfeld[x][y-1] != schiff_getroffen && spielfeld[x][y-1] != komplettes_schiff_getroffen) {
            if (spielfeld[x][y-1] == wasser_getroffen || spielfeld[x][y-1] == unmoeglich_getroffen){
                spielfeld[x][y-1] = unmoeglich_getroffen;
            } else {
                spielfeld[x][y-1] = unmoeglich;
            }
        }
        if (x+1 <= 9 && spielfeld[x+1][y] != schiff_getroffen && spielfeld[x+1][y] != komplettes_schiff_getroffen) {
            if (spielfeld[x+1][y] == wasser_getroffen || spielfeld[x+1][y] == unmoeglich_getroffen){
                spielfeld[x+1][y] = unmoeglich_getroffen;
            } else {
                spielfeld[x+1][y] = unmoeglich;
            }
        }
        if (y+1 <= 9 && spielfeld[x][y+1] != schiff_getroffen && spielfeld[x][y+1] != komplettes_schiff_getroffen) {
            if (spielfeld[x][y+1] == wasser_getroffen || spielfeld[x][y+1] == unmoeglich_getroffen){
                spielfeld[x][y+1] = unmoeglich_getroffen;
            } else {
                spielfeld[x][y+1] = unmoeglich;
            }
        }
    }

    /**
     * Prüft, ob im Spielfeld noch mindestens ein Platz mit einem Schiff belegt ist.
     *
     * @return true, wenn ein Schiff gefunden wird, sonst false.
     */
    public boolean schiffUebrich(){
        boolean schiffGefunden = false;
        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                if (spielfeld[i][j] == schiff) {
                    schiffGefunden = true;
                }
            }
        }
        return schiffGefunden;
    }

    /**
     * Gibt den Spieler des Spielfelds zurück.
     *
     * @return Der Spieler, dem das Spielfeld gehört.
     */
    public Spieler getSpieler() {
        return spieler;
    }
}