public class Spielfeld {
    private int groesse;
    private int[][] spielfeld = new int[groesse][groesse];

    public Spielfeld(int x) {
        this.groesse = x;
    }

    public void erstelleSpielfeld() {
        for (int i = 0; i < groesse; i++) {
            for (int j = 0; j < groesse; j++) {
                spielfeld[i][j] = 0;
            }
        }
    }


    public void setGroesse(int x) {
        this.groesse = x;
    }

    public int getGroesse() {
        return groesse;
    }

    public void anzeigen() {
        System.out.println(groesse);
    }
}