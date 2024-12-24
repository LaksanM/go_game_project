package go;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static go.Go.*;

public class CommandsGo implements ICommandsGo {
    private Go g;
    private static final ArrayList<String> possibleCommands = new ArrayList<>(List.of("boardsize", "showboard", "quit", "play", "clear_board"));
    // liste des commandes jouables
    private static final String X = "X";
    //Joueur X pour l'affichage
    private static final String O = "O";
    //Joueur O pour l'affichage
    private static final String def = ".";
    //Affichage par défaut si pas de joueur
    private static String ancienJoueur = "white";
    // le joueur precedent

    protected static String currentJoueur = "";
    // le joueur acutelle
    private static final String test = "A2";

    public static String getAncienJoueur() {
        return ancienJoueur;
    }

    public static void setAncienJoueur(String ancienJoueur) {
        CommandsGo.ancienJoueur = ancienJoueur;
    }

    //Constructeur
    public CommandsGo(Go g) {
        this.g = g;
    }

    protected void resetGo() {
        g.getPlateauGo().clear();
        g.getJ1().resetScore();
        g.getJ2().resetScore();
        CommandsGo.setAncienJoueur("white");
    }

    //getter possibleCommands
    public static ArrayList<String> getPossibleCommands() {
        return possibleCommands;
    }

    //fonction boardsize prend en parametre la ligne de commande rentrée avec boardsize
    public String boardsize(ArrayList<String> CommandLine) {
        System.out.println(CommandLine);
        StringBuilder sb = new StringBuilder();
        boolean nb1 = CommandLine.get(1).equals("boardsize") && CommandLine.size() == MaxLenCommand - 1;
        //si la ligne de commande contient boardisize à la bonne position,et le bon nombre de paramètres de commandes


        if (nb1) {
            int nb = getNbInString(CommandLine.get(2));
            //récupere les premiers chiffres qui sont en premieres positions après boardsize
            if (nb < 1 || nb > alphabetGo.length()) {
                sb.append("?");
                sb.append(CommandLine.get(0));
                sb.append(" unacceptable size").append(System.lineSeparator()).append(System.lineSeparator());
            } else {
                //alors ok, on crée un plateauGo avec la bonne taille
                g = new Go(Integer.parseInt(String.valueOf(nb)));
                sb.append("=");
                sb.append(CommandLine.get(0));
                sb.append(" ").append(System.lineSeparator()).append(System.lineSeparator());
                this.resetGo();

            }
        } else {
            //Alors pas le bon nombre de paramètres quand il a écrit le ligne de commande avec boardsize
            sb.append("?");
            sb.append(CommandLine.get(0));
            sb.append(" boardsize not an integer").append(System.lineSeparator()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    //Fonction qui renvoie le premier nombre qui est dans les premiers index du string en parametre
    public int getNbInString(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (isNumeric(null, str.charAt(i)))
                sb.append(str.charAt(i));
            else
                break;
        }
        if (sb.isEmpty())
            //si pas de int au début, alors on renvoie -1
            sb.append("-1");
        return Integer.parseInt(sb.toString());
    }

    //Boolen qui renvoie false si le joueur precedent est identique au joueur passé en parametre, sinon true
    private boolean JoueurDifferentAvant(String nomJoueur) {
        if (!ancienJoueur.equals(nomJoueur)) {
            ancienJoueur = nomJoueur;
            return true;
        } else {
            return false;
        }
    }

    //Commande play qui prend en parametre le nom du joueur, et sa commande de placement
    public int play(String j, String plateau) {

        //si le nom du joueur n'est pas black ou white
        if (!j.equals("black") && !j.equals("white"))
            return 0;
        String JoueurActuel = j;
        currentJoueur= j.equals("black") ? "white" : "black";
        j = j.equals("black") ? "X" : "O";

        int nb = getNbInString(plateau.substring(1));
        //nb vaut la position en chiffre
        if (isNumeric(null, plateau.charAt(0)) || nb > g.getTaillePlateau() || nb <= 0)
            //si l'utilisateur rentre un mauvais placement
            return 0;
        if (alphabetGo.indexOf(plateau.charAt(0)) != -1) {
            //si la lettre correspond à une lettre dans l'alphabetGo
            String elm = plateau.charAt(0) + String.valueOf(nb);


            if (!g.getPlateauGo().containsKey(elm) && !getAncienJoueur().equals(JoueurActuel)) {
                setAncienJoueur(JoueurActuel);
                if (this.isSucide(elm)) {
                    return 3;
                }
                g.getPlateauGo().put(elm, j);
                //on place
                for (String voisin : getVoisin(elm)) {
                    if (!(Objects.equals(g.getPlateauGo().get(voisin), g.getPlateauGo().get(elm)))){
                            this.isCapture(voisin);
                    }
                }
                System.out.println("Color: "+JoueurActuel+" | Pos: "+elm);
                System.out.println(this.showBoard());
                return 1;
            } else {
                setAncienJoueur(JoueurActuel);
                return 2;
            }
        }


        return 0;
    }


    //Fonction qui renvoie true si le String ou le Character passé en parametre en une valeur chiffrée
    public boolean isNumeric(String strNum, Character c) {
        if (c != null)
            return Character.isDigit(c);
        for (int i = 0; i < strNum.length(); i++) {
            if (!Character.isDigit(strNum.charAt(i)))
                return false;
        }
        return true;
    }

    //Commande showboard
    @Override
    public String showBoard() {
        StringBuilder sb = new StringBuilder();
        int size = g.getTaillePlateau();
        int taillePlusGrand = String.valueOf(size).length();
        // Length String de la taille du plateau
        if (size < 10)
            sb.append(" ");
        sb.append(" ".repeat(taillePlusGrand));
        for (int i = 1; i <= size; i++)
            sb.append(" ").append(g.getNumToLet().get(i));
        sb.append(System.lineSeparator());
        for (int i = size; i > 0; i--) {
            sb.append(" ".repeat(taillePlusGrand - String.valueOf(i).length()));
            if (size < 10)
                sb.append(" ");
            sb.append(i).append(" ");
            for (int j = 1; j <= size; j++) {
                String elm = g.getNumToLet().get(j) + String.valueOf(i);
                if (g.getPlateauGo().containsKey(elm)) {
                    switch (g.getPlateauGo().get(elm)) {
                        case X:
                            sb.append(X + " ");
                            break;
                        case O:
                            sb.append(O + " ");
                            break;
                        case def:
                            sb.append(def+" ");
                    }
                } else
                    sb.append(def + " ");
            }
            // sb.append(" ".repeat(taillePlusGrand - String.valueOf(i).length()));
            sb.append(i);
            if (i == 2) {
                sb.append("\t\t").append(g.getJ2().getScore());
            } else if (i == 1)
                sb.append("\t\t").append(g.getJ1().getScore());
            sb.append(System.lineSeparator());
        }
        if (g.getTaillePlateau() < 10)
            sb.append(" ");
        sb.append(" ".repeat(taillePlusGrand));
        for (int i = 1; i <= g.getTaillePlateau(); i++)
            sb.append(" ").append(g.getNumToLet().get(i));
        return sb.toString();
    }



    public boolean isSucide(String pos){

        if (getliberties(pos) == 0 ) {
            return true;
        }
        return false;
    }
    @Override
    public void isCapture(String pos){
        if (getliberties(pos) == 0 && !pos.contains("0")) {
            if (g.getPlateauGo().containsKey(pos)) {

                List<String> chaine = g.getChaine().get(pos);
                int cpt = 0;
                if(chaine != null) {

                    for (String c : chaine) {
                        if (pos.equals(c)) {
                            g.getChaine().remove(pos, c);
                            continue;
                        }
                        g.getPlateauGo().remove(c);
                        g.getChaine().remove(pos, c);

                        cpt++;
                    }
                }
                g.getPlateauGo().remove(pos);


                if (Objects.equals(currentJoueur, "black")) {
                    g.getJ1().addScore(1 + cpt);
                } else {
                    g.getJ2().addScore(1 + cpt);
                }
            }
        }
    }


    public List<String> getVoisin(String pos) {
        char lettre = pos.charAt(0);
        int chiffre = pos.charAt(1) - '0';

        int indexLettre = alphabetGo.indexOf(lettre);
        char lettreG = getLettreparindex(alphabetGo, indexLettre - 1);
        char lettreD = getLettreparindex(alphabetGo, indexLettre + 1);
        int chiffreB = getNombreValid(chiffre - 1);
        int chiffreH = getNombreValid(chiffre + 1);

        List<String> voisins = new ArrayList<>();

        // Ajoutez les voisins à la liste s'ils sont valides
        if (indexLettre - 1 >= 0) {
            voisins.add(String.valueOf(lettreG) + chiffre);
        }
        if (indexLettre + 1 < alphabetGo.length()) {
            voisins.add(String.valueOf(lettreD) + chiffre);
        }
        if (chiffre - 1 > 0) {
            voisins.add(String.valueOf(lettre) + chiffreB);
        }
        if (chiffre + 1 <= g.getTaillePlateau()) {
            voisins.add(String.valueOf(lettre) + chiffreH);
        }

        return voisins;
    }




    public int getliberties(String pos) {
        List<String> l = new ArrayList<>();

        int libertes = 0;

        for (String voisin : getVoisin(pos)) {

            if (!voisin.contains("0")) {
                if (!g.getPlateauGo().containsKey(voisin)) {
                    l.add(pos);
                    libertes++;
                } else if (g.getPlateauGo().get(voisin).equals(g.getPlateauGo().get(pos))) {
                    l.add(voisin);
                    libertes += getliberties(voisin, l);
                }
            }
        }
        if (!l.isEmpty()) {
            g.getChaine().put(pos, l);
        }
        return libertes;
    }

    public int getliberties(String pos, List<String> l) {
        int libertes = 0;
        for (String c : getVoisin(pos)) {
            if (l.contains(pos)) {
                if (!c.contains("0")) {
                    if (!g.getPlateauGo().containsKey(c)) {
                        libertes++;
                    } else if (g.getPlateauGo().get(c).equals(g.getPlateauGo().get(pos)) && !l.contains(c)) {
                        l.add(c);
                        libertes += getliberties(c,l);
                    }
                }
            }
        }
        return libertes;
    }

    // retour la lettre ou '0'
    private char getLettreparindex(String alphabet, int index) {
        return (index >= 0 && index < alphabet.length()) ? alphabet.charAt(index) : '0';
    }

    // retour un nombre valid ou 0
    private int getNombreValid(int number) {
        return (number >= 0 && number < g.getTaillePlateau()) ? number : 0;
    }


    public void playRandomGame() {
        Random rand = new Random();
        int size = g.getTaillePlateau();
        boolean isBoardFull = false;
        int x, y;
        String position;

        // Keep track of current player
        currentJoueur = "black";
        do {

            x = rand.nextInt(size) + 1;
            y = rand.nextInt(size) + 1;

            position = g.getNumToLet().get(x) + Integer.toString(y);

            if (!g.getPlateauGo().containsKey(position)) {
                    this.play(currentJoueur,position);

                if (g.getPlateauGo().size() == size * size) {
                    isBoardFull = true;
                }
            }
        } while (!isBoardFull);
    }

}