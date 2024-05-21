public class Model {
    private Spielfeld spieler1 = new Spielfeld();
    private Spielfeld spieler2 = new Spielfeld();
    int spieler = 1;

    public Model() {

    }

    public void schiessen(int n, int m) {
        //markiert das Stück vom Schiff was getroffen wurde
        spieler2.trefferMarkieren(n, m);
        System.out.println("Schuss");
        spieler1.anzeigen();
        spieler2.anzeigen();
        //System.out.println();
    }

    public boolean beendet() {
        //return true, wenn ein Endkriterium erreicht ist → Kein Schiff übrig, dass nicht getroffen wurde
        boolean ende = true;
        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                if (spieler2.getWert(i, j).equals("Schiff")) {
                    ende = false;
                }
            }
        }
        return ende;
    }

    public String getWert(int i, int j, String spieler) {
        if (spieler.equals("Eigen")) {
            return spieler1.getWert(i, j);
        } else {
            return spieler2.getWert(i, j);
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
        Spielfeld temp = spieler1;
        spieler1 = spieler2;
        spieler2 = temp;
        spieler++;
        spieler %= 2;
        System.out.println("Seitenwechsel");
        spieler1.anzeigen();
        spieler2.anzeigen();
        //System.out.println();
    }
}
