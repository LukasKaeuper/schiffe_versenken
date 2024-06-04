public class Model {
    private Spielfeld spielfeldLinks = new Spielfeld();
    private Spielfeld spielfeldRechts = new Spielfeld();
    int spieler = 1;

    public Model() {

    }

    public void schiessen(int n, int m) {
        //markiert das Stück vom Schiff was getroffen wurde
        spielfeldRechts.trefferMarkieren(n, m);
        System.out.println("Schuss");
        spielfeldLinks.anzeigen();
        spielfeldRechts.anzeigen();
        spielfeldLinks.getSpieler().zugErhoehen();
        //System.out.println();
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
