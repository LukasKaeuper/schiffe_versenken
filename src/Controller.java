import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

    private View view;
    private Model model;

    public Controller() {
        this.model = new Model();
        this.view = new View();
        this.view.erstelleListener(new MeinListener());
    }

    class MeinListener implements ActionListener {
        public MeinListener() {
            FeldAktualisieren("Eigen");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("spieler: " + model.getSpieler());
            System.out.println("Button gedrückt");
            // Index aus View holen
            String dummy = e.getActionCommand();
            int n = Integer.parseInt(dummy.substring(0, 1));
            int m = Integer.parseInt(dummy.substring(dummy.length() - 1));
            model.schiessen(n, m);
            if (model.getWert(n, m, "Gegner").equals("Wasser_getroffen")) {
                model.spielerWechseln();
            }
            int spieler = model.getSpieler();
            view.setSpieler(spieler);
            if (model.beendet()) {
                view.setGewonnen(spieler);
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
}
