import java.util.Scanner;

public class Spiel {

    public void starten() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welche groesse soll das Spielfeld haben?");
        int spielfeldGroesse = Integer.parseInt(scanner.nextLine());
        Spielfeld spielfeld = new Spielfeld(spielfeldGroesse);

        System.out.println("Spieler 1, bitte geben Sie Ihren Namen ein:");
        String nameSpieler1 = scanner.nextLine();
        Spieler spieler1 = new Spieler(nameSpieler1);

        System.out.println("Spieler 2, bitte geben Sie Ihren Namen ein:");
        String nameSpieler2 = scanner.nextLine();
        Spieler spieler2 = new Spieler(nameSpieler2);

        System.out.println("Spieler 1: " + spieler1.getName());
        System.out.println("Spieler 2: " + spieler2.getName());
        System.out.println("Das Spielfeld hat eine groesse von: " + spielfeld.getGroesse());
        scanner.close();
    }
}
