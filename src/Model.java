import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Model {
    private Spielfeld spielfeldLinks = new Spielfeld();
    private Spielfeld spielfeldRechts = new Spielfeld();
    int spieler = 1;

    public Model() {
        playSound("/Sound/ambience.wav");
    }

    public void schiessen(int n, int m) {
        //markiert das Stück vom Schiff was getroffen wurde
        spielfeldRechts.trefferMarkieren(n, m);
        if (spielfeldRechts.getWert(n, m).equals("Schiff_getroffen") || spielfeldRechts.getWert(n, m).equals("Komplettes_Schiff_getroffen")) {
            playSound("/Sound/explosion.wav");
        }
        else if (spielfeldRechts.getWert(n, m).equals("Wasser_getroffen")) {
            playSound("/Sound/wasser.wav");
        }
        System.out.println("Schuss");
        spielfeldLinks.anzeigen();
        spielfeldRechts.anzeigen();
        spielfeldLinks.getSpieler().zugErhoehen();
        //System.out.println();
    }

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
                    File file = new File(path + url);
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
                    clip.open(inputStream);
                    clip.start();
                    if (file.getPath().equals(path+ "/Sound/ambience.wav")) {
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

    public void spielerWechseln() {
        Spielfeld temp = spielfeldLinks;
        spielfeldLinks = spielfeldRechts;
        spielfeldRechts = temp;
        spieler++;
        spieler %= 2;
        System.out.println("Seitenwechsel");
        spielfeldLinks.anzeigen();
        spielfeldRechts.anzeigen();
        //System.out.println();
    }

    public int getZuege() {
        return spielfeldLinks.getSpieler().getAnzahlZuege();
    }
}
