import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

public class View extends JFrame {
    private final JButton[][] buttonSpielfeldEigen = new JButton[10][10];
    private final JButton[][] buttonSpielfeldGegner = new JButton[10][10];
    private JTextField status;
    private JTextField zuege;
    private JTextField schiffeEigen;
    private JTextField schiffeGegner;
    private JPanel container;
    private GamePanel panelSpielfeldEigen;
    private GamePanel panelSpielfeldGegner;
    private Controller controller;

    AbgeschossenBorder abgeschossenBorder = new AbgeschossenBorder(Color.RED, 10);

    public View(ActionListener sp, Controller controller) {
        super("Schiffe Versenken");
        new Menu(sp);
        this.controller = controller;
        fensterGenerieren();
    }

    private void fensterGenerieren() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLayout(new BorderLayout());

        Font font1 = new Font("SansSerif", Font.BOLD, 20);

        status = new JTextField("Spieler 1");
        status.setHorizontalAlignment(JTextField.CENTER);
        status.setFont(font1);
        status.setSize(1500,200);
        add(status, BorderLayout.NORTH);
        status.setEditable(false);
        status.setCaretColor(UIManager.getColor("Panel.background"));

        zuege = new JTextField("Anzahl an Zügen: 0");

        add(zuege, BorderLayout.SOUTH);
        zuege.setHorizontalAlignment(JTextField.CENTER);
        zuege.setFont(font1);
        zuege.setEditable(false);
        zuege.setCaretColor(UIManager.getColor("Panel.background"));

        schiffeEigen = new JTextField();
        schiffeEigen.setFont(font1);
        schiffeEigen.setSize(200, 200);
        schiffeEigen.setEditable(false);
        schiffeEigen.setCaretColor(UIManager.getColor("Panel.background"));

        schiffeGegner = new JTextField();
        schiffeGegner.setFont(font1);
        schiffeGegner.setSize(200, 200);
        schiffeGegner.setEditable(false);
        schiffeGegner.setCaretColor(UIManager.getColor("Panel.background"));

        panelSpielfeldEigen = new GamePanel("Eigen");
        panelSpielfeldGegner = new GamePanel("Gegner");

        Dimension panelSize = new Dimension(700, 700);
        panelSpielfeldEigen.setPreferredSize(panelSize);
        panelSpielfeldGegner.setPreferredSize(panelSize);

        container = new JPanel();
        container.setLayout(new GridBagLayout());

        containerFuellen();

//        Dimension textFieldSize = new Dimension(50, 30);
//        schiffeEigen.setPreferredSize(textFieldSize);
//        schiffeGegner.setPreferredSize(textFieldSize);

        add(container, BorderLayout.CENTER);
        pack();
    }

    public void zuegeAktualisieren(int spieler, int anzahlZuege) {
        if (spieler == 1) {
            zuege.setText("Anzahl an Zügen: " + anzahlZuege);
            //System.out.println("spieler 1 aktualisiert");
        }
        else if (spieler == 2) {
            zuege.setText("Anzahl an Zügen: " + anzahlZuege);
            //System.out.println("spieler 2 aktualisiert");
        }
    }

    public void spielFensterSichtbar(){
        setVisible(true);
    }

    public void rundenwechselBestaetigen() {
        panelSpielfeldEigen.setVisible(false);
        panelSpielfeldGegner.setVisible(false);
        schiffeEigen.setVisible(false);
        schiffeGegner.setVisible(false);
        zuege.setVisible(false);
        container.removeAll();
//        container.setVisible(false);

        // Einstellungen Button
        JButton neueRundeButton = new JButton("Runde beginnen");
        neueRundeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        neueRundeButton.setPreferredSize(new Dimension(300, 100));
        neueRundeButton.setFont(new Font("SansSerif", Font.BOLD, 25));

        // Panel für die Buttons
        JPanel buttonPanel = new JPanel();
//        buttonPanel.setOpaque(false);  // Macht das Panel transparent
//        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(neueRundeButton, BorderLayout.CENTER);

        //container.setLayout(new GridBagLayout());
        container.add(buttonPanel);

        neueRundeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("neue Runde gedrückt");

                buttonPanel.removeAll();

                //container = new JPanel();

                containerFuellen();
                container.remove(buttonPanel);
                //add(container, BorderLayout.CENTER);

                panelSpielfeldEigen.setVisible(true);
                panelSpielfeldGegner.setVisible(true);
                schiffeEigen.setVisible(true);
                schiffeGegner.setVisible(true);
                zuege.setVisible(true);

                // Panel neu validieren und neu zeichnen
//                buttonPanel.revalidate();
//                buttonPanel.repaint();
            }
        });
    }

    private void containerFuellen() {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 1;
        container.add(schiffeEigen, c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        container.add(panelSpielfeldEigen, c);
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        container.add(panelSpielfeldGegner, c);
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 1;
        container.add(schiffeGegner, c);
    }

    public void listenerEntfernen(ActionListener al) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.buttonSpielfeldGegner[i][j].removeActionListener(al);
            }
        }
    }

    class GamePanel extends JPanel {
        public GamePanel(String spieler) {
            setLayout(new GridLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
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

    public void erstelleSpielfeldListener(ActionListener al) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.buttonSpielfeldGegner[i][j].setActionCommand("" + i + j);
                this.buttonSpielfeldGegner[i][j].addActionListener(al);
            }
        }
    }

    public void setButton(int i, int j, String temp, String spieler) {
        if (spieler.equals("Eigen") && temp.equals("Schiff")) {
            buttonSpielfeldEigen[i][j].setText("X");
            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldEigen[i][j].setIcon(null);
        } else if (spieler.equals("Eigen") && temp.equals("Schiff_getroffen")) {
            buttonSpielfeldEigen[i][j].setText("X");
            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldEigen[i][j].setIcon(null);
        } else if (spieler.equals("Eigen") && temp.equals("Wasser")) {
            buttonSpielfeldEigen[i][j].setText("O");
            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            if (buttonSpielfeldEigen[i][j].getIcon() == null) {
                buttonSpielfeldEigen[i][j].setIcon(new ImageIcon(new ImageIcon("Bilder/waves.gif").getImage().getScaledInstance(buttonSpielfeldEigen[i][j].getWidth(), buttonSpielfeldEigen[i][j].getHeight(), Image.SCALE_DEFAULT)));
            }
        } else if (spieler.equals("Eigen") && temp.equals("Wasser_getroffen")) {
            buttonSpielfeldEigen[i][j].setText("O");
            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
        } else if (spieler.equals("Eigen") && temp.equals("Komplettes_Schiff_getroffen")) {
            buttonSpielfeldEigen[i][j].setText("X");
            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
            buttonSpielfeldEigen[i][j].setForeground(Color.RED);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldEigen[i][j].setIcon(null);
        } else if (spieler.equals("Eigen") && temp.equals("unmoeglich")) {
            buttonSpielfeldEigen[i][j].setText("O");
            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            if (buttonSpielfeldEigen[i][j].getIcon() == null) {
                buttonSpielfeldEigen[i][j].setIcon(new ImageIcon(new ImageIcon("Bilder/waves.gif").getImage().getScaledInstance(buttonSpielfeldEigen[i][j].getWidth(), buttonSpielfeldEigen[i][j].getHeight(), Image.SCALE_DEFAULT)));
            }
        } else if (spieler.equals("Gegner") && temp.equals("Schiff_getroffen")) {
            buttonSpielfeldGegner[i][j].setText("X");
            buttonSpielfeldGegner[i][j].setBackground(Color.GRAY);
            buttonSpielfeldGegner[i][j].setForeground(Color.BLACK);
            buttonSpielfeldGegner[i][j].setBorder(abgeschossenBorder);
            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldGegner[i][j].setIcon(null);
        } else if (spieler.equals("Gegner") && temp.equals("Komplettes_Schiff_getroffen")) {
            buttonSpielfeldGegner[i][j].setText("X");
            buttonSpielfeldGegner[i][j].setBackground(Color.GRAY);
            buttonSpielfeldGegner[i][j].setForeground(Color.RED);
            buttonSpielfeldGegner[i][j].setBorder(abgeschossenBorder);
            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldGegner[i][j].setIcon(null);
        } else if (spieler.equals("Gegner") && temp.equals("Wasser_getroffen")) {
            buttonSpielfeldGegner[i][j].setText("O");
            buttonSpielfeldGegner[i][j].setBackground(Color.BLUE);
            buttonSpielfeldGegner[i][j].setForeground(Color.BLACK);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            if (buttonSpielfeldGegner[i][j].getIcon() == null) {
                buttonSpielfeldGegner[i][j].setIcon(new ImageIcon(new ImageIcon("Bilder/waves.gif").getImage().getScaledInstance(buttonSpielfeldGegner[i][j].getWidth(), buttonSpielfeldGegner[i][j].getHeight(), Image.SCALE_DEFAULT)));
            }
        } else if (spieler.equals("Gegner") && temp.equals("unmoeglich")) {
            buttonSpielfeldGegner[i][j].setText("X");
            buttonSpielfeldGegner[i][j].setBackground(null);
            buttonSpielfeldGegner[i][j].setForeground(Color.RED);
            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
            buttonSpielfeldGegner[i][j].setIcon(null);
        } else {
            buttonSpielfeldGegner[i][j].setText("");
            buttonSpielfeldGegner[i][j].setBackground(null);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldGegner[i][j].setIcon(null);
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

    public class Menu extends JFrame {

        public Menu(ActionListener sp) {
            // Fenster initialisieren
            setTitle("Schiffe Versenken");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            // Hintergrundbild
            final BufferedImage backgroundImage;
            try {
                backgroundImage = ImageIO.read(new File("Bilder/Logo/SchiffeVersenkenLogo.png"));
            } catch (IOException e) {
                throw new RuntimeException("Background image not found", e);
            }

            JPanel background = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            };
            setContentPane(background);
            background.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            // Logo/Icon hinzufügen
            ImageIcon icon = new ImageIcon("Bilder/Logo/SchiffeVersenkenLogo.png");
            setIconImage(icon.getImage());

            // Panel für die Buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false);  // Macht das Panel transparent
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

            // Einzelspieler Button
            JButton singlePlayerButton = new JButton("Einzelspieler");
            singlePlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(singlePlayerButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Platz zwischen den Buttons

            // Mehrspieler Button
            JButton multiPlayerButton = new JButton("Mehrspieler");
            multiPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(multiPlayerButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Platz zwischen den Buttons

            // Einstellungen Button
            JButton settingsButton = new JButton("Einstellungen");
            settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(settingsButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Regel Button
            JButton regelButton = new JButton("Regeln");
            regelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(regelButton);

            // Create the hover window
            JFrame hoverFrame = new JFrame("Regeln");
            hoverFrame.setSize(600, 600);
            hoverFrame.setLayout(new FlowLayout());
            hoverFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            // Create the label for hover text
            JLabel hoverTextLabel = new JLabel("<html>Herzlich Willkommen beim Spiel Schiffe Versenken!<br/><br/>Regeln<br/><br/>Die Schiffe dürfen nicht aneinander liegen sondern haben immer mindestens ein Feld Abstand<br/><br/>Vorbereitung : Jeder Spieler bekommt zufällig plazierte Schiffe<br/>Das Spielfeld besteht aus einer Größe von 10x10<br/><br/>Spielablauf: Die Spieler können abwechselnd ein Feld auswählen auf das Sie abfeuern möchten<br/>Trifft ein Spieler ein Feld wo sich ein Schiff befindet darf dieser nochmal feuern<br/><br/>Gewonnen: Der Spieler, der zuerst alle Schiffe des Gegners versenkt, gewinnt das Spiel<br/><br/>Anzahl an Schiffen: Insgesamt 10<br/> 1x5<br/>2x4<br/>3x3<br/>4x2</html>");
            hoverTextLabel.setForeground(Color.BLACK);

            // Add the label to the hover window
            hoverFrame.add(hoverTextLabel);

            // Add mouse listener to handle hover effect
            regelButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hoverFrame.setVisible(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hoverFrame.setVisible(false);
                }
            });


            // Alle Buttons gleich groß machen
            Dimension buttonSize = new Dimension(200, 50);
            singlePlayerButton.setMaximumSize(buttonSize);
            multiPlayerButton.setMaximumSize(buttonSize);
            settingsButton.setMaximumSize(buttonSize);
            regelButton.setMaximumSize(buttonSize);

            // Button Panel in die Mitte setzen
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(0, 0, 20, 0);
            gbc.anchor = GridBagConstraints.CENTER;
            background.add(buttonPanel, gbc);

            // ActionListener für die Buttons

            singlePlayerButton.addActionListener(sp);

            multiPlayerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Mehrspieler Button gedrückt");

                    // Entferne alle Buttons außer dem Mehrspieler-Button
                    buttonPanel.removeAll();

                    // Neue Buttons für "Lokal" und "Online" hinzufügen
                    JButton localButton = new JButton("Lokal");
                    localButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    localButton.setMaximumSize(buttonSize);
                    localButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            spielFensterSichtbar();
                        }
                    });

                    JButton onlineButton = new JButton("Online");
                    onlineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    onlineButton.setMaximumSize(buttonSize);
                    onlineButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("Online Button gedrückt");
                        }
                    });

                    buttonPanel.add(localButton);
                    buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Platz zwischen den Buttons
                    buttonPanel.add(onlineButton);

                    // Panel neu validieren und neu zeichnen
                    buttonPanel.revalidate();
                    buttonPanel.repaint();
                }
            });

            settingsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Einstellungen Button gedrückt");
                    JDialog settingsDialog = new JDialog(Menu.this, "Einstellungen", true);
                    settingsDialog.setSize(300, 200);
                    settingsDialog.setLayout(new FlowLayout());
                    settingsDialog.setLocationRelativeTo(Menu.this);

                    JButton fullscreenButton = new JButton("Vollbildmodus");
                    fullscreenButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("Vollbildmodus Button gedrückt");
                            setExtendedState(JFrame.MAXIMIZED_BOTH);
                            dispose();
                            setUndecorated(true);
                            setVisible(true);
                        }
                    });

                    settingsDialog.add(fullscreenButton);
                    settingsDialog.setVisible(true);
                }
            });

            setVisible(true);
        }
    }
}
