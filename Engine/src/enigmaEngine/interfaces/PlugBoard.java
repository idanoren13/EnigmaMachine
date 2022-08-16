package enigmaEngine.interfaces;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public interface PlugBoard {
    char returnCharacterPair(char character);
    void UpdatePairs(List<Pair<Character, Character>> pairList);
    void addPair(char a, char b);
    HashMap<Character, Character> getPairs();
//    HashMap<Character, Character> generateInputIntoPairs(List<Pair<Character,Character>> pairList); // if input = null -> return null
    boolean containsPair(Pair<Character, Character> pair);

}