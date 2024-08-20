import java.util.Date;

public class Spieler {
    private String name;
    private int anzahlZuege = 0;

    public Spieler(String name){

        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getAnzahlZuege(){
        return anzahlZuege;
    }

    public void zugErhoehen(){
        this.anzahlZuege++;
    }

}
