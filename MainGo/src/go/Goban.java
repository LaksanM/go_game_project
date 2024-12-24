package go;

public class Goban {
    Go go;

    public Goban(int taille, String positions) {
        go = new Go(taille);
        String[] mots = positions.split(" ");
        String j = "black";
        // Parcourir chaque mot
        for (String mot : mots) {
            // Vérifier si le mot a une longueur de 2 caractères
            if (mot.length() == 2) {
                go.getcGo().play(j, mot);
                j = j.equals("black") ? "white" : "black";
            }
        }
    }

    public int getliberties(String pos) {
       return  go.getcGo().getliberties(pos);
    }

    public void play(String j, String pos) {
        go.getcGo().play(j, pos);
    }
    public String showboard(){
        return go.getcGo().showBoard();
    }
}


