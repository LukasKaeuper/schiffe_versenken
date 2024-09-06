import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Die Klasse Controller ist das Bindeglied zwischen der Benutzeroberfläche View und dem Model.
 * Sie verarbeitet Benutzereingaben und aktualisiert die Darstellung entsprechend den Änderungen im Model.
 *
 * @author Lukas Käuper, Marten Ahmann
 * @version 05.09.2024
 */
public class Controller {

    private View view;           // Referenz auf die View-Komponente, die die Benutzeroberfläche darstellt.
    private Model model;         // Referenz auf das Model, das die Spiel-Daten und Logik enthält.
    private String modus;        // Der aktuelle Spielmodus, z.B. "lokal_mp" für lokalen Multiplayer.

    /**
     * Konstruktor: Initialisiert den Controller mit einer neuen Instanz von Model und View.
     * Setzt den Spielmodus standardmäßig auf "lokal_mp" (lokaler Multiplayer).
     */
    public Controller() {
        this.model = new Model(this);
        this.view = new View(this);
        this.modus = "lokal_mp";
    }

    /**
     * Initialisiert die Spielfelder im Model und setzt die Spielfeld-Listener in der View.
     * Diese Methode sorgt dafür, dass das Spielfeld bereit ist und auf Benutzeraktionen reagieren kann.
     */
    public void spielfelderInitialisieren() {
        model.spielfeldInitialisieren();
        this.view.erstelleSpielfeldListener(new SpielfeldListener());
    }

    /**
     * Innere Klasse, die als ActionListener für das Spielfeld fungiert.
     * Reagiert auf Klicks auf die Spielfeld-Buttons und führt entsprechende Aktionen aus.
     */
    class SpielfeldListener implements ActionListener {

        /**
         * Konstruktor: Aktualisiert das Spielfeld
         */
        public SpielfeldListener() {
            feldAktualisieren("Eigen");
            feldAktualisieren("Eigen");
        }

        /**
         * Diese Methode wird aufgerufen, wenn ein Spielfeld-Button geklickt wird.
         * Sie verarbeitet den Klick, führt den Schuss aus und aktualisiert die Spielfelder.
         *
         * @param e Das ActionEvent, das den Klick auf den Button beschreibt.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Index aus View holen
            String dummy = e.getActionCommand();
            int n = Integer.parseInt(dummy.substring(0, 1));
            int m = Integer.parseInt(dummy.substring(dummy.length() - 1));

            // Überprüfen, ob auf Wasser oder ein Schiff geklickt wurde
            if (model.getWert(n, m, "Gegner").equals("Wasser") || model.getWert(n, m, "Gegner").equals("Schiff")) {
                System.out.println("Spieler: " + model.getSpielerNameEins());
                model.schiessen(n, m);
                view.zuegeAktualisieren(model.getSpieler(), model.getZuege());

                // Spielmodus-spezifische Logik für Rundenwechsel und KI-Zug
                if (model.getWert(n, m, "Gegner").equals("Wasser_getroffen") && !modus.equals("sp")) {
                    model.spielerWechseln("lokal_mp");
                    view.rundenwechselBestaetigen();
                    view.nameAktualisieren(model.getSpieler(), model.getSpielerNameEins());
                    view.zuegeAktualisieren(model.getSpieler(), model.getZuege());
                }
                if (model.getWert(n, m, "Gegner").equals("Wasser_getroffen") && modus.equals("sp")) {
                    model.spielerWechseln("sp");
                    model.ki_schiessen();
                    view.zuegeAktualisieren(model.getSpieler(), model.getZuege());
                }
            }
            // Überprüfen, ob das Spiel beendet ist
            switch(model.beendet()){
                case 1:
                    view.setGewonnen(model.getSpielerNameZwei());
                    view.listenerEntfernen();
                    view.bestenlisteEintragen(model.getSpielerNameZwei(),model.getZuegeKI());
                    System.out.println("Bestenliste hinzugefügt");
                    break;
                case 2:
                    view.setGewonnen(model.getSpielerNameEins());
                    view.listenerEntfernen();
                    view.bestenlisteEintragen(model.getSpielerNameEins(),model.getZuege());
                    System.out.println("Bestenliste hinzugefügt");
                    break;
            }
            feldAktualisieren("Eigen");
            feldAktualisieren("Gegner");
            feldAktualisieren("Gegner");
        }
    }

    /**
     * Aktualisiert die Darstellung des Spielfelds für den angegebenen Spieler.
     * Diese Methode holt die aktuellen Spielfeldwerte vom Model und setzt sie in der View.
     *
     * @param spieler Der Spieler, dessen Spielfeld aktualisiert werden soll.
     */
    public void feldAktualisieren(String spieler) {
        String temp;
        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                temp = model.getWert(i, j, spieler);
                view.setButton(i, j, temp, spieler);
            }
        }
    }

    /**
     * Benachrichtigt die View darüber, dass ein komplettes Schiff getroffen wurde.
     *
     * @param laenge Die Länge des getroffenen Schiffes.
     * @param spieler Der Spieler, dem das getroffene Schiff gehört.
     */
    public void schiffanzeigeBenachrichtigen(int laenge, String spieler){
        System.out.println("Komplettes Schiff der Länge " + laenge + " von Spieler: " + spieler + " getroffen!");
        view.schiffanzeigeAktualisieren(laenge, spieler);
    }

    /**
     * Setzt den aktuellen Spielmodus.
     *
     * @param modus Der zu setzende Spielmodus, z.B. "lokal_mp" für lokalen Multiplayer oder "sp" für Singleplayer.
     */
    public void setModus(String modus){
        this.modus = modus;
    }

    /**
     * Setzt den Namen des ersten Spielers.
     *
     * @param name Der Name des ersten Spielers.
     */
    public void setSpielerNameEins(String name) {
        model.setSpielerNameEins(name);
    }

    /**
     * Gibt den Namen des ersten Spielers zurück.
     *
     * @return Der Name des ersten Spielers.
     */
    public String getSpielerNameEins() {
        return model.getSpielerNameEins();
    }

    /**
     * Setzt den Namen des zweiten Spielers.
     *
     * @param name Der Name des zweiten Spielers.
     */
    public void setSpielerNameZwei(String name) {
        model.setSpielerNameZwei(name);
    }

    /**
     * Gibt den Namen des zweiten Spielers zurück.
     *
     * @return Der Name des zweiten Spielers.
     */
    public String getSpielerNameZwei() {
        return model.getSpielerNameZwei();
    }

    /**
     * Gibt die Nummer des aktuellen Spielers zurück.
     *
     * @return Die Nummer des aktuellen Spielers.
     */
    public int getSpieler(){
        return model.getSpieler();
    }

    /**
     * Leitet eine Anfrage bezüglich der Beendigung des Spiels an das model weiter.
     *
     * @return Die Nummer des Siegers.
     */
    public int istBeendet(){
        return model.beendet();
    }
}
