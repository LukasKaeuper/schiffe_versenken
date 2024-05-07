import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.*;

public class View extends JFrame {
    private JButton[][] buttonSpielfeldEigen = new JButton[10][10];
    private JButton[][] buttonSpielfeldGegner = new JButton[10][10];
    private JTextField status;

    public View() {
        super("Schiffe Versenken");
        fensterGenerieren();
    }

    private void fensterGenerieren() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,600);
        setLayout(new BorderLayout());
        status = new JTextField("Spieler 1");
        add(status,BorderLayout.NORTH);
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(1,2));
        GamePanel panelSpielfeldEigen = new GamePanel("Eigen");
        GamePanel panelSpielfeldGegner = new GamePanel("Gegner");
        container.add(panelSpielfeldEigen);
        container.add(panelSpielfeldGegner);
        add(container, BorderLayout.CENTER);
        setVisible(true);
    }

    class GamePanel extends JPanel {
        public GamePanel(String spieler) {
            setLayout(new GridLayout(10,10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            for (int i=0;i<10;i++) {
                for (int j=0;j<10;j++) {
                    if (spieler == "Eigen") {
                        buttonSpielfeldEigen[i][j] = new JButton();
                        add(buttonSpielfeldEigen[i][j]);
                    } else {
                        buttonSpielfeldGegner[i][j] = new JButton();
                        add(buttonSpielfeldGegner[i][j]);
                    }
                }
            }
        }
    }

    public void erstelleListener(ActionListener al) {
        for (int i=0;i<3;i++) {
            for (int j=0;j<3;j++) {
                this.buttonSpielfeldEigen[i][j].setActionCommand("" + i + j);
                this.buttonSpielfeldEigen[i][j].addActionListener(al);
                this.buttonSpielfeldGegner[i][j].setActionCommand("" + i + j);
                this.buttonSpielfeldGegner[i][j].addActionListener(al);
            }
        }
    }

}
