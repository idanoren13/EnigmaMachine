package enigmaEngine.interfaces;

import javafx.util.Pair;

import java.util.HashMap;

public interface PlugBoard {
    char returnCharacterPair(char character);
    void addPair(char a, char b);
    HashMap<Character, Character> getPairs();
    boolean containsPair(Pair<Character, Character> pair);

}