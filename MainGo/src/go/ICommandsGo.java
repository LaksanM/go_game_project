package go;

import java.util.ArrayList;

public interface ICommandsGo {

    String showBoard();

    int getNbInString(String s);

    boolean isNumeric(String s, Character c);

    String boardsize(ArrayList<String> motSplit);

    int play(String s, String s1);

    void isCapture(String pos);
}
