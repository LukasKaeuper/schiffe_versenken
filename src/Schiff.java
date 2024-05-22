import java.util.ArrayList;

public class Schiff {
    private boolean horizontal;
    private int laenge;
    private int reihe;
    private int spalte;
    private ArrayList<Koordinatenpaar> koordinaten;

    public Schiff(boolean horizontal, int laenge, int reihe, int spalte){
        this.horizontal = horizontal;
        this.laenge = laenge;
        this.reihe = reihe;
        this.spalte = spalte;
        koordinaten = new ArrayList<>();
        System.out.println("neues Schiff \n");
//        if (horizontal) {
//            for (int i=0; i<laenge; i++) {
//                koordinaten.add(new Koordinatenpaar(reihe+i, spalte));
//                //System.out.println("horizontal" + "(" + reihe+i + ", " + spalte + ")");
//                System.out.println("horizontal" + koordinaten.getFirst().ausgabe());
//            }
//        } else {
//            for (int i=0; i<laenge; i++) {
//                koordinaten.add(new Koordinatenpaar(reihe, spalte+i));
//                //System.out.println("vertikal" + "(" + reihe + ", " + spalte+i + ")");
//                System.out.println("vertikal" + koordinaten.getFirst().ausgabe());
//            }
//        }
    }

    public void neueKoordinate(int x, int y) {
        koordinaten.add(new Koordinatenpaar(x, y));

    }

    public void ausgabe() {
        koordinaten.forEach((n) -> System.out.println(this.horizontal ? "horizontal" + n.ausgabe() : "vertikal" + n.ausgabe()));
        System.out.println();
    }

//    public Schiff getSchiff(int n, int m) {
//        return temp;
//    }

    private class Koordinatenpaar<X, Y> {
        public final X x;
        public final Y y;
        public Koordinatenpaar(X x, Y y) {
            this.x = x;
            this.y = y;
        }

        public String ausgabe() {
            return "(" + x + ", " + y + ")";
        }
    }
}
