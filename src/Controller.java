import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

    private View view;
    private Model model;
    private String modus;

    public Controller() {
        this.model = new Model();
        this.view = new View(new SingleplayerListener(), this);
        this.view.erstelleSpielfeldListener(new SpielfeldListener());
        this.modus = "lokal_mp";
    }

    class SpielfeldListener implements ActionListener {
        public SpielfeldListener() {
            FeldAktualisieren("Eigen");
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
            FeldAktualisieren("Eigen");
            FeldAktualisieren("Gegner");
            view.zuegeAktualisieren(model.getSpieler(), model.getZuege());
        }

        private void FeldAktualisieren(String spieler) {
            String temp;
            for (int i=0; i<10; i++) {
                for (int j = 0; j < 10; j++) {
                    temp = model.getWert(i, j, spieler);
                    view.setButton(i, j, temp, spieler);
                }
            }
        }
    }

    class SingleplayerListener implements ActionListener{
        public SingleplayerListener() {}

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Singleplayer\n");
            modus = "sp";
            view.spielFensterSichtbar();
        }
    }
}
