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

            //Muss später zum Rundenwächsel geschoben werden
            eigenesFeldAktualisieren();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Index aus View holen
            String dummy = e.getActionCommand();
            int n = Integer.parseInt(dummy.substring(0, 1));
            int m = Integer.parseInt(dummy.substring(dummy.length() - 1));
            model.schießen(n, m);
            gegnerFeldAktualisieren();
            //int spieler = model.getSpieler();
            // View aktualisieren
            //view.setSpieler(spieler);
            // Spiel beendet
//            if (model.beendet()) {
//                view.setGewonnen(spieler);
//                model.zuruecksetzen();
//            }
//            spielStandAnzeigen();
        }

        private void eigenesFeldAktualisieren() {
            String temp;
            for (int i=0; i<10; i++) {
                for (int j = 0; j < 10; j++) {
                    temp = model.getWert(i, j, "Eigen");
                    view.setButton(i, j, temp, "Eigen");
                }
//                if (temp.equals("Schiff") || temp.equals("Wasser")) {
//                    view.setButton(i, j, temp);
//                }
            }
        }

        private void gegnerFeldAktualisieren() {
            String temp;
            for (int i=0; i<10; i++) {
                for (int j = 0; j < 10; j++) {
                    temp = model.getWert(i, j, "Gegner");
                    view.setButton(i, j, temp, "Gegner");
                }
            }
        }
    }
}
