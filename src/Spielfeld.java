import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Spielfeld {
    private int reihe;
    private int spalte;
    private final int wasser = 0;
    private final int schiff = 1;
    private final int wasser_getroffen = 2;
    private final int schiff_getroffen = 3;
    private final int komplettes_schiff_getroffen = 4;
    private final int unmoeglich = 5;
    private final int unmoeglich_getroffen = 6;
    private Spieler spieler;
    private boolean regelAus;

    private int[][] spielfeld;
    private final ArrayList<Schiff> schiffe;


    public Spielfeld(int i, int j, boolean x) {
        reihe = i;
        spalte = j;
        regelAus = x;
        spielfeld = new int[reihe][spalte];
        schiffe = new ArrayList<Schiff>();
        spieler = new Spieler("Spieler");
        initialisiereSpielfeld();
        if (i == 10) {
            schiffePlatzieren(5,regelAus);
            schiffePlatzieren(4,regelAus);
            schiffePlatzieren(4,regelAus);
            schiffePlatzieren(3,regelAus);
            schiffePlatzieren(3,regelAus);
            schiffePlatzieren(3,regelAus);
            schiffePlatzieren(2,regelAus);
            schiffePlatzieren(2,regelAus);
            schiffePlatzieren(2,regelAus);
            schiffePlatzieren(2,regelAus);
        }
        else if (i == 20){
            schiffePlatzieren(10,regelAus);
            schiffePlatzieren(8,regelAus);
            schiffePlatzieren(8,regelAus);
            schiffePlatzieren(6,regelAus);
            schiffePlatzieren(6,regelAus);
            schiffePlatzieren(6,regelAus);
            schiffePlatzieren(4,regelAus);
            schiffePlatzieren(4,regelAus);
            schiffePlatzieren(4,regelAus);
            schiffePlatzieren(4,regelAus);
        }
        anzeigen();
    }

    public void initialisiereSpielfeld() {
        for (int i = 0; i < reihe; i++) {
            for (int j = 0; j < spalte; j++) {
                spielfeld[i][j] = wasser;
            }
        }
    }

    public void schiffePlatzieren(int laenge, boolean aus){
        Random random = new Random();
        boolean platziert = false;
        aus = getRegelAus();

        if(!aus){
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

        if(aus){
            while(!platziert){
                boolean horizontal = random.nextBoolean();
                int x = random.nextInt(spalte);
                int y = random.nextInt(reihe);

                if(checkPlatzierung(x,y,laenge, horizontal)){
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
    }

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

    public int trefferMarkieren(int x, int y) {
        if (spielfeld[x][y] == wasser) {
            spielfeld[x][y] = wasser_getroffen;
        }
        if (spielfeld[x][y] == schiff) {
            spielfeld[x][y] = schiff_getroffen;
        }
        return ganzesSchiffGetroffen(x, y);
    }

    public int ganzesSchiffGetroffen(int x, int y) {
        AtomicInteger laengeVomAbgeschossenenSchiff = new AtomicInteger(0);
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
                        laengeVomAbgeschossenenSchiff.set(n.getLaenge());
                    }
                }
            });
        }
        unmoeglicheFelderMarkieren(x, y);
        return laengeVomAbgeschossenenSchiff.get();
    }

    public void unmoeglicheFelderMarkieren(int x, int y) {

        if(!regelAus){
            if (spielfeld[x][y] == schiff_getroffen || spielfeld[x][y] == komplettes_schiff_getroffen) {

                if (x - 1 >= 0 && y - 1 >= 0 && (spielfeld[x - 1][y - 1] == wasser_getroffen || spielfeld[x - 1][y - 1] == unmoeglich_getroffen)) {
                    spielfeld[x - 1][y - 1] = unmoeglich_getroffen;
                } else if (x - 1 >= 0 && y - 1 >= 0 && spielfeld[x - 1][y - 1] != wasser_getroffen) {
                    spielfeld[x - 1][y - 1] = unmoeglich;
                }

                if (x - 1 >= 0 && y + 1 <= 9 && (spielfeld[x - 1][y + 1] == wasser_getroffen || spielfeld[x - 1][y + 1] == unmoeglich_getroffen)) {
                    spielfeld[x - 1][y + 1] = unmoeglich_getroffen;
                } else if (x - 1 >= 0 && y + 1 <= 9 && spielfeld[x - 1][y + 1] != wasser_getroffen) {
                    spielfeld[x - 1][y + 1] = unmoeglich;
                }

                if (x + 1 <= 9 && y - 1 >= 0 && (spielfeld[x + 1][y - 1] == wasser_getroffen || spielfeld[x + 1][y - 1] == unmoeglich_getroffen)) {
                    spielfeld[x + 1][y - 1] = unmoeglich_getroffen;
                } else if (x + 1 <= 9 && y - 1 >= 0 && spielfeld[x + 1][y - 1] != wasser_getroffen) {
                    spielfeld[x + 1][y - 1] = unmoeglich;
                }

                if (x + 1 <= 9 && y + 1 <= 9 && (spielfeld[x + 1][y + 1] == wasser_getroffen || spielfeld[x + 1][y + 1] == unmoeglich_getroffen)) {
                    spielfeld[x + 1][y + 1] = unmoeglich_getroffen;
                } else if (x + 1 <= 9 && y + 1 <= 9 && spielfeld[x + 1][y + 1] != wasser_getroffen) {
                    spielfeld[x + 1][y + 1] = unmoeglich;
                }
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

    private void himmelsrichtungenAlsUnmoeglichMarkieren(int x, int y) {
        if(!regelAus){
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
    }

    public Spieler getSpieler() {
        return spieler;
    }

    public boolean getRegelAus(){
        return regelAus;
    }

    public void setRegelAus(boolean regelAus) {
        this.regelAus = regelAus;
    }

    public int getSpalte() {
        return spalte;
    }

    public void setSpalte(int spalte) {
        this.spalte = spalte;
    }

    public int getReihe() {
        return reihe;
    }

    public void setReihe(int reihe) {
        this.reihe = reihe;
    }
}