import javax.sound.sampled.*;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Das Model überprüft den Spielstatus, verwaltet sowohl die Spielfelder als auch die Spieler, und beinhaltet die
 * Hintergrundlogik des schießens, der KI, und dem Sound.
 * @author Lukas Käuper, Marten Ahmann
 * @version 05.09.2024
 */
public class Model {
    private Spielfeld spielfeldLinks;                   // Das Spielfeld des aktuellen Spielers
    private Spielfeld spielfeldRechts;                  // Das Spielfeld des Gegners
    private int spieler = 1;                            // Gibt an, welcher Spieler gerade an der Reihe ist (1 oder 2)
    private int kiSchussX;                              // Die X-Position des nächsten KI-Schusses
    private int kiSchussY;                              // Die Y-Position des nächsten KI-Schusses
    private boolean neuesSchiffSuchen = true;           // Flag, ob nach einem neuen Schiff gesucht wird
    private String suchRichtung = "unbekannt";          // Die Richtung, in die die KI sucht
    private Controller controller;                      // Der Controller, der das Model steuert

    /**
     * Konstruktor, der das Model initialisiert, den Controller zuweist und das Hintergrundrauschen startet.
     *
     * @param controller Der Controller, der das Model steuert.
     */
    public Model(Controller controller) {
        playSound("ambience.wav", true, -10f);
        this.controller = controller;
    }

    /**
     * Initialisiert die Spielfelder für beide Spieler und sucht erste Koordinaten für den KI-Schuss.
     */
    public void spielfeldInitialisieren(){
        this.kiSchussX = ThreadLocalRandom.current().nextInt(0, 10);
        this.kiSchussY = ThreadLocalRandom.current().nextInt(0, 10);
        neuesSchiffSuchen = true;
        spielfeldLinks = new Spielfeld();
        spielfeldRechts = new Spielfeld();
    }

    /**
     * Führt einen Schuss auf das gegnerische Spielfeld aus und spielt den entsprechenden Sound.
     * Benachrichtigt den Controller über die Länge des abgeschossenen Schiffes.
     *
     * @param n Die X-Koordinate des Schusses.
     * @param m Die Y-Koordinate des Schusses.
     */
    public void schiessen(int n, int m) {
        //markiert das Stück vom Schiff was getroffen wurde
        int laengeVomAbgeschossenenSchiff = spielfeldRechts.trefferMarkieren(n, m);
        if (spielfeldRechts.getWert(n, m).equals("Schiff_getroffen")) {
            playSound("kleine_explosion.wav", false, 3f);
        }
        else if (spielfeldRechts.getWert(n, m).equals("Komplettes_Schiff_getroffen")) {
            playSound("große_explosion.wav", false, -6f);
        }
        else if (spielfeldRechts.getWert(n, m).equals("Wasser_getroffen")) {
            playSound("wasser.wav", false, -6f);
        }
        if (laengeVomAbgeschossenenSchiff != 0) {
            controller.schiffanzeigeBenachrichtigen(laengeVomAbgeschossenenSchiff, "Gegner");
        }
        System.out.println("Schuss");
        spielfeldLinks.anzeigen();
        spielfeldRechts.anzeigen();
        spielfeldLinks.getSpieler().zugErhoehen();
    }

    /**
     * Lässt die KI einen Schuss abgeben und verfolgt den Fortschritt der Suche nach einem Schiff.
     * Benachrichtigt den Controller, falls ein Schiff komplett abgeschossen wurde.
     */
    public void ki_schiessen() {
        int laengeVomAbgeschossenenSchiff = 0;
        if (neuesSchiffSuchen) {
            while (!spielfeldLinks.getWert(kiSchussX, kiSchussY).equals("Schiff") && !spielfeldLinks.getWert(kiSchussX, kiSchussY).equals("Wasser") || spielfeldLinks.getWert(kiSchussX, kiSchussY).equals("unmoeglich") || spielfeldLinks.getWert(kiSchussX, kiSchussY).equals("unmoeglich_getroffen")) {
                if (spielfeldLinks.schiffUebrich()){
                    kiSchussX = ThreadLocalRandom.current().nextInt(0, 10);
                    kiSchussY = ThreadLocalRandom.current().nextInt(0, 10);
                }
                else return;
            }
        }
        laengeVomAbgeschossenenSchiff = spielfeldLinks.trefferMarkieren(kiSchussX, kiSchussY);
        System.out.println("KI Schuss bei: (" + kiSchussX + ", " + kiSchussY + ")");

        if (spielfeldLinks.getWert(kiSchussX, kiSchussY).equals("Schiff_getroffen")) {
            neuesSchiffSuchen = false;
            int ursprungsTrefferX = kiSchussX;
            int ursprungstrefferY = kiSchussY;
            int letzterTrefferX = kiSchussX;
            int letzterTrefferY = kiSchussY;

            while (!spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY).equals("Wasser_getroffen")) {
                if (letzterTrefferX+1 <= 9 && !spielfeldLinks.getWert(letzterTrefferX+1, letzterTrefferY).equals("Wasser_getroffen") && !spielfeldLinks.getWert(letzterTrefferX+1, letzterTrefferY).equals("unmoeglich") && !spielfeldLinks.getWert(letzterTrefferX+1, letzterTrefferY).equals("unmoeglich_getroffen") && (suchRichtung.equals("süden") || suchRichtung.equals("unbekannt"))) {
                    laengeVomAbgeschossenenSchiff = spielfeldLinks.trefferMarkieren(letzterTrefferX+1, letzterTrefferY);
                    System.out.println("KI Schuss bei: (" + Integer.toString(letzterTrefferX+1) + ", " + letzterTrefferY + ")");
                    if(spielfeldLinks.getWert(letzterTrefferX+1, letzterTrefferY).equals("Schiff_getroffen")) {
                        letzterTrefferX++;
                        suchRichtung = "süden";
                    } else {
                        if (spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY).equals("Komplettes_Schiff_getroffen")) {
                            neuesSchiffSuchen = true;
                            suchRichtung = "unbekannt";
                            controller.schiffanzeigeBenachrichtigen(laengeVomAbgeschossenenSchiff, "Eigen");
                            System.out.println("Suche nach neuem Schiff: " + neuesSchiffSuchen + "\n");
                            ki_schiessen();
                            return;
                        }
                        break;
                    }
                }
                else if (letzterTrefferY+1 <= 9 && !spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY+1).equals("Wasser_getroffen") && !spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY+1).equals("unmoeglich") && !spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY+1).equals("unmoeglich_getroffen") && (suchRichtung.equals("westen") || suchRichtung.equals("unbekannt"))) {
                    laengeVomAbgeschossenenSchiff = spielfeldLinks.trefferMarkieren(letzterTrefferX, letzterTrefferY+1);
                    System.out.println("KI Schuss bei: (" + letzterTrefferX + ", " + Integer.toString(letzterTrefferY+1) + ")");
                    if(spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY+1).equals("Schiff_getroffen")) {
                        letzterTrefferY++;
                        suchRichtung = "westen";
                    } else {
                        if (spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY).equals("Komplettes_Schiff_getroffen")) {
                            neuesSchiffSuchen = true;
                            suchRichtung = "unbekannt";
                            controller.schiffanzeigeBenachrichtigen(laengeVomAbgeschossenenSchiff, "Eigen");
                            System.out.println("Suche nach neuem Schiff: " + neuesSchiffSuchen + "\n");
                            ki_schiessen();
                            return;
                        }
                        break;
                    }
                }
                else if (letzterTrefferX-1 >= 0 && !spielfeldLinks.getWert(letzterTrefferX-1, letzterTrefferY).equals("Wasser_getroffen") && !spielfeldLinks.getWert(letzterTrefferX-1, letzterTrefferY).equals("unmoeglich") && !spielfeldLinks.getWert(letzterTrefferX-1, letzterTrefferY).equals("unmoeglich_getroffen") && (suchRichtung.equals("norden") || suchRichtung.equals("unbekannt"))) {
                    laengeVomAbgeschossenenSchiff = spielfeldLinks.trefferMarkieren(letzterTrefferX-1, letzterTrefferY);
                    System.out.println("KI Schuss bei: (" + Integer.toString(letzterTrefferX-1) + ", " + letzterTrefferY + ")");
                    if(spielfeldLinks.getWert(letzterTrefferX-1, letzterTrefferY).equals("Schiff_getroffen")) {
                        letzterTrefferX--;
                        suchRichtung = "norden";
                    } else {
                        if (spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY).equals("Komplettes_Schiff_getroffen")) {
                            neuesSchiffSuchen = true;
                            suchRichtung = "unbekannt";
                            controller.schiffanzeigeBenachrichtigen(laengeVomAbgeschossenenSchiff, "Eigen");
                            System.out.println("Suche nach neuem Schiff: " + neuesSchiffSuchen + "\n");
                            ki_schiessen();
                            return;
                        }
                        break;
                    }
                }
                else if (letzterTrefferY-1 >= 0 && !spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY-1).equals("Wasser_getroffen") && !spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY-1).equals("unmoeglich") && !spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY-1).equals("unmoeglich_getroffen") && (suchRichtung.equals("osten") || suchRichtung.equals("unbekannt"))) {
                    laengeVomAbgeschossenenSchiff = spielfeldLinks.trefferMarkieren(letzterTrefferX, letzterTrefferY-1);
                    System.out.println("KI Schuss bei: (" + letzterTrefferX + ", " + Integer.toString(letzterTrefferY-1) + ")");
                    if(spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY-1).equals("Schiff_getroffen")) {
                        letzterTrefferY--;
                        suchRichtung = "osten";
                    } else {
                        if (spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY).equals("Komplettes_Schiff_getroffen")) {
                            neuesSchiffSuchen = true;
                            suchRichtung = "unbekannt";
                            controller.schiffanzeigeBenachrichtigen(laengeVomAbgeschossenenSchiff, "Eigen");
                            System.out.println("Suche nach neuem Schiff: " + neuesSchiffSuchen + "\n");
                            ki_schiessen();
                            return;
                        }
                        break;
                    }
                } else {
                    letzterTrefferX = ursprungsTrefferX;
                    letzterTrefferY = ursprungstrefferY;
                    switch (suchRichtung) {
                        case "norden":
                            suchRichtung = "süden";
                            break;
                        case "süden":
                            suchRichtung = "norden";
                            break;
                        case "westen":
                            suchRichtung = "osten";
                            break;
                        case "osten":
                            suchRichtung = "westen";
                            break;
                    }
                }
            }
        }
        spielerWechseln("sp");
        System.out.println("Suche nach neuem Schiff: " + neuesSchiffSuchen + "\n");
    }

    /**
     * Startet einen neuen Thread und spielt den angegebenen Sound ab.
     *
     * @param url Der Name der Sounddatei.
     * @param loop Gibt an, ob der Sound wiederholt werden soll.
     * @param volume Die Lautstärke des Sounds.
     */
    public static synchronized void playSound(final String url, boolean loop, float volume) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
                    File file = new File(path + "/Sound/" + url);
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
                    clip.open(inputStream);
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(volume);
                    clip.start();
                    if (loop) {
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Überprüft, ob das Spiel beendet ist, und gibt den Sieger zurück.
     *
     * @return 0, wenn Spiel nicht beendet ist. 1, wenn auf dem linken Feld alle Schiffe getroffen wurden.
     * 2, wenn auf dem rechten Feld alle Schiffe getroffen wurden.
     */
    public int beendet() {
        int ende = 0;
        if (!spielfeldLinks.schiffUebrich()){
            ende = 1;
        }
        if (!spielfeldRechts.schiffUebrich()){
            ende = 2;
        }
        return ende;
    }

    /**
     * Gibt den Wert eines bestimmten Feldes auf dem Spielfeld des angegebenen Spielers zurück.
     *
     * @param i Die X-Koordinate des Feldes.
     * @param j Die Y-Koordinate des Feldes.
     * @param spieler Der Spieler, dessen Spielfeld überprüft werden soll.
     * @return Der Wert des Feldes als String.
     */
    public String getWert(int i, int j, String spieler) {
        if (spieler.equals("Eigen")) {
            return spielfeldLinks.getWert(i, j);
        } else {
            return spielfeldRechts.getWert(i, j);
        }
    }

    /**
     * Gibt die Nummer des aktuellen Spielers zurück.
     *
     * @return 1 für Spieler 1 oder 2 für Spieler 2.
     */
    public int getSpieler() {
        return switch (spieler) {
            case 1 -> 1;
            case 0 -> 2;
            default -> 0;
        };
    }

    /**
     * Wechselt den aktuellen Spieler und, falls man Mehrspieler spielt, die Seiten der Spielfelder.
     *
     * @param modus Gibt an, ob der Seitenwechsel durchgeführt werden soll oder nicht.
     */
    public void spielerWechseln(String modus) {
        if (!modus.equals("sp")) {
            Spielfeld temp = spielfeldLinks;
            spielfeldLinks = spielfeldRechts;
            spielfeldRechts = temp;
            System.out.println("Seitenwechsel");
            spielfeldLinks.anzeigen();
            spielfeldRechts.anzeigen();
        }
        spieler++;
        spieler %= 2;
    }

    /**
     * Gibt die Anzahl der Züge des aktuellen Spielers zurück.
     *
     * @return Die Anzahl der Züge des aktuellen Spielers.
     */
    public int getZuege() {
        return spielfeldLinks.getSpieler().getAnzahlZuege();
    }

    /**
     * Gibt die Anzahl der Züge des Computergegners zurück.
     * Zur Ermittlung werden getroffene Felder auf dem linken Spielfeld gezählt.
     *
     * @return Die Anzahl der Züge des Computergegners.
     */
    public int getZuegeKI(){
        int anzahlZuege = 0;
        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                if (spielfeldLinks.getWert(i, j).equals("Schiff_getroffen") || spielfeldLinks.getWert(i, j).equals("Wasser_getroffen") || spielfeldLinks.getWert(i, j).equals("unmoeglich_getroffen") || spielfeldLinks.getWert(i, j).equals("Komplettes_Schiff_getroffen")) {
                    anzahlZuege++;
                }
            }
        }
        return anzahlZuege;
    }

    /**
     * Setzt den Namen des ersten Spielers.
     *
     * @param name Der Name des ersten Spielers.
     */
    public void setSpielerNameEins(String name) {
        spielfeldLinks.getSpieler().setName(name);
    }

    /**
     * Gibt den Namen des ersten Spielers zurück.
     *
     * @return Der Name des ersten Spielers.
     */
    public String getSpielerNameEins() {
        return spielfeldLinks.getSpieler().getName();
    }

    /**
     * Setzt den Namen des zweiten Spielers.
     *
     * @param name Der Name des zweiten Spielers.
     */
    public void setSpielerNameZwei(String name) {
        spielfeldRechts.getSpieler().setName(name);
    }

    /**
     * Gibt den Namen des zweiten Spielers zurück.
     *
     * @return Der Name des zweiten Spielers.
     */
    public String getSpielerNameZwei() {
        return spielfeldRechts.getSpieler().getName();
    }
}
