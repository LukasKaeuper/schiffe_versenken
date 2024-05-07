public class SchiffeVersenken {

    Controller controller;

    public SchiffeVersenken(){
        controller = new Controller();
    }
    public static void main(String[] args) {
        Spiel spiel = new Spiel();
        spiel.starten();
    }
}
