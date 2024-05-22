import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

public class View extends JFrame {
    private final JButton[][] buttonSpielfeldEigen = new JButton[10][10];
    private final JButton[][] buttonSpielfeldGegner = new JButton[10][10];
    private JTextField status;
    AbgeschossenBorder abgeschossenBorder = new AbgeschossenBorder(Color.RED, 10);

    public View() {
        super("Schiffe Versenken");
        fensterGenerieren();
    }

    private void fensterGenerieren() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1500,900);
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
                    if (spieler.equals("Eigen")) {
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
        for (int i=0;i<10;i++) {
            for (int j=0;j<10;j++) {
                //this.buttonSpielfeldEigen[i][j].setActionCommand("" + i + j);
                //this.buttonSpielfeldEigen[i][j].addActionListener(al);
                this.buttonSpielfeldGegner[i][j].setActionCommand("" + i + j);
                this.buttonSpielfeldGegner[i][j].addActionListener(al);
            }
        }
    }

    public void setButton(int i, int j, String temp, String spieler) {
        if (spieler.equals("Eigen") && temp.equals("Schiff")) {
            buttonSpielfeldEigen[i][j].setText("X");
            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
        }
        else if (spieler.equals("Eigen") && temp.equals("Schiff_getroffen")) {
            buttonSpielfeldEigen[i][j].setText("X");
            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
            //buttonSpielfeldEigen[i][j].setForeground(Color.RED);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
        }
        else if (spieler.equals("Eigen") && temp.equals("Wasser")) {
            buttonSpielfeldEigen[i][j].setText("O");
            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
        }
        else if (spieler.equals("Eigen") && temp.equals("Wasser_getroffen")) {
            buttonSpielfeldEigen[i][j].setText("O");
            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
            //buttonSpielfeldEigen[i][j].setForeground(Color.RED);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
        }
        else if (spieler.equals("Eigen") && temp.equals("Komplettes_Schiff_getroffen")) {
            buttonSpielfeldEigen[i][j].setText("X");
            buttonSpielfeldEigen[i][j].setBackground(Color.RED);
            //buttonSpielfeldEigen[i][j].setForeground(Color.RED);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
        }
        else if (spieler.equals("Gegner") && temp.equals("Schiff_getroffen")) {
            buttonSpielfeldGegner[i][j].setText("X");
            buttonSpielfeldGegner[i][j].setBackground(Color.GRAY);
            //buttonSpielfeldGegner[i][j].setForeground(Color.RED);
            buttonSpielfeldGegner[i][j].setBorder(abgeschossenBorder);
        }
        else if (spieler.equals("Gegner") && temp.equals("Komplettes_Schiff_getroffen")) {
            buttonSpielfeldGegner[i][j].setText("X");
            buttonSpielfeldGegner[i][j].setBackground(Color.RED);
            //buttonSpielfeldEigen[i][j].setForeground(Color.RED);
            buttonSpielfeldGegner[i][j].setBorder(abgeschossenBorder);
        }
        else if (spieler.equals("Gegner") && temp.equals("Wasser_getroffen")) {
            buttonSpielfeldGegner[i][j].setText("O");
            buttonSpielfeldGegner[i][j].setBackground(Color.BLUE);
            //buttonSpielfeldGegner[i][j].setForeground(Color.RED);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
        }
        else {
            buttonSpielfeldGegner[i][j].setText("");
            buttonSpielfeldGegner[i][j].setBackground(null);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
        }
    }

    public void setSpieler(int spieler) {
        status.setText("Spieler " + spieler);
    }

    public void setGewonnen(int spieler) {
        status.setText("Spieler " + spieler + " hat gewonnen!");
    }

    private static class AbgeschossenBorder implements Border {

        private final int radius;
        private final Color color;

        private AbgeschossenBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.drawRect(x, y, width - 1, height - 1);
        }
    }
}
