import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

    private View view;
    private Model model;
    private String modus;

    public Controller() {
        this.model = new Model(this);
        this.view = new View(new SingleplayerListener(), this, 10);
        this.view.erstelleSpielfeldListener(new SpielfeldListener(), 10);
        this.modus = "lokal_mp";
    }

    public void viewAktuakisieren() {
        view = new View(new SingleplayerListener(), new Controller(), getSpalte());
    }

    class SpielfeldListener implements ActionListener {
        public SpielfeldListener() {
            FeldAktualisieren("Eigen", getSpalte());
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
            FeldAktualisieren("Eigen", getSpalte());
            FeldAktualisieren("Gegner", getSpalte());
            view.zuegeAktualisieren(model.getSpieler(), model.getZuege());
            if (!model.beendet()){
                view.nameAktualisieren(model.getSpieler(), model.getName());
            }
        }
    }

    public void FeldAktualisieren(String spieler, int x) {
        String temp;
        for (int i=0; i< x; i++) {
            for (int j = 0; j < x; j++) {
                temp = model.getWert(i, j, spieler);
                view.setButton(i, j, temp, spieler);
            }
        }
    }

    public void schiffanzeigeBenachrichtigen(int laenge, String spieler){
        System.out.println("Komplettes Schiff der Länge " + laenge + " von Spieler: " + spieler + " getroffen!");
        view.schiffanzeigeAktualisieren(laenge, spieler);
    }

    class SingleplayerListener implements ActionListener{
        public SingleplayerListener() {}

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Singleplayer\n");
            modus = "sp";
            view.spielFensterSichtbar(true);
            FeldAktualisieren("Eigen", getSpalte());
        }
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

    public void setRegelAus(boolean value){
        model.setRegelAus(value);
    }

    public boolean getRegelAus(){
        return model.getRegelAus();
    }

    public boolean getFeldGroesser() {
        return model.getFeldGroesser();
    }

    public void setFeldGroesser(boolean feldGroesser) {
        model.setFeldGroesser(feldGroesser);
    }

    public void setSpalte(int spalte){
        model.setSpalte(spalte);
    }

    public int getSpalte(){
        return model.getSpalte();
    }

    public void setReihe(int reihe){
        model.setReihe(reihe);
    }

    public int getReihe(){
        return model.getReihe();
    }

    public void spielfeldAktualisiern(int i){
        model.spielfeldaktualisieren(i);
    }
}
