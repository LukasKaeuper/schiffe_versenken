import java.util.ArrayList;

/**
 * Die Schiff-Klasse repräsentiert ein Schiff im Spiel. Sie enthält Informationen über die Position, Ausrichtung,
 * Länge und Koordinaten des Schiffs. Sie bietet Methoden zum Hinzufügen von Koordinaten, zur Ausgabe der
 * Schiffsinformationen und zur Überprüfung, ob das Schiff bereits abgeschossen wurde.
 */
public class Schiff {
    private boolean horizontal;                                 // Gibt die Ausrichtung des Schiffs an (horizontal oder vertikal)
    private int laenge;                                         // Die Länge des Schiffs
    private ArrayList<Koordinatenpaar> koordinaten;             // Liste der Koordinaten, die das Schiff abdeckt

    /**
     * Konstruktor zum Erstellen eines neuen Schiffs.
     * @param horizontal Gibt an, ob das Schiff horizontal ausgerichtet ist.
     * @param laenge Die Länge des Schiffs.
     */
    public Schiff(boolean horizontal, int laenge){
        this.horizontal = horizontal;
        this.laenge = laenge;
        this.koordinaten = new ArrayList<>();
        System.out.println("neues Schiff \n");
    }

    /**
     * Fügt dem Schiff eine neue Koordinate hinzu.
     * @param x Die X-Koordinate der neuen Position.
     * @param y Die Y-Koordinate der neuen Position.
     */
    public void neueKoordinate(int x, int y) {
        koordinaten.add(new Koordinatenpaar(x, y));
    }

    /**
     * Gibt die Koordinaten des Schiffs aus, sowie die Ausrichtung.
     */
    public void ausgabe() {
        koordinaten.forEach((n) -> System.out.println(this.horizontal ? "horizontal " + n.ausgabe() : "vertikal " + n.ausgabe()));
        System.out.println();
    }

    /**
     * Gibt die Liste der Koordinaten des Schiffs zurück.
     * @return Eine ArrayList von Koordinatenpaaren, die die Positionen des Schiffs repräsentieren.
     */
    public ArrayList<Koordinatenpaar> getKoordinaten() {
        return koordinaten;
    }

    /**
     * Gibt die Länge des Schiffs zurück.
     * @return Die Länge des Schiffs.
     */
    public int getLaenge(){
        return laenge;
    }

    /**
     * Die Koordinatenpaar-Klasse repräsentiert ein Paar von Koordinaten (X und Y).
     * @param <X> Der Typ der X-Koordinate.
     * @param <Y> Der Typ der Y-Koordinate.
     */
    public static class Koordinatenpaar<X, Y> {
        public final X x;
        public final Y y;

        public Koordinatenpaar(X x, Y y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Gibt den Wert der X-Koordinate als Integer zurück.
         * @return Die X-Koordinate als Integer.
         */
        public int getX() {
            return (Integer) x;
        }

        /**
         * Gibt den Wert der Y-Koordinate als Integer zurück.
         * @return Die Y-Koordinate als Integer.
         */
        public int getY() {
            return (Integer) y;
        }

        /**
         * Gibt die Koordinaten als String im Format "(x, y)" aus.
         * @return Die Koordinaten als String.
         */
        public String ausgabe() {
            return "(" + x + ", " + y + ")";
        }
    }
}
