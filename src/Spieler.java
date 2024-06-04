public class Spieler {
    private String name;
    private int anzahlZuege = 0;
    private int anzahlSchiffe = 10;
    ;

    public Spieler(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getAnzahlZuege(){
        return anzahlZuege;
    }

    public int getAnzahlSchiffe(){
        return anzahlSchiffe;
    }

    public void schiffVerloren(){
        anzahlSchiffe--;
    }

    public void zugErhoehen(){
        this.anzahlZuege++;
    }
}
