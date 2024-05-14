import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spielfeld {
    private int reihe = 10;
    private int spalte = 10;
    private int leer = 0;
    private int schiff = 1;

    private int[][] spielfeld;
    private List<Schiff> schiffe;


    public Spielfeld() {
        spielfeld = new int[reihe][spalte];
        schiffe = new ArrayList<>();
        initialisiereSpielfeld();
        schiffePlazieren(5);
        schiffePlazieren(4);
        schiffePlazieren(3);
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
        boolean horizental = random.nextBoolean();
        int x;
        int y;

        if(horizental){
            x = random.nextInt(reihe);
            y = random.nextInt(spalte - laenge + 1);
        } else {
            x = random.nextInt(reihe - laenge + 1);
            y = random.nextInt(spalte);
        }
        if(checkPlazierung(x,y,laenge,horizental)){
            Schiff neuesSchiff = new Schiff(horizental,laenge,x,y);
            schiffe.add(neuesSchiff);

            if(horizental){
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
                if(y + i >= spalte || spielfeld[x][y + i] != leer){
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

    public void anzeigen(){
        System.out.println();
        for(int i = 0; i < reihe; i++){
            for(int j = 0; j < spalte; j++){
                System.out.print(spielfeld[i][j] + " ");
            }
            System.out.println();
        }
    }
}