import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class View {
    private Spielfeld[][] spielfeldEigen = new Spielfeld[10][10];
    private Spielfeld[][] spielfeldGegner = new Spielfeld[10][10];

    public void erstelleListener(ActionListener al) {
        for (int i=0;i<3;i++) {
            for (int j=0;j<3;j++) {
                this.spielfeldEigen[i][j].setActionCommand("" + i + j);
                this.spielfeldEigen[i][j].addActionListener(al);
                this.spielfeldGegner[i][j].setActionCommand("" + i + j);
                this.spielfeldGegner[i][j].addActionListener(al);
            }
        }
    }

}
