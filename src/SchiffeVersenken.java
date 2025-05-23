
/**
 * Die Klasse SchiffeVersenken startet das Spiel.
 *
 * @author Lukas Käuper, Marten Ahmann
 * @version 05.09.2024
 */
public class SchiffeVersenken {

    Controller controller;

    /**
     * Konstruktor der Klasse SchiffeVersenken.
     * Erstellt ein neues Controller-Objekt zur Steuerung des Spiels.
     */
    public SchiffeVersenken(){
        controller = new Controller();
    }

    /**
     * Die Hauptmethode, die das Programm startet.
     */
    public static void main(String[] args) {new SchiffeVersenken();
    }
}
