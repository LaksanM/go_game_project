package go;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Go {
    private final Map<Integer, Character> NumToLet;
    //Map pour l'alphabet et le numero associé à la lettre
    protected static final String alphabetGo = "ABCDEFGHJKLMNOPQRST";
    private final Map<String, String> PlateauGo;

    private final Map<String, List<String>> Chaine;

    // La key est l'emplacement et la value est le joueur black ou white
    private final int taillePlateau;
    //taille du plateau taillePlateau * taillePlateau
    protected static final int MaxLenCommand = 4;
    //Len max pour jouer une commande
    private final Joueur j1;
    private final Joueur j2;

    private CommandsGo cGo;

    public Go(int taille) {
        taillePlateau = taille;
        NumToLet = new HashMap<>();
        ConstruireAlphabet();
        PlateauGo = new HashMap<>();
        Chaine = new HashMap<>();
        j1 = new Joueur("BLACK");
        j2 = new Joueur("WHITE");
        cGo = new CommandsGo(this);
    }

    public CommandsGo getcGo() {
        return cGo;
    }



    public int getTaillePlateau() {
        return taillePlateau;
    }


    public Map<String, String> getPlateauGo() {
        return PlateauGo;
    }

    public Map<String, List<String>> getChaine() {
        return Chaine;
    }

    private void ConstruireAlphabet() {
        NumToLet.clear();
        for (int i = 1; i <= taillePlateau; i++) {
            NumToLet.put(i, alphabetGo.charAt(i - 1));
        }
    }

    public Map<Integer, Character> getNumToLet() {
        return NumToLet;
    }

    public Joueur getJ1() {
        return j1;
    }

    public Joueur getJ2() {
        return j2;
    }

    public int jouerGo(String entree) {

        String[] lineCommand = entree.split("\\s+");
        //metre dans un array
        ArrayList<String> motSplit = new ArrayList<>();
        for (int i = 0; i < lineCommand.length; i++) {
            if (i == 0 && CommandsGo.getPossibleCommands().contains(lineCommand[i])) {
                //si la premiere commande n'a pas de chiffres, et uniquement la nom de la commande
                motSplit.add("");
                motSplit.add(lineCommand[i]);
            } else if (i == 0 && cGo.isNumeric(lineCommand[0], null))
                //commence par seulement un chiffre!:
                motSplit.add(lineCommand[i]);
            else if (i == 0 && cGo.getNbInString(lineCommand[i]) != -1 && !cGo.isNumeric(lineCommand[i].substring(1), null)) {
                //séparer la premiere commande possede id command  et nom de la commande collés
                motSplit.add(String.valueOf(cGo.getNbInString(lineCommand[i])));
                motSplit.add(lineCommand[i].substring(String.valueOf(cGo.getNbInString(lineCommand[i])).length()));
            } else if (i == 0 && !cGo.isNumeric(lineCommand[i], null)) {
                //si la premiere commande n'est pas un chiffre
                //alors on met case vide et après on met le nom de la commande
                motSplit.add("");
                motSplit.add(lineCommand[i]);
            } else
                motSplit.add(lineCommand[i]);
        }
        //Maintenant, motsplit contient la ligne de commande

        //entree prend le nom de la commande -> boardsize ou play ou quit ou showboard
        if (motSplit.size() <= 1)
            return 1;
        switch (motSplit.get(1)) {
            case "showboard":
                if (taillePlateau != 0) {
                    System.out.print(goodCommand(motSplit));
                    System.out.println(cGo.showBoard() + System.lineSeparator());
                }
                break;
            case "boardsize":
                System.out.println(cGo.boardsize(motSplit));
                break;
            case "test":
                cGo.getliberties("A2");
                break;
            case "play":
                //Si pas d'id de commande
                int play;
                if (!(motSplit.size() <= Go.MaxLenCommand))
                    play = 0;
                else
                    play = cGo.play(motSplit.get(2), motSplit.get(3).toUpperCase());
                switch (play) {
                    case 0:
                        System.out.println("? invalid color or coordinate" + System.lineSeparator());
                        break;
                    case 1:
                        System.out.println(goodCommand(motSplit));
                        break;
                    case 2:
                        System.out.println("? illegal move" + System.lineSeparator());
                        break;
                    case 3:
                        System.out.println("? illegal move sucide" + System.lineSeparator());
                        break;
                }
                break;
            case "clear_board":
                cGo.resetGo();
                System.out.println(goodCommand(motSplit));
                break;
            case "quit":
                System.out.println(goodCommand(motSplit));
                return 0;
            case "":
                break;
            default:
                System.out.print("?");
                System.out.print(motSplit.get(0));
                System.out.println(" unknown command" + System.lineSeparator());
                break;
        }
        return 1;
    }

    private String goodCommand(ArrayList<String> CommandLine) {
        StringBuilder sb = new StringBuilder();
        sb.append("=");
        sb.append(CommandLine.get(0));
        sb.append(" ");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

}

