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

    private final int[][] spielfeld;
    private final ArrayList<Schiff> schiffe;


    public Spielfeld() {
        spielfeld = new int[reihe][spalte];
        schiffe = new ArrayList<Schiff>();
        initialisiereSpielfeld();
        schiffePlazieren(5);
        schiffePlazieren(4);
        schiffePlazieren(4);
        schiffePlazieren(3);
        schiffePlazieren(3);
        schiffePlazieren(3);
        schiffePlazieren(2);
        schiffePlazieren(2);
        schiffePlazieren(2);
        schiffePlazieren(2);
        anzeigen();
    }

    public void initialisiereSpielfeld() {
        for (int i = 0; i < reihe; i++) {
            for (int j = 0; j < spalte; j++) {
                spielfeld[i][j] = leer;
            }
        }
    }

    public void schiffePlazieren(int laenge){
        Random random = new Random();
        boolean horizontal = random.nextBoolean();
        int x = random.nextInt(spalte);;
        int y = random.nextInt(reihe);;

        if(checkPlazierung(x,y,laenge, horizontal) && checkAbstand(x,y,laenge, horizontal)){
            Schiff neuesSchiff = new Schiff(horizontal,laenge,x,y);

            if(horizontal){
                for(int i = 0; i < laenge; i++){
                    spielfeld[x][y + i] = schiff;
                    //System.out.println("neue Koordinate");
                    neuesSchiff.neueKoordinate(x, y+i);
                }
            } else {
                for(int i = 0; i < laenge; i++){
                    spielfeld[x + i][y] = schiff;
                    //System.out.println("neue Koordinate");
                    neuesSchiff.neueKoordinate(x+i, y);
                }
            }
            schiffe.add(neuesSchiff);
            neuesSchiff.ausgabe();

        } else {
            schiffePlazieren(laenge);
        }
    }

    private boolean checkPlazierung(int x, int y, int laenge, boolean horizontal){
        if(horizontal){
            for (int i = 0; i < laenge; i++){
                if(y + i >= spalte || spielfeld[x][y + i] != leer) {
                    return false;
                }
            }
        } else {
            for(int i = 0; i < laenge; i++){
                if(x + i >= reihe || spielfeld[x + i][y] != leer){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkAbstand(int x, int y, int laenge, boolean horizontal) {
        int startReihe = Math.max(0, x - 1);
        int endReihe = Math.min(reihe - 1, horizontal ? x + 1 : x + laenge);
        int startSpalte = Math.max(0, y - 1);
        int endSpalte = Math.min(spalte - 1, horizontal ? y + laenge : y + 1);

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
}