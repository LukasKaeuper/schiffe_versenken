public class Schiff {
    private String typ;
    private int laenge;

    public Schiff(String typ, int laenge){
        this.typ = typ;
        this.laenge = laenge;
    }

    public void erstelleShiffe(){
        Schiff[] schiffe = {
                new Schiff("Fünfer", 5),
                new Schiff("Vierer1", 4),
                new Schiff("Vierer2", 4),
                new Schiff("Dreier1", 3),
                new Schiff("Dreier2", 3),
                new Schiff("Dreier3", 3),
                new Schiff("Zweier1", 2),
                new Schiff("Zweier2", 2),
                new Schiff("Zweier3", 2),
                new Schiff("Zweier4", 2)
        };
    }


    public String getTyp() {
        return typ;
    }

    public int getLaege() {
        return laenge;
    }


}
