import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JTextField schiffe;
    private JPanel container;
    private GamePanel panelSpielfeldEigen;
    private GamePanel panelSpielfeldGegner;
    private Menu menu;

    AbgeschossenBorder abgeschossenBorder = new AbgeschossenBorder(Color.RED, 10);

    public View() {
        super("Schiffe Versenken");
        menu = new Menu();
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

        schiffe = new JTextField();
        schiffe.setFont(font1);
        schiffe.setSize(200, 200);
        schiffe.setEditable(false);
        schiffe.setCaretColor(UIManager.getColor("Panel.background"));

        container = new JPanel();

        panelSpielfeldEigen = new GamePanel("Eigen");
        panelSpielfeldGegner = new GamePanel("Gegner");

        container.add(schiffe);
        Dimension textFieldSize = new Dimension(50, 30);
        schiffe.setPreferredSize(textFieldSize);

        Dimension panelSize = new Dimension(700, 700);
        panelSpielfeldEigen.setPreferredSize(panelSize);
        panelSpielfeldGegner.setPreferredSize(panelSize);
        container.add(panelSpielfeldEigen);
        container.add(panelSpielfeldGegner);
        add(container, BorderLayout.CENTER);
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

    private void spielFensterSichtbar(){
        setVisible(true);
    }

    public void rundenwechselBestaetigen() {
        panelSpielfeldEigen.setVisible(false);
        panelSpielfeldGegner.setVisible(false);
        schiffe.setVisible(false);
        zuege.setVisible(false);

        GridBagConstraints gbc = new GridBagConstraints();

        // Panel für die Buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);  // Macht das Panel transparent
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Einstellungen Button
        JButton neueRundeButton = new JButton("Runde beginnen");
        neueRundeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(neueRundeButton);

//        // Alle Buttons gleich groß machen
//        Dimension buttonSize = new Dimension(400, 100);
//        neueRundeButton.setMaximumSize(buttonSize);

        // Button Panel in die Mitte setzen
        gbc.gridx = 0;
        gbc.gridy = 1;
        //gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        container.add(buttonPanel, gbc);

        neueRundeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("neue Runde gedrückt");

                buttonPanel.removeAll();

                panelSpielfeldEigen.setVisible(true);
                panelSpielfeldGegner.setVisible(true);
                schiffe.setVisible(true);
                zuege.setVisible(true);

                // Panel neu validieren und neu zeichnen
//                buttonPanel.revalidate();
//                buttonPanel.repaint();
            }
        });
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
        } else if (spieler.equals("Eigen") && temp.equals("Schiff_getroffen")) {
            buttonSpielfeldEigen[i][j].setText("X");
            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
        } else if (spieler.equals("Eigen") && temp.equals("Wasser")) {
            buttonSpielfeldEigen[i][j].setText("O");
            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
        } else if (spieler.equals("Eigen") && temp.equals("Wasser_getroffen")) {
            buttonSpielfeldEigen[i][j].setText("O");
            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
        } else if (spieler.equals("Eigen") && temp.equals("Komplettes_Schiff_getroffen")) {
            buttonSpielfeldEigen[i][j].setText("X");
            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
            buttonSpielfeldEigen[i][j].setForeground(Color.RED);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
        } else if (spieler.equals("Gegner") && temp.equals("Schiff_getroffen")) {
            buttonSpielfeldGegner[i][j].setText("X");
            buttonSpielfeldGegner[i][j].setBackground(Color.GRAY);
            buttonSpielfeldGegner[i][j].setForeground(Color.BLACK);
            buttonSpielfeldGegner[i][j].setBorder(abgeschossenBorder);
        } else if (spieler.equals("Gegner") && temp.equals("Komplettes_Schiff_getroffen")) {
            buttonSpielfeldGegner[i][j].setText("X");
            buttonSpielfeldGegner[i][j].setBackground(Color.GRAY);
            buttonSpielfeldGegner[i][j].setForeground(Color.RED);
            buttonSpielfeldGegner[i][j].setBorder(abgeschossenBorder);
        } else if (spieler.equals("Gegner") && temp.equals("Wasser_getroffen")) {
            buttonSpielfeldGegner[i][j].setText("O");
            buttonSpielfeldGegner[i][j].setBackground(Color.BLUE);
            buttonSpielfeldGegner[i][j].setForeground(Color.BLACK);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
        } else {
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

    public class Menu extends JFrame {

        public Menu() {
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

            // Alle Buttons gleich groß machen
            Dimension buttonSize = new Dimension(200, 50);
            singlePlayerButton.setMaximumSize(buttonSize);
            multiPlayerButton.setMaximumSize(buttonSize);
            settingsButton.setMaximumSize(buttonSize);

            // Button Panel in die Mitte setzen
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(0, 0, 20, 0);
            gbc.anchor = GridBagConstraints.CENTER;
            background.add(buttonPanel, gbc);

            // ActionListener für die Buttons
            singlePlayerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                }
            });

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
