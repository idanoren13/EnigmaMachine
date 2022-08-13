package enigmaEngine.interfaces;

import Resources.PairClass;

import java.util.List;

public interface PlugBoard {
    char returnCharacterPair(char character);
    void UpdatePairs(List<PairClass<Character, Character>> pairList);
    void addPair(char a, char b);
//    HashMap<Character, Character> generateInputIntoPairs(List<Pair<Character,Character>> pairList); // if input = null -> return null


}
