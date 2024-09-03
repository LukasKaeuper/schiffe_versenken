
public class Spieler {
    private String name;            // Name des Spielers
    private int anzahlZuege = 0;    // Anzahl der Züge vom Spieler

    /**
     * Konstruktor für die Klasse Spieler.
     *
     * @param name Der Name des Spielers.
     */
    public Spieler(String name){

        this.name = name;
    }

    /**
     * Gibt den Namen des Spielers zurück.
     *
     * @return Der Name des Spielers.
     */
    public String getName(){
        return name;
    }

    /**
     * Setzt den Namen des Spielers.
     *
     * @param name Der neue Name des Spielers.
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gibt die Anzahl der Züge des Spielers zurück.
     *
     * @return Die Anzahl der Züge des Spielers.
     */
    public int getAnzahlZuege(){
        return anzahlZuege;
    }

    /**
     * Erhöht die Anzahl der Züge des Spielers um eins.
     */
    public void zugErhoehen(){
        this.anzahlZuege++;
    }

}
