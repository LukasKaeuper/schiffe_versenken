import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

    private View view;
    private Model model;
    private String modus;
    private int groesse;
    private boolean regelAus;

    public Controller() {
        this.model = new Model(this);
        this.view = new View(new SingleplayerListener(), this, 10);
        this.modus = "lokal_mp";
        this.groesse = 10;
        this.regelAus = false;
    }

    public void viewAktuakisieren() {
        view = new View(new SingleplayerListener(), new Controller(), getGroesse());
    }

    class SpielfeldListener implements ActionListener {
        public SpielfeldListener() {
            FeldAktualisieren("Eigen", getGroesse());
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
            FeldAktualisieren("Eigen", getGroesse());
            FeldAktualisieren("Gegner", getGroesse());
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
            model.spielfeldaktualisieren(groesse);
            FeldAktualisieren("Eigen", groesse);
            setSpielerNameEins("Spieler 1");
            setSpielerNameZwei("Spieler 2");
            view.erstelleSpielfeldListener(new SpielfeldListener(), groesse);
            view.status_fuellen();
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
        regelAus = value;
    }

    public boolean getRegelAus(){
        return regelAus;
    }

    public void setGroesse(int groesse){
        this.groesse = groesse;
    }

    public int getGroesse(){
        return groesse;
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
