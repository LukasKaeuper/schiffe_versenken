import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Spielfeld {
    private final int reihe = 10;
    private final int spalte = 10;
    private final int leer = 0;
    private final int schiff = 1;
    private final int leer_getroffen = 2;
    private final int schiff_getroffen = 3;
    private final int komplettes_schiff_getroffen = 4;
    private Spieler spieler;

    private final int[][] spielfeld;
    private final ArrayList<Schiff> schiffe;


    public Spielfeld() {
        spielfeld = new int[reihe][spalte];
        schiffe = new ArrayList<Schiff>();
        spieler = new Spieler("platzhalter");
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

    public void initialisiereSpielfeld() {
        for (int i = 0; i < reihe; i++) {
            for (int j = 0; j < spalte; j++) {
                spielfeld[i][j] = leer;
            }
        }
    }

    public void schiffePlatzieren(int laenge){
        Random random = new Random();
        boolean platziert = false;

        while(!platziert){
            boolean horizontal = random.nextBoolean();
            int x = random.nextInt(spalte);
            int y = random.nextInt(reihe);

            if(checkPlatzierung(x,y,laenge, horizontal) && checkAbstand(x,y,laenge, horizontal)){
                Schiff neuesSchiff = new Schiff(horizontal,laenge,x,y);

                if(horizontal){
                    for(int i = 0; i < laenge; i++){
                        spielfeld[y][x + i] = schiff;
                        //System.out.println("neue Koordinate");
                        neuesSchiff.neueKoordinate(y, x+i);
                    }
                } else {
                    for(int i = 0; i < laenge; i++){
                        spielfeld[y + i][x] = schiff;
                        //System.out.println("neue Koordinate");
                        neuesSchiff.neueKoordinate(y+i, x);
                    }
                }
                schiffe.add(neuesSchiff);
                neuesSchiff.ausgabe();
                platziert = true;
            }
        }
    }

    private boolean checkPlatzierung(int x, int y, int laenge, boolean horizontal){
        if(horizontal){
            if (x + laenge > spalte - 1){
                for (int i = 0; i < laenge; i++){
                    if(x + i >= spalte || spielfeld[y][x + i] != leer) {
                        return false;
                    }
                }
                return false;
            }
        } else {
            if (y + laenge > reihe - 1){
                for(int i = 0; i < laenge; i++){
                    if(y + i >= reihe || spielfeld[y + i][x] != leer){
                        return false;
                    }
                }
                return false;
            }
        }
        return true;
    }

    private boolean checkAbstand(int x, int y, int laenge, boolean horizontal) {
        int startReihe = Math.max(0, y - 1);
        int endReihe = horizontal ? Math.min(reihe - 1, y + 1) : Math.min(reihe - 1, y + laenge);
        int startSpalte = Math.max(0, x - 1);
        int endSpalte = horizontal ? Math.min(spalte - 1, x + laenge) : Math.min(spalte - 1, x + 1);

        for (int i = startReihe; i <= endReihe; i++) {
            for (int j = startSpalte; j <= endSpalte; j++) {
                if (spielfeld[i][j] != leer) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getWert(int x, int y) {
        if (spielfeld[x][y] == schiff) {
            return "Schiff";
        }
        else if (spielfeld[x][y] == leer) {
            return "Wasser";
        }
        else if (spielfeld[x][y] == leer_getroffen) {
            return "Wasser_getroffen";
        }
        else if (spielfeld[x][y] == schiff_getroffen) {
            return "Schiff_getroffen";
        }
        else if (spielfeld[x][y] == komplettes_schiff_getroffen) {
            return "Komplettes_Schiff_getroffen";
        }
        else {
            return null;
        }
    }

    public void anzeigen(){
        //System.out.println();
        for(int i = 0; i < reihe; i++){
            for(int j = 0; j < spalte; j++){
                System.out.print(spielfeld[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void trefferMarkieren(int x, int y) {
        if (spielfeld[x][y] == leer) {
            spielfeld[x][y] = leer_getroffen;
        }
        if (spielfeld[x][y] == schiff) {
            spielfeld[x][y] = schiff_getroffen;
        }
        ganzesSchiffGetroffen(x, y);
    }

    public void ganzesSchiffGetroffen(int x, int y) {
        AtomicBoolean alleGetroffen = new AtomicBoolean(true);
        for (Schiff n : schiffe) {
            n.getKoordinaten().forEach((m) -> {
                if (m.x.equals(x) && m.y.equals(y)) {
                    //System.out.println("Schiff der Länge " + n.getLaenge() + " getroffen!");
                    n.getKoordinaten().forEach((k) -> {
                        if (!(spielfeld[k.getX()][k.getY()] == schiff_getroffen)) {
                            alleGetroffen.set(false);
                        }
                    });
                    if (alleGetroffen.get()) {
                        n.getKoordinaten().forEach((l) -> {
                            spielfeld[l.getX()][l.getY()] = komplettes_schiff_getroffen;
                        });
                        n.setAbgeschossen();
                    }
                }
            });
        }
    }

    public Spieler getSpieler() {
        return spieler;
    }
}