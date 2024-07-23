import java.util.ArrayList;

public class Schiff {
    private boolean horizontal;
    private int laenge;
    private int reihe;
    private int spalte;
    private ArrayList<Koordinatenpaar> koordinaten;
    private boolean abgeschossen;

    public Schiff(boolean horizontal, int laenge, int reihe, int spalte){
        this.horizontal = horizontal;
        this.laenge = laenge;
        this.reihe = reihe;
        this.spalte = spalte;
        this.abgeschossen = false;
        this.koordinaten = new ArrayList<>();
        System.out.println("neues Schiff \n");
    }

    public void neueKoordinate(int x, int y) {
        koordinaten.add(new Koordinatenpaar(x, y));
    }

    public void ausgabe() {
        koordinaten.forEach((n) -> System.out.println(this.horizontal ? "horizontal " + n.ausgabe() : "vertikal " + n.ausgabe()));
        System.out.println();
    }

    public ArrayList<Koordinatenpaar> getKoordinaten() {
        return koordinaten;
    }

    public void setAbgeschossen() {
        abgeschossen = true;
        //System.out.println("Schiff der Länge : " + this.laenge + " abgeschossen");
    }

    public int getLaenge(){
        return laenge;
    }

    public static class Koordinatenpaar<X, Y> {
        public final X x;
        public final Y y;

        public Koordinatenpaar(X x, Y y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return (Integer) x;
        }

        public int getY() {
            return (Integer) y;
        }

        public String ausgabe() {
            return "(" + x + ", " + y + ")";
        }
    }
}
