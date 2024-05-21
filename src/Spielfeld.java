import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spielfeld {
    private int reihe = 10;
    private int spalte = 10;
    private int leer = 0;
    private int schiff = 1;
    private int leer_getroffen = 2;
    private int schiff_getroffen = 3;

    private int[][] spielfeld;
    private List<Schiff> schiffe;


    public Spielfeld() {
        spielfeld = new int[reihe][spalte];
        schiffe = new ArrayList<>();
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
        int x;
        int y;

        if(horizontal){
            y = random.nextInt(reihe);
            x = random.nextInt(spalte - laenge + 1);
        } else {
            y = random.nextInt(reihe - laenge + 1);
            x = random.nextInt(spalte);
        }
        if(checkPlazierung(x,y,laenge, horizontal) && checkAbstand(x,y,laenge, horizontal)){
            Schiff neuesSchiff = new Schiff(horizontal,laenge,x,y);
            schiffe.add(neuesSchiff);

            if(horizontal){
                for(int i = 0; i < laenge; i++){
                    spielfeld[x][y + i] = schiff;
                }
            } else {
                for(int i = 0; i < laenge; i++){
                    spielfeld[x + i][y] = schiff;
                }
            }

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

    public void trefferMarkieren(int n, int m) {
        if (spielfeld[n][m] == leer) {
            spielfeld[n][m] = leer_getroffen;
        }
        if (spielfeld[n][m] == schiff) {
            spielfeld[n][m] = schiff_getroffen;
        }
    }
}