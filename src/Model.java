public class Model {
    private Spielfeld spieler1 = new Spielfeld();
    private Spielfeld spieler2 = new Spielfeld();

    public Model() {

    }

    public void schießen(int n, int m) {
        //markiert das Stück vom Schiff was getroffen wurde
        spieler2.trefferMarkieren(n, m);
    }

    public boolean beendet() {
        //return True, wenn ein Endkriterium erreicht ist -> Alle Schiffe eines Spielers zerstört
        boolean ende = false;
        return ende;
    }

    public String getWert(int i, int j, String spieler) {
        if (spieler.equals("Eigen")) {
            return spieler1.getWert(i, j);
        } else {
            return spieler2.getWert(i, j);
        }
    }
}
