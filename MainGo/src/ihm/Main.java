package ihm;


import go.Go;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        lancer();
    }

/*    public static void lancer() {
        Go g = new Go(19);
        Scanner sc = new Scanner(System.in);
        String entree;
        int jeu = 1;
        //Pour les commandes prenants un espace
        while (jeu != 0) {
            entree = sc.nextLine();
            if (!entree.isEmpty())
                jeu = g.jouerGo(entree);
        }
    }*/
    public static void lancer() {
        Go g = new Go(6);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Veuillez sélectionner le mode de jeu : ");
            System.out.println("1: random vs random ");
            System.out.println("2: player vs player ");

            String gameMode = sc.nextLine().trim();

            switch (gameMode) {
                case "1":
                    g.getcGo().playRandomGame();
                    break;
                case "2":
                    int jeu = 1;
                    while (jeu != 0) {
                        String entree = sc.nextLine();
                        if (!entree.isEmpty())
                            jeu = g.jouerGo(entree);
                    }
                    break;
                default:
                    System.out.println("Option non reconnue. Veuillez réessayer.");
                    continue;
            }
            break;
        }
        sc.close();

    }

}