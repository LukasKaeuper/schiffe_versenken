import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

    private View view;
    private Model model;
    private String modus;

    public Controller() {
        this.model = new Model(this);
        this.view = new View(this);
        this.modus = "lokal_mp";
    }

    public void spielfelderInitialisieren() {
        model.spielfeldInitialisieren();
        this.view.erstelleSpielfeldListener(new SpielfeldListener());
    }

    class SpielfeldListener implements ActionListener {
        public SpielfeldListener() {
            feldAktualisieren("Eigen");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Button gedrückt");
            // Index aus View holen
            String dummy = e.getActionCommand();
            int n = Integer.parseInt(dummy.substring(0, 1));
            int m = Integer.parseInt(dummy.substring(dummy.length() - 1));
            if (model.getWert(n, m, "Gegner").equals("Wasser") || model.getWert(n, m, "Gegner").equals("Schiff")) {
                System.out.println("spieler: " + model.getSpieler());
                model.schiessen(n, m);
                if (model.getWert(n, m, "Gegner").equals("Wasser_getroffen") && !modus.equals("sp")) {
                    model.spielerWechseln("lokal_mp");
                    view.rundenwechselBestaetigen();
                    view.setSpieler(model.getSpieler());
                }
                if (model.getWert(n, m, "Gegner").equals("Wasser_getroffen") && modus.equals("sp")) {
                    model.spielerWechseln("sp");
                    model.ki_schiessen();
                }
            }
            if (model.beendet()) {
                view.setGewonnen(model.getSpieler());
                view.listenerEntfernen(this);
                //model.zuruecksetzen();
            }
            feldAktualisieren("Eigen");
            feldAktualisieren("Gegner");
            view.zuegeAktualisieren(model.getSpieler(), model.getZuege());
            view.nameAktualisieren(model.getSpieler(), model.getName());
        }

    }

    public void feldAktualisieren(String spieler) {
        String temp;
        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                temp = model.getWert(i, j, spieler);
                view.setButton(i, j, temp, spieler);
            }
        }
    }

    public void schiffanzeigeBenachrichtigen(int laenge, String spieler){
        System.out.println("Komplettes Schiff der Länge " + laenge + " von Spieler: " + spieler + " getroffen!");
        view.schiffanzeigeAktualisieren(laenge, spieler);
    }

    public void setModus(String modus){
        this.modus = modus;
    }

    public void setSpielerNameEins(String name) {
        model.setSpielerNameEins(name);
    }

    public String getSpielerNameEins() {
        return model.getSpielerNameEins();
    }

    public void setSpielerNameZwei(String name) {
        model.setSpielerNameZwei(name);
    }

    public String getSpielerNameZwei() {
        return model.getSpielerNameZwei();
    }
}
