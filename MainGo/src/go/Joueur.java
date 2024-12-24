package go;

public class Joueur {
    private int stones;
    private final String name;
    private final String c;

    public String getScore() {
        return this.name + " (" + this.c + ") has captured " + this.stones + " stones";
    }

    public Joueur(String name) {
        stones = 0;
        this.name = name;
        c = name.equals("BLACK") ? "X" : name.equals("WHITE") ? "O" : "";
    }

    public void resetScore() {
        stones = 0;
    }

    public void addScore(int number){
        stones += number;
    }
}
