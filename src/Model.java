import javax.sound.sampled.*;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

public class Model {
    private Spielfeld spielfeldLinks;
    private Spielfeld spielfeldRechts;
    private int spieler = 1;
    private int kiSchussX;
    private int kiSchussY;
    private boolean neuesSchiffSuchen = true;
    private String suchRichtung = "unbekannt";
    private Controller controller;

    public Model(Controller controller) {
        playSound("ambience.wav", true, -10f);
        this.kiSchussX = ThreadLocalRandom.current().nextInt(0, 10);
        this.kiSchussY = ThreadLocalRandom.current().nextInt(0, 10);
        this.controller = controller;
    }

    public void spielfeldInitialisieren(){
        spielfeldLinks = new Spielfeld();
        spielfeldRechts = new Spielfeld();
    }

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

    public void ki_schiessen() {
        int laengeVomAbgeschossenenSchiff = 0;
        if (neuesSchiffSuchen) {
            while (!spielfeldLinks.getWert(kiSchussX, kiSchussY).equals("Schiff") && !spielfeldLinks.getWert(kiSchussX, kiSchussY).equals("Wasser") || spielfeldLinks.getWert(kiSchussX, kiSchussY).equals("unmoeglich") || spielfeldLinks.getWert(kiSchussX, kiSchussY).equals("unmoeglich_getroffen")) {
                kiSchussX = ThreadLocalRandom.current().nextInt(0, 10);
                kiSchussY = ThreadLocalRandom.current().nextInt(0, 10);
            }
        }
        laengeVomAbgeschossenenSchiff = spielfeldLinks.trefferMarkieren(kiSchussX, kiSchussY);
        spielfeldRechts.getSpieler().zugErhoehen();
        System.out.println("KI Schuss bei: (" + kiSchussX + ", " + kiSchussY + ")");
        if (spielfeldLinks.getWert(kiSchussX, kiSchussY).equals("Schiff_getroffen")) {
            neuesSchiffSuchen = false;
            int ursprungsTrefferX = kiSchussX;
            int ursprungstrefferY = kiSchussY;
            int letzterTrefferX = kiSchussX;
            int letzterTrefferY = kiSchussY;

            while (!spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY).equals("Wasser_getroffen")) {
                if (letzterTrefferX+1 <= 9 && !spielfeldLinks.getWert(letzterTrefferX+1, letzterTrefferY).equals("Wasser_getroffen") && !spielfeldLinks.getWert(letzterTrefferX+1, letzterTrefferY).equals("unmoeglich") && (suchRichtung.equals("süden") || suchRichtung.equals("unbekannt"))) {
                    laengeVomAbgeschossenenSchiff = spielfeldLinks.trefferMarkieren(letzterTrefferX+1, letzterTrefferY);
                    spielfeldRechts.getSpieler().zugErhoehen();
                    System.out.println("KI Schuss bei: (" + Integer.toString(letzterTrefferX+1) + ", " + letzterTrefferY + ")");
                    if(spielfeldLinks.getWert(letzterTrefferX+1, letzterTrefferY).equals("Schiff_getroffen")) {
                        letzterTrefferX++;
                        suchRichtung = "süden";
                    } else {
                        if (spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY).equals("Komplettes_Schiff_getroffen")) {
                            neuesSchiffSuchen = true;
                            suchRichtung = "unbekannt";
                            //System.out.println("KI hat ganzes Schiff getroffen!");
                            controller.schiffanzeigeBenachrichtigen(laengeVomAbgeschossenenSchiff, "Eigen");
                            System.out.println("Suche nach neuem Schiff: " + neuesSchiffSuchen + "\n");
                            ki_schiessen();
                            return;
                        }
                        break;
                    }
                }
                else if (letzterTrefferY+1 <= 9 && !spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY+1).equals("Wasser_getroffen") && !spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY+1).equals("unmoeglich") && (suchRichtung.equals("westen") || suchRichtung.equals("unbekannt"))) {
                    laengeVomAbgeschossenenSchiff = spielfeldLinks.trefferMarkieren(letzterTrefferX, letzterTrefferY+1);
                    spielfeldRechts.getSpieler().zugErhoehen();
                    System.out.println("KI Schuss bei: (" + letzterTrefferX + ", " + Integer.toString(letzterTrefferY+1) + ")");
                    if(spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY+1).equals("Schiff_getroffen")) {
                        letzterTrefferY++;
                        suchRichtung = "westen";
                    } else {
                        if (spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY).equals("Komplettes_Schiff_getroffen")) {
                            neuesSchiffSuchen = true;
                            suchRichtung = "unbekannt";
                            //System.out.println("KI hat ganzes Schiff getroffen!");
                            controller.schiffanzeigeBenachrichtigen(laengeVomAbgeschossenenSchiff, "Eigen");
                            System.out.println("Suche nach neuem Schiff: " + neuesSchiffSuchen + "\n");
                            ki_schiessen();
                            return;
                        }
                        break;
                    }
                }
                else if (letzterTrefferX-1 >= 0 && !spielfeldLinks.getWert(letzterTrefferX-1, letzterTrefferY).equals("Wasser_getroffen") && !spielfeldLinks.getWert(letzterTrefferX-1, letzterTrefferY).equals("unmoeglich") && (suchRichtung.equals("norden") || suchRichtung.equals("unbekannt"))) {
                    laengeVomAbgeschossenenSchiff = spielfeldLinks.trefferMarkieren(letzterTrefferX-1, letzterTrefferY);
                    spielfeldRechts.getSpieler().zugErhoehen();
                    System.out.println("KI Schuss bei: (" + Integer.toString(letzterTrefferX-1) + ", " + letzterTrefferY + ")");
                    if(spielfeldLinks.getWert(letzterTrefferX-1, letzterTrefferY).equals("Schiff_getroffen")) {
                        letzterTrefferX--;
                        suchRichtung = "norden";
                    } else {
                        if (spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY).equals("Komplettes_Schiff_getroffen")) {
                            neuesSchiffSuchen = true;
                            suchRichtung = "unbekannt";
                            //System.out.println("KI hat ganzes Schiff getroffen!");
                            controller.schiffanzeigeBenachrichtigen(laengeVomAbgeschossenenSchiff, "Eigen");
                            System.out.println("Suche nach neuem Schiff: " + neuesSchiffSuchen + "\n");
                            ki_schiessen();
                            return;
                        }
                        break;
                    }
                }
                else if (letzterTrefferY-1 >= 0 && !spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY-1).equals("Wasser_getroffen") && !spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY-1).equals("unmoeglich") && (suchRichtung.equals("osten") || suchRichtung.equals("unbekannt"))) {
                    laengeVomAbgeschossenenSchiff = spielfeldLinks.trefferMarkieren(letzterTrefferX, letzterTrefferY-1);
                    spielfeldRechts.getSpieler().zugErhoehen();
                    System.out.println("KI Schuss bei: (" + letzterTrefferX + ", " + Integer.toString(letzterTrefferY-1) + ")");
                    if(spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY-1).equals("Schiff_getroffen")) {
                        letzterTrefferY--;
                        suchRichtung = "osten";
                    } else {
                        if (spielfeldLinks.getWert(letzterTrefferX, letzterTrefferY).equals("Komplettes_Schiff_getroffen")) {
                            neuesSchiffSuchen = true;
                            suchRichtung = "unbekannt";
                            //System.out.println("KI hat ganzes Schiff getroffen!");
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

    public boolean beendet() {
        //return true, wenn ein Endkriterium erreicht ist → Kein Schiff übrig, dass nicht getroffen wurde
        boolean ende = true;
        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                if (spielfeldRechts.getWert(i, j).equals("Schiff")) {
                    ende = false;
                }
            }
        }
        return ende;
    }

    public String getWert(int i, int j, String spieler) {
        if (spieler.equals("Eigen")) {
            return spielfeldLinks.getWert(i, j);
        } else {
            return spielfeldRechts.getWert(i, j);
        }
    }

    public int getSpieler() {
        return switch (spieler) {
            case 1 -> 1;
            case 0 -> 2;
            default -> 0;
        };
    }

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

    public int getZuege() {
        return spielfeldLinks.getSpieler().getAnzahlZuege();
    }

    public String getName() {
        return spielfeldLinks.getSpieler().getName();
    }

    public void setSpielerNameEins(String name) {
        spielfeldLinks.getSpieler().setName(name);
    }

    public String getSpielerNameEins() {
        return spielfeldLinks.getSpieler().getName();
    }

    public void setSpielerNameZwei(String name) {
        spielfeldRechts.getSpieler().setName(name);
    }

    public String getSpielerNameZwei() {
        return spielfeldRechts.getSpieler().getName();
    }
}
