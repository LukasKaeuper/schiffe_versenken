import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Die View-Klasse stellt die grafische Benutzeroberfläche für das "Schiffe Versenken"-Spiel bereit.
 * Sie enthält die Spielfelder für den eigenen und gegnerischen Bereich, sowie Steuerungselemente
 * wie Schaltflächen und Textfelder, um den Status und die Anzahl der Züge anzuzeigen.
 */
public class View extends JFrame {
    private final JButton[][] buttonSpielfeldEigen = new JButton[10][10];                       // Schaltflächen für das eigene Spielfeld
    private final JButton[][] buttonSpielfeldGegner = new JButton[10][10];                      // Schaltflächen für das gegnerische Spielfeld
    private JTextField status;                                                                  // Textfeld zur Anzeige des aktuellen Status
    private JTextField zuege;                                                                   // Textfeld zur Anzeige der Anzahl der Züge
    private JTextField nameEins = new JTextField("Lukas", 15);                     // Textfeld für den Namen des ersten Spielers
    private JTextField nameZwei = new JTextField("Marten", 15);                    // Textfeld für den Namen des zweiten Spielers
    private JPanel schiffanzeigeEigen;                                                          // Panel zur Anzeige von Schiffspositionen des eigenen Spielfelds
    private JPanel schiffanzeigeGegner;                                                         // Panel zur Anzeige von Schiffspositionen des gegnerischen Spielfelds
    private JPanel container;                                                                   // Container-Panel für die Spielfelder
    private GamePanel panelSpielfeldEigen;                                                      // GamePanel für das eigene Spielfeld
    private GamePanel panelSpielfeldGegner;                                                     // GamePanel für das gegnerische Spielfeld
    private Controller controller;                                                              // Controller zur Steuerung der Spiellogik
    private ImageIcon wasser;                                                                   // Icon für Wasser
    private ImageIcon wasser_unmoeglich;                                                        // Icon für unzulässige Wasserbereiche
    private ImageIcon uboot_einzel;                                                             // Icon für ein einzelnes U-Boot
    private ImageIcon uboot_horizontal_hinten;                                                  // Icon für das hintere Ende eines horizontalen U-Boots
    private ImageIcon uboot_horizontal_mitte;                                                   // Icon für die Mitte eines horizontalen U-Boots
    private ImageIcon uboot_horizontal_vorne;                                                   // Icon für die vordere Seite eines horizontalen U-Boots
    private ImageIcon uboot_vertikal_hinten;                                                    // Icon für das hintere Ende eines vertikalen U-Boots
    private ImageIcon uboot_vertikal_mitte;                                                     // Icon für die Mitte eines vertikalen U-Boots
    private ImageIcon uboot_vertikal_vorne;                                                     // Icon für die vordere Seite eines vertikalen U-Boots
    private ImageIcon uboot_horizontal_hinten_feuer;                                            // Icon für das hintere Ende eines horizontalen U-Boots nach einem Treffer
    private ImageIcon uboot_horizontal_mitte_feuer;                                             // Icon für die Mitte eines horizontalen U-Boots nach einem Treffer
    private ImageIcon uboot_horizontal_vorne_feuer;                                             // Icon für die vordere Seite eines horizontalen U-Boots nach einem Treffer
    private ImageIcon uboot_vertikal_hinten_feuer;                                              // Icon für das hintere Ende eines vertikalen U-Boots nach einem Treffer
    private ImageIcon uboot_vertikal_mitte_feuer;                                               // Icon für die Mitte eines vertikalen U-Boots nach einem Treffer
    private ImageIcon uboot_vertikal_vorne_feuer;                                               // Icon für die vordere Seite eines vertikalen U-Boots nach einem Treffer
    private ArrayList<ImageIcon> schiffGifs = new ArrayList<>();                                // Liste der Schiff-Icons
    private ArrayList<ImageIcon> andereGifs = new ArrayList<>();                                // Liste der anderen Icons
    Font font1 = new Font("SansSerif", Font.BOLD, 20);                               // Schriftart für die Textfelder
    Menu menu;                                                                                  // Hauptmenü
    JButton zurueckHauptmenue = new JButton("Hauptmenü");                                  // Schaltfläche zum Zurückkehren zum Hauptmenü
    JButton bestenlistebutton = new JButton("Bestenliste");                                // Schaltfläche zur Anzeige der Bestenliste

    Bestenliste bestenliste = new Bestenliste();                                                // Instanz der Bestenliste

    AbgeschossenBorder abgeschossenBorder = new AbgeschossenBorder(Color.RED, 10);        // Border für abgeschossene Schiffe

    /**
     * Konstruktor für die View-Klasse.
     * @param controller Der Controller, der die Spiellogik steuert.
     */
    public View(Controller controller) {
        super("Schiffe Versenken");                                                          // Titel des Fensters
        menu = new Menu();                                                                        // Initialisiert das Hauptmenü
        this.controller = controller;                                                             // Setzt den Controller
        fensterGenerieren();                                                                      // Generiert das Fenster


        // Skaliert die Icons entsprechend der Größe der Schaltflächen im Spielfeld
        uboot_einzel = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_einzel.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_horizontal_hinten = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_horizontal_hinten.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_horizontal_mitte = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_horizontal_mitte.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_horizontal_vorne = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_horizontal_vorne.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_vertikal_hinten = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_vertikal_hinten.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_vertikal_mitte = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_vertikal_mitte.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_vertikal_vorne = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_vertikal_vorne.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_horizontal_hinten_feuer = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_horizontal_hinten_feuer.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_horizontal_mitte_feuer = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_horizontal_mitte_feuer.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_horizontal_vorne_feuer = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_horizontal_vorne_feuer.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_vertikal_hinten_feuer = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_vertikal_hinten_feuer.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_vertikal_mitte_feuer = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_vertikal_mitte_feuer.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        uboot_vertikal_vorne_feuer = new ImageIcon(new ImageIcon("Bilder/Wasser/uboot_vertikal_vorne_feuer.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        wasser = new ImageIcon(new ImageIcon("Bilder/Wasser/wasser.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));
        wasser_unmoeglich = new ImageIcon(new ImageIcon("Bilder/Wasser/wasser_unmöglich_kreis.gif").getImage().getScaledInstance(buttonSpielfeldGegner[1][1].getWidth(), buttonSpielfeldGegner[1][1].getHeight(), Image.SCALE_DEFAULT));

        schiffGifs.add(uboot_einzel);
        schiffGifs.add(uboot_horizontal_hinten);
        schiffGifs.add(uboot_horizontal_mitte);
        schiffGifs.add(uboot_horizontal_vorne);
        schiffGifs.add(uboot_vertikal_hinten);
        schiffGifs.add(uboot_vertikal_mitte);
        schiffGifs.add(uboot_vertikal_vorne);
        schiffGifs.add(uboot_horizontal_hinten_feuer);
        schiffGifs.add(uboot_horizontal_mitte_feuer);
        schiffGifs.add(uboot_horizontal_vorne_feuer);
        schiffGifs.add(uboot_vertikal_hinten_feuer);
        schiffGifs.add(uboot_vertikal_mitte_feuer);
        schiffGifs.add(uboot_vertikal_vorne_feuer);
        andereGifs.add(wasser);
        andereGifs.add(wasser_unmoeglich);
    }

    /**
     * Generiert das Fenster und fügt alle Komponenten hinzu.
     */
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
                iconsEntfernen();
            }
        });


//        Dimension textFieldSize = new Dimension(50, 30);
//        schiffanzeigeEigen.setPreferredSize(textFieldSize);
//        schiffanzeigeGegner.setPreferredSize(textFieldSize);

        add(container, BorderLayout.CENTER);
        pack();
    }

    /**
     * Entfernt alle Icons von den Schaltflächen des Spielfelds.
     */
    private void iconsEntfernen() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                buttonSpielfeldEigen[i][j].setIcon(null);
                buttonSpielfeldGegner[i][j].setIcon(null);
            }
        }
    }

    /**
     * Füllt die Panels für die Schiffsanzeige mit den Schiffen für den eigenen und den gegnerischen Bereich.
     * Jedes Panel zeigt eine Liste von Schiffsarten in absteigender Reihenfolge der Größe.
     */
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

    /**
     * Aktualisiert die Anzeige eines Schiffes in der Schiffsanzeige für den angegebenen Spieler.
     * Setzt den Status des Schiffes auf "abgeschossen", wenn das Schiff noch nicht als abgeschossen markiert ist.
     *
     * @param laenge Die Länge des Schiffes, das aktualisiert werden soll.
     * @param spieler Der Name des Spielers ("Eigen" oder "Gegner"), dessen Schiffsanzeige aktualisiert wird.
     */
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

    /**
     * Setzt die Schiffsanzeige zurück, indem alle bestehenden Einträge entfernt und dann neu gefüllt werden.
     */
    public void schiffanzeigenZuruecksetzen(){
        schiffanzeigeEigen.removeAll();
        schiffanzeigeGegner.removeAll();
        schiffanzeigenFuellen();
    }

    /**
     * Repräsentiert einen Eintrag für ein Schiff in der Schiffsanzeige.
     * Jedes Schiff wird als Reihe von Buttons dargestellt, die die Länge des Schiffes repräsentieren.
     */
    private class Schiffeintrag extends JPanel{
        private int laenge;                                                      // Die Länge des Schiffes
        private boolean abgeschossen;                                            // Status, ob das Schiff abgeschossen wurde
        Dimension buttonSize = new Dimension(10, 10);               // Standardgröße der Buttons, die das Schiff darstellen

        /**
         * Konstruktor für die Schiffeintrag-Klasse.
         * Initialisiert die Länge des Schiffes und den Status des Schiffes.
         * Erstellt eine Reihe von Buttons, die das Schiff darstellen.
         *
         * @param laenge Die Länge des Schiffes.
         */
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

        /**
         * Färbt alle Buttons im Panel rot ein, um anzuzeigen, dass das Schiff abgeschossen wurde.
         */
        public void einfaerben(){
            for (Component c: this.getComponents()){
                c.setBackground(Color.RED);
            }
        }

        /**
         * Gibt zurück, ob das Schiff abgeschossen wurde.
         *
         * @return true, wenn das Schiff abgeschossen wurde, andernfalls false.
         */
        public boolean istAbgeschossen(){
            return abgeschossen;
        }

        /**
         * Setzt den Status des Schiffes auf abgeschossen und färbt alle Buttons rot ein.
         */
        public void setAbgeschossen(){
            this.abgeschossen = true;
            this.einfaerben();
        }

        /**
         * Gibt die Länge des Schiffes zurück.
         *
         * @return Die Länge des Schiffes.
         */
        public int getLaenge(){
            return laenge;
        }
    }

    /**
     * Repräsentiert einen Eintrag in der Bestenliste.
     * Jeder Eintrag enthält den Namen des Spielers, die Anzahl der Züge und das Datum/Zeit des Eintrags.
     */
    public class Eintrag {
        String name;              // Name des Spielers
        int anzahlZuege;          // Anzahl der Züge des Spielers
        String datumZeit;         // Datum und Zeit des Eintrags

        /**
         * Konstruktor für die Eintrag-Klasse.
         *
         * @param name Der Name des Spielers.
         * @param anzahlZuege Die Anzahl der Züge des Spielers.
         * @param datumZeit Datum und Zeit des Eintrags.
         */
        public Eintrag(String name, int anzahlZuege, String datumZeit) {
            this.name = name;
            this.anzahlZuege = anzahlZuege;
            this.datumZeit = datumZeit;
        }

        /**
         * Gibt den Namen des Spielers zurück.
         *
         * @return Der Name des Spielers.
         */
        public String getName() {
            return name;
        }

        /**
         * Gibt die Anzahl der Züge zurück.
         *
         * @return Die Anzahl der Züge.
         */
        public int getAnzahlZuege() {
            return anzahlZuege;
        }

        /**
         * Gibt das Datum und die Zeit des Eintrags zurück.
         *
         * @return Datum und Zeit des Eintrags.
         */
        public String getDatumZeit() {
            return datumZeit;
        }

        /**
         * Gibt eine String-Darstellung des Eintrags zurück, die für die CSV-Datei verwendet werden kann.
         *
         * @return Die String-Darstellung des Eintrags.
         */
        @Override
        public String toString() {
            return name + "," + anzahlZuege + "," + datumZeit;
        }
    }

    /**
     * Verwaltet eine Bestenliste, die Einträge in einer CSV-Datei speichert und lädt.
     * Die Bestenliste wird nach der Anzahl der Züge sortiert.
     */
    public class Bestenliste {
        private ArrayList<Eintrag> eintraege;
        private String dateiName = "bestenliste.csv";

        /**
         * Konstruktor für die Bestenliste-Klasse.
         * Lädt bestehende Einträge aus der Datei, wenn vorhanden.
         */
        public Bestenliste() {
            eintraege = new ArrayList<>();
            laden();
        }

        /**
         * Fügt einen neuen Eintrag zur Bestenliste hinzu und speichert die Liste in der Datei.
         * Die Liste wird nach der Anzahl der Züge sortiert.
         *
         * @param name Der Name des Spielers.
         * @param anzahlZuege Die Anzahl der Züge des Spielers.
         */
        public void eintragHinzufuegen(String name, int anzahlZuege) {
            String datumZeit = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
            eintraege.add(new Eintrag(name, anzahlZuege, datumZeit));
            Collections.sort(eintraege, Comparator.comparingInt(Eintrag::getAnzahlZuege));
            speichern();
        }

        /**
         * Löscht alle Einträge aus der Bestenliste und speichert diese leere Liste in der Datei.
         */
        public void alleEintraegeLoeschen() {
            eintraege.clear();
            speichern();
        }

        /**
         * Gibt die Liste der Einträge in der Bestenliste zurück.
         *
         * @return Die Liste der Einträge.
         */
        public ArrayList<Eintrag> getEintraege() {
            return eintraege;
        }

        /**
         * Speichert die Einträge der Bestenliste in einer CSV-Datei.
         */
        private void speichern() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dateiName))) {
                for (Eintrag eintrag : eintraege) {
                    writer.write(eintrag.toString());             // Schreibt jeden Eintrag als Zeile in die Datei
                    writer.newLine();                             // Fügt einen Zeilenumbruch nach jedem Eintrag hinzu
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Lädt die Einträge aus der CSV-Datei, wenn diese existiert.
         */
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

    /**
     * Generiert und zeigt die Bestenliste in einem Container an.
     * Die bestehenden Panels werden ausgeblendet und eine Tabelle wird erstellt, um die Bestenliste darzustellen.
     */
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
                if (controller.istBeendet()){
                    setGewonnen(controller.getName());
                }
                else{
                    nameAktualisieren(controller.getSpieler(), controller.getName());
                }
                containerFuellen();
            }
        });

        JButton zurueckHauptmenue = new JButton("Hauptmenü");
        zurueckHauptmenue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listenerEntfernen();
                setVisible(false);
                container.removeAll();
                menu.setVisible(true);
                iconsEntfernen();
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

    /**
     * Generiert und zeigt die Bestenliste in einem Container an, wenn Sie über das Hauptmenü aufgerufen wurde.
     * Die bestehenden Panels werden ausgeblendet und eine Tabelle wird erstellt, um die Bestenliste darzustellen.
     */
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


    /**
     * Aktualisiert die Anzeige der Anzahl der Züge für den angegebenen Spieler.
     * Gibt außerdem eine Konsolennachricht aus, die den aktuellen Stand anzeigt.
     *
     * @param spieler Der Spieler, dessen Züge aktualisiert werden sollen (1 oder 2).
     * @param anzahlZuege Die neue Anzahl der Züge des Spielers.
     */
    public void zuegeAktualisieren(int spieler, int anzahlZuege) {
        if (spieler == 1) {
            zuege.setText("Anzahl an Zügen: " + anzahlZuege);
            System.out.println("Spieler: " + controller.getSpielerNameEins() + " aktualisiert, Anzahl Zuege: " + anzahlZuege + "\n");
        }
        else if (spieler == 2) {
            zuege.setText("Anzahl an Zügen: " + anzahlZuege);
            System.out.println("Spieler: " + controller.getSpielerNameEins() + " aktualisiert, Anzahl Zuege: " + anzahlZuege + "\n");
        }
    }

    /**
     * Aktualisiert den Namen des aktuellen Spielers im Statusbereich.
     *
     * @param spieler Der Spieler, dessen Name aktualisiert werden soll (1 oder 2).
     * @param name Der neue Name des Spielers.
     */
    public void nameAktualisieren(int spieler, String name){
        if (spieler == 1) {
            status.setText(name);
        }
        else if (spieler == 2) {
            status.setText(name);
        }
    }

    /**
     * Fügt einen Eintrag zur Bestenliste hinzu.
     *
     * @param name Der Name des Spielers.
     * @param anzahlZuege Die Anzahl der Züge des Spielers.
     */
    public void bestenlisteEintragen(String name, int anzahlZuege){
        bestenliste.eintragHinzufuegen(name,anzahlZuege);
    }

    /**
     * Macht das Spiel-Fenster sichtbar.
     */
    public void spielFensterSichtbar(){
        setVisible(true);
    }

    /**
     * Führt den Rundenwechsel durch und zeigt den Button zum Starten der neuen Runde an.
     * Die Anzeigen für die Schiffe werden getauscht, und der Button zum Starten der neuen Runde wird erstellt.
     */
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
                iconsEntfernen();
                controller.feldAktualisieren("Eigen");
                controller.feldAktualisieren("Eigen");
                controller.feldAktualisieren("Gegner");
                controller.feldAktualisieren("Gegner");

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

    /**
     * Füllt den Container mit den Panels und Buttons für das aktuelle Spiel.
     * Positioniert die Panels und Buttons mithilfe des GridBagLayout.
     */
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

    /**
     * Entfernt alle ActionListener von den Buttons im Spielfeld des Gegners.
     */
    public void listenerEntfernen() {
        for (ActionListener al: buttonSpielfeldGegner[1][1].getActionListeners()) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    this.buttonSpielfeldGegner[i][j].removeActionListener(al);
                }
            }
        }
    }

    /**
     * Die Klasse GamePanel ist eine benutzerdefinierte JPanel-Klasse, die ein
     * Spielfeld in einem GridLayout für ein Spiel darstellt. Abhängig vom übergebenen
     * Spieler wird entweder ein Spielfeld für den "Eigen"-Spieler oder den "Gegner"-Spieler erstellt.
     */
    class GamePanel extends JPanel {

        /**
         * Konstruktor für die GamePanel-Klasse initialisiert das Panel mit einem GridLayout bestehend aus 10x10
         * Buttons. Abhängig vom übergebenen Spieler wird entweder ein Array von Buttons
         * für den Eigen-Spieler oder den Gegner-Spieler erstellt und dem Panel hinzugefügt.
         *
         * @param spieler Ein String, der angibt, für welchen Spieler das Spielfeld erstellt wird.
         */
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

    /**
     * Erstellt ActionListener für das Spielfeld des Gegners.
     *
     * @param al Ein ActionListener, der für die Verarbeitung der Button-Klicks verwendet wird.
     */
    public void erstelleSpielfeldListener(ActionListener al) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.buttonSpielfeldGegner[i][j].setActionCommand("" + i + j);
                this.buttonSpielfeldGegner[i][j].addActionListener(al);
            }
        }
    }

    /**
     * Diese Methode konfiguriert das Aussehen und Verhalten eines Buttons im Spielfeld, abhängig davon,
     * ob es sich um das Spielfeld des eigenen Spielers oder des Gegners handelt und welchen Zustand der
     * Button darstellen soll.
     *
     * @param i Der Index der Reihe im 10x10 Raster, wo der Button gesetzt werden soll.
     * @param j Der Index der Spalte im 10x10 Raster, wo der Button gesetzt werden soll.
     * @param temp Ein String, der den Zustand des Buttons angibt.
     * @param spieler Ein String, der angibt, für welchen Spieler das Spielfeld dargestellt wird.
     */
    public void setButton(int i, int j, String temp, String spieler) {
        if (spieler.equals("Eigen") && temp.equals("Schiff")) {
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setText("X");
//            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
//            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
//            buttonSpielfeldEigen[i][j].setIcon(null);
            schiffGifEinfuegen(i, j, buttonSpielfeldEigen, false);
        } else if (spieler.equals("Eigen") && temp.equals("Schiff_getroffen")) {
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setText("X");
//            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
//            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
//            buttonSpielfeldEigen[i][j].setIcon(null);
            schiffGifEinfuegen(i, j, buttonSpielfeldEigen, false);
        } else if (spieler.equals("Eigen") && temp.equals("Wasser")) {
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
              //buttonSpielfeldEigen[i][j].setBackground(Color.decode("#4F84c9"));
            if (buttonSpielfeldEigen[i][j].getIcon() == null) {
                buttonSpielfeldEigen[i][j].setIcon(wasser);
            }
        } else if (spieler.equals("Eigen") && temp.equals("Wasser_getroffen")) {
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
//            buttonSpielfeldEigen[i][j].setBackground(Color.decode("#0277bd"));
            if (buttonSpielfeldEigen[i][j].getIcon() == null) {
                buttonSpielfeldEigen[i][j].setIcon(wasser);
            }
        } else if (spieler.equals("Eigen") && temp.equals("Komplettes_Schiff_getroffen")) {
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setText("X");
//            buttonSpielfeldEigen[i][j].setBackground(Color.GRAY);
//            buttonSpielfeldEigen[i][j].setForeground(Color.RED);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
//            buttonSpielfeldEigen[i][j].setIcon(null);
            schiffGifEinfuegen(i, j, buttonSpielfeldEigen, true);
        } else if (spieler.equals("Eigen") && temp.equals("unmoeglich")) {
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setText("O");
//            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            //buttonSpielfeldEigen[i][j].setBackground(Color.decode("#4F84c9"));
            if (buttonSpielfeldEigen[i][j].getIcon() == null) {
                buttonSpielfeldEigen[i][j].setIcon(wasser);
            }
        } else if (spieler.equals("Eigen") && temp.equals("unmoeglich_getroffen")) {
            buttonSpielfeldEigen[i][j].setText(null);
//            buttonSpielfeldEigen[i][j].setText("O");
//            buttonSpielfeldEigen[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldEigen[i][j].setForeground(Color.BLACK);
            buttonSpielfeldEigen[i][j].setBorder(abgeschossenBorder);
//            buttonSpielfeldEigen[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            //buttonSpielfeldEigen[i][j].setBackground(Color.decode("#0277bd"));
            if (buttonSpielfeldEigen[i][j].getIcon() == null) {
                buttonSpielfeldEigen[i][j].setIcon(wasser);
            }
        } else if (spieler.equals("Gegner") && temp.equals("Schiff_getroffen")) {
            buttonSpielfeldGegner[i][j].setText(null);
            //buttonSpielfeldGegner[i][j].setText("X");
            //buttonSpielfeldGegner[i][j].setBackground(Color.GRAY);
            //buttonSpielfeldGegner[i][j].setForeground(Color.BLACK);
            //buttonSpielfeldGegner[i][j].setBorder(abgeschossenBorder);
            //buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            schiffGifEinfuegen(i, j, buttonSpielfeldGegner, false);
        } else if (spieler.equals("Gegner") && temp.equals("Komplettes_Schiff_getroffen")) {
            buttonSpielfeldGegner[i][j].setText(null);
//            buttonSpielfeldGegner[i][j].setText("X");
//            buttonSpielfeldGegner[i][j].setBackground(Color.GRAY);
//            buttonSpielfeldGegner[i][j].setForeground(Color.RED);
//            buttonSpielfeldGegner[i][j].setBorder(abgeschossenBorder);
//            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
//            buttonSpielfeldGegner[i][j].setIcon(null);
            schiffGifEinfuegen(i, j, buttonSpielfeldGegner, true);
        } else if (spieler.equals("Gegner") && temp.equals("Wasser_getroffen")) {
            buttonSpielfeldGegner[i][j].setText(null);
//            buttonSpielfeldGegner[i][j].setText("O");
//            buttonSpielfeldGegner[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldGegner[i][j].setForeground(Color.BLACK);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
//            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
              //buttonSpielfeldGegner[i][j].setBackground(Color.decode("#4F84c9"));
            if (buttonSpielfeldGegner[i][j].getIcon() == null) {
                buttonSpielfeldGegner[i][j].setIcon(wasser);
            }
        } else if (spieler.equals("Gegner") && temp.equals("unmoeglich")) {
            buttonSpielfeldGegner[i][j].setText(null);
            //buttonSpielfeldGegner[i][j].setBackground(null);
            //buttonSpielfeldGegner[i][j].setForeground(Color.RED);
            //buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
            //buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
            //buttonSpielfeldGegner[i][j].setIcon(null);
            //buttonSpielfeldGegner[i][j].setBackground(Color.decode("#4F84c9"));
            if (buttonSpielfeldGegner[i][j].getIcon() == null) {
                buttonSpielfeldGegner[i][j].setIcon(wasser_unmoeglich);
            }
            //buttonSpielfeldGegner[i][j].setBackground(Color.RED);
        } else if (spieler.equals("Gegner") && temp.equals("unmoeglich_getroffen")) {
            buttonSpielfeldGegner[i][j].setText(null);
//            buttonSpielfeldGegner[i][j].setText("O");
//            buttonSpielfeldGegner[i][j].setBackground(Color.BLUE);
//            buttonSpielfeldGegner[i][j].setForeground(Color.BLACK);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
//            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            //buttonSpielfeldGegner[i][j].setBackground(Color.decode("#4F84c9"));
            if (buttonSpielfeldGegner[i][j].getIcon() == null) {
                buttonSpielfeldGegner[i][j].setIcon(wasser);
            }
        } else {
            buttonSpielfeldGegner[i][j].setText("");
            buttonSpielfeldGegner[i][j].setBackground(null);
            buttonSpielfeldGegner[i][j].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Button.border"));
            buttonSpielfeldGegner[i][j].setFont(new Font("Arial", Font.PLAIN, 15));
            buttonSpielfeldGegner[i][j].setIcon(null);
        }
    }

    /**
     * Fügt ein Bild für ein Schiff an eine bestimmte Position in einem 2D-Array von JButton-Objekten ein.
     *
     * @param i Die Reihe im Array, an der das Schiff eingefügt werden soll.
     * @param j Die Spalte im Array, an der das Schiff eingefügt werden soll.
     * @param spielfeldButtons Das 2D-Array von JButton-Objekten, das das Spielfeld darstellt.
     * @param kaputt Ein boolescher Wert, der angibt, ob das Schiff beschädigt ist oder nicht.
     */
    private void schiffGifEinfuegen(int i, int j, JButton[][] spielfeldButtons, boolean kaputt){
        if (j-1 < 0 && i-1 < 0){
            if (!schiffGifs.contains(spielfeldButtons[i+1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                spielfeldButtons[i][j].setIcon(uboot_einzel);
            }
            else if (schiffGifs.contains(spielfeldButtons[i+1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne);
            }
        }
        else if (j+1 > 9 && i-1 < 0){
            if (!schiffGifs.contains(spielfeldButtons[i+1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j-1].getIcon())){
                spielfeldButtons[i][j].setIcon(uboot_einzel);
            }
            else if (schiffGifs.contains(spielfeldButtons[i+1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j-1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten);
            }
        }
        else if (j+1 > 9 && i+1 > 9){
            if (!schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j-1].getIcon())){
                spielfeldButtons[i][j].setIcon(uboot_einzel);
            }
            else if (schiffGifs.contains(spielfeldButtons[i-1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j-1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten);
            }
        }
        else if (j-1 < 0 && i+1 > 9){
            if (!schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                spielfeldButtons[i][j].setIcon(uboot_einzel);
            }
            else if (schiffGifs.contains(spielfeldButtons[i-1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne);
            }
        }
        else if (i+1 > 9){
            if (!schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j+1].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j-1].getIcon())){
                spielfeldButtons[i][j].setIcon(uboot_einzel);
            }
            else if (andereGifs.contains(spielfeldButtons[i][j-1].getIcon()) && schiffGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j-1].getIcon()) && schiffGifs.contains(spielfeldButtons[i][j+1].getIcon()) || spielfeldButtons[i][j+1].getIcon() == null || spielfeldButtons[i][j-1].getIcon() == null){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_mitte_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_mitte);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j-1].getIcon()) && andereGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten);
            }
            else if (schiffGifs.contains(spielfeldButtons[i-1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten);
            }
        }
        else if (j+1 > 9){
            if (!schiffGifs.contains(spielfeldButtons[i+1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j-1].getIcon())){
                spielfeldButtons[i][j].setIcon(uboot_einzel);
            }
            else if (andereGifs.contains(spielfeldButtons[i-1][j].getIcon()) && schiffGifs.contains(spielfeldButtons[i+1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne);
            }
            else if (schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && schiffGifs.contains(spielfeldButtons[i+1][j].getIcon()) || spielfeldButtons[i-1][j].getIcon() == null || spielfeldButtons[i+1][j].getIcon() == null){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_mitte_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_mitte);
            }
            else if (schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && andereGifs.contains(spielfeldButtons[i+1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j-1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten);
            }
        }
        else if (i-1 < 0){
            if (!schiffGifs.contains(spielfeldButtons[i+1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j+1].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j-1].getIcon())){
                spielfeldButtons[i][j].setIcon(uboot_einzel);
            }
            else if (andereGifs.contains(spielfeldButtons[i][j-1].getIcon()) && schiffGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j-1].getIcon()) && schiffGifs.contains(spielfeldButtons[i][j+1].getIcon()) || spielfeldButtons[i][j+1].getIcon() == null || spielfeldButtons[i][j-1].getIcon() == null){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_mitte_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_mitte);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j-1].getIcon()) && andereGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten);
            }
            else if (schiffGifs.contains(spielfeldButtons[i+1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne);
            }
        }
        else if (j-1 < 0){
            if (!schiffGifs.contains(spielfeldButtons[i+1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                spielfeldButtons[i][j].setIcon(uboot_einzel);
            }
            else if (andereGifs.contains(spielfeldButtons[i-1][j].getIcon()) && schiffGifs.contains(spielfeldButtons[i+1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne);
            }
            else if (schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && schiffGifs.contains(spielfeldButtons[i+1][j].getIcon()) || spielfeldButtons[i-1][j].getIcon() == null || spielfeldButtons[i+1][j].getIcon() == null){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_mitte_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_mitte);
            }
            else if (schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && andereGifs.contains(spielfeldButtons[i+1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne);
            }
        }
        else{
            if (!schiffGifs.contains(spielfeldButtons[i+1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j+1].getIcon()) && !schiffGifs.contains(spielfeldButtons[i][j-1].getIcon())){
                spielfeldButtons[i][j].setIcon(uboot_einzel);
            }
            else if (andereGifs.contains(spielfeldButtons[i-1][j].getIcon()) && schiffGifs.contains(spielfeldButtons[i+1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_vorne);
            }
            else if (schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && schiffGifs.contains(spielfeldButtons[i+1][j].getIcon()) || spielfeldButtons[i-1][j].getIcon() == null || spielfeldButtons[i+1][j].getIcon() == null){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_mitte_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_mitte);
            }
            else if (schiffGifs.contains(spielfeldButtons[i-1][j].getIcon()) && andereGifs.contains(spielfeldButtons[i+1][j].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_vertikal_hinten);
            }
            else if (andereGifs.contains(spielfeldButtons[i][j-1].getIcon()) && schiffGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_vorne);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j-1].getIcon()) && schiffGifs.contains(spielfeldButtons[i][j+1].getIcon()) || spielfeldButtons[i][j+1].getIcon() == null || spielfeldButtons[i][j-1].getIcon() == null){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_mitte_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_mitte);
            }
            else if (schiffGifs.contains(spielfeldButtons[i][j-1].getIcon()) && andereGifs.contains(spielfeldButtons[i][j+1].getIcon())){
                if (kaputt) spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten_feuer);
                else spielfeldButtons[i][j].setIcon(uboot_horizontal_hinten);
            }
        }
    }

    /**
     * Setzt den Text des Statusfeldes, welcher Spieler gewonnen hat.
     *
     * @param gewinner Der Name des Gewinners, der in der Nachricht angezeigt werden soll.
     */
    public void setGewonnen(String gewinner) {
        status.setText(gewinner + " hat gewonnen!");
    }

    /**
     * Eine benutzerdefinierte Border-Klasse für Komponenten, die einen farbigen Rand mit einem bestimmten Radius darstellt.
     */
    private static class AbgeschossenBorder implements Border {

        private final int radius;
        private final Color color;

        /**
         * Konstruktor für die AbgeschossenBorder-Klasse.
         *
         * @param color Die Farbe des Randes.
         * @param radius Der Radius, der den Abstand des Rands von der Komponente definiert.
         */
        private AbgeschossenBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        /**
         * Gibt die Insets (Abstände) des Rands zurück.
         *
         * @param c Die Komponente, für die der Rand definiert ist.
         * @return Ein Insets-Objekt, das den Abstand des Rands von der Komponente angibt.
         */
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        /**
         * Gibt an, ob der Rand undurchsichtig ist.
         *
         * @return true, da der Rand immer undurchsichtig ist.
         */
        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        /**
         * Zeichnet den Rand um die Komponente.
         *
         * @param c Die Komponente, für die der Rand gezeichnet wird.
         * @param g Das Graphics-Objekt, das zum Zeichnen verwendet wird.
         * @param x Die x-Position der Komponente.
         * @param y Die y-Position der Komponente.
         * @param width Die Breite der Komponente.
         * @param height Die Höhe der Komponente.
         */
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.drawRect(x, y, width - 1, height - 1);
        }
    }

    /**
     * Die Menü-Klasse erweitert das Spiel mit einem Menü.
     */
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
            JButton regelButton = new JButton("Hilfe");
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
            JFrame hoverFrame = new JFrame("Hilfe");
            hoverFrame.setSize(600, 600);
            hoverFrame.setLayout(new FlowLayout());
            hoverFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);


            // Create the label for hover text
            JLabel hoverTextLabel = new JLabel("<html>Herzlich Willkommen beim Spiel Schiffe Versenken!<br/><br/><br/><br/>Namen<br/><br/>Um die Namen für Spieler 1 und Spieler 2 zu ändern müssen Sie in die Einstellungen gehen<br/><br/><br/><br/>Regeln<br/><br/>Die Schiffe werden automatisch gesetzt und dürfen nicht aneinander liegen sondern haben<br> immer mindestens ein Feld Abstand<br/><br/>Vorbereitung : Jeder Spieler bekommt zufällig plazierte Schiffe<br/>Das Spielfeld besteht aus einer Größe von 10x10<br/><br/>Spielablauf: Die Spieler können abwechselnd ein Feld auswählen auf das Sie schießen möchten<br/>Trifft ein Spieler ein Feld wo sich ein Schiff befindet darf dieser nochmal feuern<br/><br/>Gewonnen: Der Spieler, der zuerst alle Schiffe des Gegners versenkt, gewinnt das Spiel<br/><br/>Anzahl an Schiffen: 10<br/> 1x5<br/>2x4<br/>3x3<br/>4x2</html>");
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
