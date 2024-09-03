import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class View extends JFrame {
    private final JButton[][] buttonSpielfeldEigen = new JButton[10][10];
    private final JButton[][] buttonSpielfeldGegner = new JButton[10][10];
    private JTextField status;
    private JTextField zuege;
    private JTextField nameEins = new JTextField("Lukas", 15);;
    private JTextField nameZwei = new JTextField("Marten", 15);;
    private JPanel schiffanzeigeEigen;
    private JPanel schiffanzeigeGegner;
    private JPanel container;
    private GamePanel panelSpielfeldEigen;
    private GamePanel panelSpielfeldGegner;
    private Controller controller;
    private ImageIcon wasser = new ImageIcon("Bilder/Wasser/NEU_Wasser.gif");
    Font font1 = new Font("SansSerif", Font.BOLD, 20);
    Menu menu;
    JButton zurueckHauptmenue = new JButton("Hauptmenü");
    JButton bestenlistebutton = new JButton("Bestenliste");

    Bestenliste bestenliste = new Bestenliste();

    AbgeschossenBorder abgeschossenBorder = new AbgeschossenBorder(Color.RED, 10);

    public View(Controller controller) {
        super("Schiffe Versenken");
        menu = new Menu();
        this.controller = controller;
        fensterGenerieren();
    }

    private void fensterGenerieren() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1600, 1000);
        setLayout(new BorderLayout());

        zuege = new JTextField("Anzahl an Zügen: 0");

        status = new JTextField(nameEins.getText());
        status.setHorizontalAlignment(JTextField.CENTER);
        status.setFont(font1);
        status.setSize(1500,200);
        add(status, BorderLayout.NORTH);
        status.setEditable(false);
        status.setCaretColor(UIManager.getColor("Panel.background"));

        add(zuege, BorderLayout.SOUTH);
        zuege.setHorizontalAlignment(JTextField.CENTER);
        zuege.setFont(font1);
        zuege.setEditable(false);
        zuege.setCaretColor(UIManager.getColor("Panel.background"));

        schiffanzeigeEigen = new JPanel();
        schiffanzeigeEigen.setLayout(new BoxLayout(schiffanzeigeEigen, BoxLayout.Y_AXIS));
        schiffanzeigeEigen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

        schiffanzeigeGegner = new JPanel();
        schiffanzeigeGegner.setLayout(new BoxLayout(schiffanzeigeGegner, BoxLayout.Y_AXIS));
        schiffanzeigeGegner.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        Dimension anzeigeSize = new Dimension(170, 700);
        schiffanzeigeEigen.setPreferredSize(anzeigeSize);
        schiffanzeigeGegner.setPreferredSize(anzeigeSize);
        schiffanzeigenFuellen();

        panelSpielfeldEigen = new GamePanel("Eigen");
        panelSpielfeldGegner = new GamePanel("Gegner");

        Dimension panelSize = new Dimension(700, 700);
        panelSpielfeldEigen.setPreferredSize(panelSize);
        panelSpielfeldGegner.setPreferredSize(panelSize);

        container = new JPanel();
        container.setLayout(new GridBagLayout());

        containerFuellen();
        bestenlistebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bestenlisteGenerieren();
            }
        });

        zurueckHauptmenue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listenerEntfernen();
                setVisible(false);
                menu.setVisible(true);
                containerFuellen();
            }
        });


//        Dimension textFieldSize = new Dimension(50, 30);
//        schiffanzeigeEigen.setPreferredSize(textFieldSize);
//        schiffanzeigeGegner.setPreferredSize(textFieldSize);

        add(container, BorderLayout.CENTER);
        pack();
    }

    private void schiffanzeigenFuellen() {
        //eigeneSchiffe.add(new )
        schiffanzeigeEigen.add(new Schiffeintrag(5));
        schiffanzeigeEigen.add(new Schiffeintrag(4));
        schiffanzeigeEigen.add(new Schiffeintrag(4));
        schiffanzeigeEigen.add(new Schiffeintrag(3));
        schiffanzeigeEigen.add(new Schiffeintrag(3));
        schiffanzeigeEigen.add(new Schiffeintrag(3));
        schiffanzeigeEigen.add(new Schiffeintrag(2));
        schiffanzeigeEigen.add(new Schiffeintrag(2));
        schiffanzeigeEigen.add(new Schiffeintrag(2));
        schiffanzeigeEigen.add(new Schiffeintrag(2));
        schiffanzeigeGegner.add(new Schiffeintrag(5));
        schiffanzeigeGegner.add(new Schiffeintrag(4));
        schiffanzeigeGegner.add(new Schiffeintrag(4));
        schiffanzeigeGegner.add(new Schiffeintrag(3));
        schiffanzeigeGegner.add(new Schiffeintrag(3));
        schiffanzeigeGegner.add(new Schiffeintrag(3));
        schiffanzeigeGegner.add(new Schiffeintrag(2));
        schiffanzeigeGegner.add(new Schiffeintrag(2));
        schiffanzeigeGegner.add(new Schiffeintrag(2));
        schiffanzeigeGegner.add(new Schiffeintrag(2));
    }

    public void schiffanzeigeAktualisieren(int laenge, String spieler){
        if (spieler.equals("Eigen")){
            for (Component c: schiffanzeigeEigen.getComponents()){
                Schiffeintrag temp = (Schiffeintrag) c;
                if (!temp.istAbgeschossen() && temp.getLaenge() == laenge){
                    temp.setAbgeschossen();
                    return;
                }
            }
        }
        if (spieler.equals("Gegner")){
            for (Component c: schiffanzeigeGegner.getComponents()){
                Schiffeintrag temp = (Schiffeintrag) c;
                if (!temp.istAbgeschossen() && temp.getLaenge() == laenge){
                    temp.setAbgeschossen();
                    return;
                }
            }
        }
    }

    public void schiffanzeigenZuruecksetzen(){
        schiffanzeigeEigen.removeAll();
        schiffanzeigeGegner.removeAll();
        schiffanzeigenFuellen();
    }

    private class Schiffeintrag extends JPanel{
        private int laenge;
        private boolean abgeschossen;
        Dimension buttonSize = new Dimension(10, 10);

        public Schiffeintrag(int laenge){
            this.laenge = laenge;
            this.abgeschossen = false;
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            this.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            for (int i=0; i < laenge; i++){
                JButton button = new JButton();
                button.setBackground(Color.LIGHT_GRAY);
                button.setEnabled(false);
                button.setPreferredSize(buttonSize);
                this.add(button);
            }
        }

        public void einfaerben(){
            for (Component c: this.getComponents()){
                c.setBackground(Color.RED);
            }
        }

        public boolean istAbgeschossen(){
            return abgeschossen;
        }

        public void setAbgeschossen(){
            this.abgeschossen = true;
            this.einfaerben();
        }

        public int getLaenge(){
            return laenge;
        }
    }

    public class Eintrag {
        String name;
        int anzahlZuege;
        String datumZeit;

        public Eintrag(String name, int anzahlZuege, String datumZeit) {
            this.name = name;
            this.anzahlZuege = anzahlZuege;
            this.datumZeit = datumZeit;
        }

        public String getName() {
            return name;
        }

        public int getAnzahlZuege() {
            return anzahlZuege;
        }

        public String getDatumZeit() {
            return datumZeit;
        }

        @Override
        public String toString() {
            return name + "," + anzahlZuege + "," + datumZeit;
        }
    }

    public class Bestenliste {
        private ArrayList<Eintrag> eintraege;
        private String dateiName = "bestenliste.csv";

        public Bestenliste() {
            eintraege = new ArrayList<>();
            laden();
        }

        public void eintragHinzufuegen(String name, int anzahlZuege) {
            String datumZeit = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
            eintraege.add(new Eintrag(name, anzahlZuege, datumZeit));
            Collections.sort(eintraege, Comparator.comparingInt(Eintrag::getAnzahlZuege));
            speichern();
        }

        public void alleEintraegeLoeschen() {
            eintraege.clear();
            speichern();
        }

        public ArrayList<Eintrag> getEintraege() {
            return eintraege;
        }

        private void speichern() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dateiName))) {
                for (Eintrag eintrag : eintraege) {
                    writer.write(eintrag.toString());
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void laden() {
            File file = new File(dateiName);
            if (!file.exists()) {
                System.out.println("Datei " + dateiName + " existiert nicht. Eine neue Datei wird erstellt, wenn Einträge hinzugefügt werden.");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(dateiName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String name = parts[0];
                    int anzahlZuege = Integer.parseInt(parts[1]);
                    String datumZeit = parts[2];
                    eintraege.add(new Eintrag(name, anzahlZuege, datumZeit));
                }
                Collections.sort(eintraege, Comparator.comparingInt(Eintrag::getAnzahlZuege));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void bestenlisteGenerieren(){
        panelSpielfeldEigen.setVisible(false);
        panelSpielfeldGegner.setVisible(false);
        schiffanzeigeEigen.setVisible(false);
        schiffanzeigeGegner.setVisible(false);
        zuege.setVisible(false);
        container.removeAll();
        status.setText("Bestenliste");

        // Bestehende Einträge abrufen
        ArrayList<Eintrag> eintraege = bestenliste.getEintraege();

        Font font1 = new Font("SansSerif", Font.BOLD, 15);

        // Daten für die Tabelle vorbereiten
        String[] spaltenNamen = {"Rang", "Name", "Züge", "Datum/Zeit"};
        String[][] daten = new String[eintraege.size()][4];

        for (int i = 0; i < eintraege.size(); i++) {
            Eintrag eintrag = eintraege.get(i);
            daten[i][0] = Integer.toString(i + 1);
            daten[i][1] = eintrag.getName();
            daten[i][2] = Integer.toString(eintrag.getAnzahlZuege());
            daten[i][3] = eintrag.getDatumZeit();
        }

        // JTable erstellen und in einem JFrame anzeigen
        JTable tabelle = new JTable(daten, spaltenNamen);
        tabelle.getTableHeader().setReorderingAllowed(false);
        tabelle.getTableHeader().setFont(font1);
        JScrollPane scrollPane = new JScrollPane(tabelle);
        tabelle.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < tabelle.getColumnCount(); i++) {
            tabelle.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JButton zurueckbutton = new JButton("Zurück");
        zurueckbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                container.removeAll();
                containerFuellen();
            }
        });

        JButton zurueckHauptmenue = new JButton("Hauptmenü");
        zurueckHauptmenue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                container.removeAll();
                menu.setVisible(true);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1; // Position der Tabelle in der GridBagLayout
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH; // Tabelle füllt den verfügbaren Raum aus
        gbc.insets = new Insets(100, 400, 100, 400); // Abstand um die Tabelle herum
        container.add(scrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 100, 10, 10); // Abstände um den Button herum
        gbc.fill = GridBagConstraints.NONE;
        container.add(zurueckbutton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 100); // Abstände um den Button herum
        gbc.fill = GridBagConstraints.NONE;
        container.add(zurueckHauptmenue, gbc);
    }

    public void bestenlisteGenerierenImMenue(){
        panelSpielfeldEigen.setVisible(false);
        panelSpielfeldGegner.setVisible(false);
        schiffanzeigeEigen.setVisible(false);
        schiffanzeigeGegner.setVisible(false);
        zuege.setVisible(false);
        container.removeAll();
        status.setText("Bestenliste");

        // Bestehende Einträge abrufen
        ArrayList<Eintrag> eintraege = bestenliste.getEintraege();

        Font font1 = new Font("SansSerif", Font.BOLD, 15);

        // Daten für die Tabelle vorbereiten
        String[] spaltenNamen = {"Rang", "Name", "Züge", "Datum/Zeit"};
        String[][] daten = new String[eintraege.size()][4];

        for (int i = 0; i < eintraege.size(); i++) {
            Eintrag eintrag = eintraege.get(i);
            daten[i][0] = Integer.toString(i + 1);
            daten[i][1] = eintrag.getName();
            daten[i][2] = Integer.toString(eintrag.getAnzahlZuege());
            daten[i][3] = eintrag.getDatumZeit();
        }

        // JTable erstellen und in einem JFrame anzeigen
        JTable tabelle = new JTable(daten, spaltenNamen);
        tabelle.getTableHeader().setReorderingAllowed(false);
        tabelle.getTableHeader().setFont(font1);
        JScrollPane scrollPane = new JScrollPane(tabelle);
        tabelle.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < tabelle.getColumnCount(); i++) {
            tabelle.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JButton zurueckHauptmenue = new JButton("Hauptmenü");
        zurueckHauptmenue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                container.removeAll();
                menu.setVisible(true);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1; // Position der Tabelle in der GridBagLayout
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH; // Tabelle füllt den verfügbaren Raum aus
        gbc.insets = new Insets(100, 400, 100, 400); // Abstand um die Tabelle herum
        container.add(scrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // Abstände um den Button herum
        gbc.fill = GridBagConstraints.NONE;
        container.add(zurueckHauptmenue, gbc);
    }


    public void zuegeAktualisieren(int spieler, int anzahlZuege) {
        if (spieler == 1) {
            zuege.setText("Anzahl an Zügen: " + anzahlZuege);
            System.out.println("spieler 1 aktualisiert, Anzahl Zuege: " + anzahlZuege);
        }
        else if (spieler == 2) {
            zuege.setText("Anzahl an Zügen: " + anzahlZuege);
            System.out.println("spieler 2 aktualisiert, Anzahl Zuege: " + anzahlZuege);
        }
    }

    public void nameAktualisieren(int spieler, String name){
        if (spieler == 1) {
            status.setText(name);
        }
        else if (spieler == 2) {
            status.setText(name);
        }
    }

    public void bestenlisteEintragen(int spieler, String name){
        if (spieler == 1) {

        }
        else if (spieler == 2) {

        }
    }

    public void spielFensterSichtbar(){
        setVisible(true);
    }

    public void rundenwechselBestaetigen() {
        panelSpielfeldEigen.setVisible(false);
        panelSpielfeldGegner.setVisible(false);
        schiffanzeigeEigen.setVisible(false);
        schiffanzeigeGegner.setVisible(false);
        zuege.setVisible(false);
        container.removeAll();
//        container.setVisible(false);

        // Schiffanzeigen tauschen
        JPanel temp = new JPanel();
        for (Component c: schiffanzeigeEigen.getComponents()){
            temp.add(c);
        }
        for (Component c: schiffanzeigeGegner.getComponents()){
            schiffanzeigeEigen.add(c);
        }
        for (Component c: temp.getComponents()){
            schiffanzeigeGegner.add(c);
        }

        // neueRunde Button
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
        container.add(schiffanzeigeEigen, c);
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
        container.add(schiffanzeigeGegner, c);
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 5, 10, 0); // Abstände um den Button herum
        c.fill = GridBagConstraints.NONE;
        container.add(bestenlistebutton, c);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 0, 10, 5); // Abstände um den Button herum
        c.fill = GridBagConstraints.NONE;
        container.add(zurueckHauptmenue, c);

        panelSpielfeldEigen.setVisible(true);
        panelSpielfeldGegner.setVisible(true);
        schiffanzeigeEigen.setVisible(true);
        schiffanzeigeGegner.setVisible(true);
        zuege.setVisible(true);
    }

    public void listenerEntfernen() {
        for (ActionListener al: buttonSpielfeldGegner[1][1].getActionListeners()) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    this.buttonSpielfeldGegner[i][j].removeActionListener(al);
                }
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
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
              buttonSpielfeldEigen[i][j].setBackground(Color.decode("#4F84c9"));
            if (buttonSpielfeldEigen[i][j].getIcon() == null) {
                buttonSpielfeldEigen[i][j].setIcon(new ImageIcon(wasser.getImage().getScaledInstance(buttonSpielfeldEigen[i][j].getWidth(), buttonSpielfeldEigen[i][j].getHeight(), Image.SCALE_DEFAULT)));
            }
        } else if (spieler.equals("Eigen") && temp.equals("Wasser_getroffen")) {
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldEigen[i][j].setBackground(Color.decode("#0277bd"));
            if (buttonSpielfeldEigen[i][j].getIcon() == null) {
                buttonSpielfeldEigen[i][j].setIcon(new ImageIcon(wasser.getImage().getScaledInstance(buttonSpielfeldEigen[i][j].getWidth(), buttonSpielfeldEigen[i][j].getHeight(), Image.SCALE_DEFAULT)));
            }
        } else if (spieler.equals("Eigen") && temp.equals("Komplettes_Schiff_getroffen")) {
            buttonSpielfeldEigen[i][j].setText("X");
            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
            buttonSpielfeldEigen[i][j].setForeground(Color.RED);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldEigen[i][j].setIcon(null);
        } else if (spieler.equals("Eigen") && temp.equals("unmoeglich")) {
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setText("O");
//            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldEigen[i][j].setBackground(Color.decode("#4F84c9"));
            if (buttonSpielfeldEigen[i][j].getIcon() == null) {
                buttonSpielfeldEigen[i][j].setIcon(new ImageIcon(wasser.getImage().getScaledInstance(buttonSpielfeldEigen[i][j].getWidth(), buttonSpielfeldEigen[i][j].getHeight(), Image.SCALE_DEFAULT)));
            }
        } else if (spieler.equals("Eigen") && temp.equals("unmoeglich_getroffen")) {
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setText("O");
//            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldEigen[i][j].setBackground(Color.decode("#0277bd"));
            if (buttonSpielfeldEigen[i][j].getIcon() == null) {
                buttonSpielfeldEigen[i][j].setIcon(new ImageIcon(wasser.getImage().getScaledInstance(buttonSpielfeldEigen[i][j].getWidth(), buttonSpielfeldEigen[i][j].getHeight(), Image.SCALE_DEFAULT)));
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
            buttonSpielfeldGegner[i][j].setText(null);
//            buttonSpielfeldGegner[i][j].setText("O");
//            buttonSpielfeldGegner[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldGegner[i][j].setForeground(Color.BLACK);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
//            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
              buttonSpielfeldGegner[i][j].setBackground(Color.decode("#4F84c9"));
            if (buttonSpielfeldGegner[i][j].getIcon() == null) {
                buttonSpielfeldGegner[i][j].setIcon(new ImageIcon(wasser.getImage().getScaledInstance(buttonSpielfeldGegner[i][j].getWidth(), buttonSpielfeldGegner[i][j].getHeight(), Image.SCALE_DEFAULT)));
            }
        } else if (spieler.equals("Gegner") && temp.equals("unmoeglich")) {
            buttonSpielfeldGegner[i][j].setText("X");
            buttonSpielfeldGegner[i][j].setBackground(null);
            buttonSpielfeldGegner[i][j].setForeground(Color.RED);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
            buttonSpielfeldGegner[i][j].setIcon(null);
        } else if (spieler.equals("Gegner") && temp.equals("unmoeglich_getroffen")) {
            buttonSpielfeldGegner[i][j].setText(null);
//            buttonSpielfeldGegner[i][j].setText("O");
//            buttonSpielfeldGegner[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldGegner[i][j].setForeground(Color.BLACK);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
//            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldGegner[i][j].setBackground(Color.decode("#4F84c9"));
            if (buttonSpielfeldGegner[i][j].getIcon() == null) {
                buttonSpielfeldGegner[i][j].setIcon(new ImageIcon(wasser.getImage().getScaledInstance(buttonSpielfeldGegner[i][j].getWidth(), buttonSpielfeldGegner[i][j].getHeight(), Image.SCALE_DEFAULT)));
            }
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

        public Menu() {
            // Fenster initialisieren
            setTitle("Schiffe Versenken");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            // Hintergrundbild
            BufferedImage backgroundImage;
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
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            JButton bestenlisteMenue = new JButton("Bestenliste");
            bestenlisteMenue.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(bestenlisteMenue);

            bestenlisteMenue.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    spielFensterSichtbar();
                    containerFuellen();
                    schiffanzeigenZuruecksetzen();
                    setVisible(false);
                    bestenlisteGenerierenImMenue();
                }
            });

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
            bestenlisteMenue.setMaximumSize(buttonSize);

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
                    System.out.println("Singleplayer\n");
                    controller.setModus("sp");
                    controller.spielfelderInitialisieren();
                    controller.setSpielerNameEins(nameEins.getText());
                    controller.setSpielerNameZwei(nameZwei.getText());
                    containerFuellen();
                    status.setText(nameEins.getText());
                    controller.feldAktualisieren("Gegner");
                    schiffanzeigenZuruecksetzen();
                    zuege.setText("Anzahl an Zügen: 0");
                    setVisible(false);
                    spielFensterSichtbar();
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
                            System.out.println("Lokaler Multiplayer\n");
                            controller.setModus("lokal_mp");
                            controller.spielfelderInitialisieren();
                            controller.setSpielerNameEins(nameEins.getText());
                            controller.setSpielerNameZwei(nameZwei.getText());
                            containerFuellen();
                            status.setText(nameEins.getText());
                            controller.feldAktualisieren("Gegner");
                            schiffanzeigenZuruecksetzen();
                            zuege.setText("Anzahl an Zügen: 0");
                            setVisible(false);
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

                    JButton zurueckButton = new JButton("zurück");
                    zurueckButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    zurueckButton.setMaximumSize(buttonSize);

                zurueckButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buttonPanel.removeAll();

                        buttonPanel.add(singlePlayerButton);
                        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                        buttonPanel.add(multiPlayerButton);
                        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                        buttonPanel.add(settingsButton);
                        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                        buttonPanel.add(regelButton);
                        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                        buttonPanel.add(bestenlisteMenue);

                        buttonPanel.revalidate();
                        buttonPanel.repaint();

                    }
                });


                    buttonPanel.add(localButton);
                    buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Platz zwischen den Buttons
                    buttonPanel.add(onlineButton);
                    buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Platz zwischen den Buttons
                    buttonPanel.add(zurueckButton);

                    // Panel neu validieren und neu zeichnen
                    buttonPanel.revalidate();
                    buttonPanel.repaint();
                }
            });

            settingsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonPanel.removeAll();


                    buttonPanel.setOpaque(true);  // Macht das Panel transparent
                    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

                    JLabel labelSpielerEins = new JLabel("Name Spieler 1:");
                    buttonPanel.add(labelSpielerEins);

                    buttonPanel.add(nameEins);

                    JLabel labelSpielerZwei = new JLabel("Name Spieler 2:");
                    buttonPanel.add(labelSpielerZwei);

                    buttonPanel.add(nameZwei);

                    JLabel labelBestenliste = new JLabel("Bestenliste löschen");
                    buttonPanel.add(labelBestenliste);
                    buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                    JButton loeschen = new JButton("OK");

                    loeschen.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            bestenliste.alleEintraegeLoeschen();
                        }
                    });

                    buttonPanel.add(loeschen);
                    JButton zurueckButton = new JButton("zurück");
                    zurueckButton.setAlignmentY(Component.CENTER_ALIGNMENT);
                    buttonPanel.add(Box.createRigidArea(new Dimension(0,30)));
                    buttonPanel.add(zurueckButton);
                    buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));

                    zurueckButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            buttonPanel.removeAll();

                            buttonPanel.setOpaque(false);

                            buttonPanel.add(singlePlayerButton);
                            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                            buttonPanel.add(multiPlayerButton);
                            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                            buttonPanel.add(settingsButton);
                            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                            buttonPanel.add(regelButton);
                            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                            buttonPanel.add(bestenlisteMenue);

                            buttonPanel.revalidate();
                            buttonPanel.repaint();
                        }
                    });
                    buttonPanel.revalidate();
                    buttonPanel.repaint();
                }
            });
            setVisible(true);
        }

    }
}
