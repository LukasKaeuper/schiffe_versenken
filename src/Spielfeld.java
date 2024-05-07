public class Spielfeld {
private int groesse = 10;
private char[][] spielfeld = new char[groesse][groesse];

    public void erstelleSpielfeld(){
        for (int i = 0; i < groesse; i++){
            for (int j = 0; j < groesse; j++){
                spielfeld[i][j] = '-';
            }
        }
    }




    public void setGroesse(int x){
        this.groesse = x;
    }

    public int getGroesse(){
        return groesse;
    }

    public void anzeigen(){
        System.out.println(groesse);
    }

}
