package enigmaEngine.impl;

import javafx.util.Pair;
import jdk.internal.util.xml.impl.Pair;

import java.util.HashMap;
import java.util.List;

public class PlugBoard implements enigmaEngine.interfaces.PlugBoard {
    private final HashMap<Character, Character> abcPairs;

    public PlugBoard() {
        this.abcPairs = new HashMap<Character, Character>();
    }

    public PlugBoard(List<Pair<Character,Character>> pairList) {
        this.abcPairs = generateInputIntoPairs(pairList);
    }


    private HashMap<Character, Character> generateInputIntoPairs(List<Pair<Character,Character>> pairList) {
//        String[] allPairs = pairList.split(",");
//        HashMap<Character, Character> abcPairs = new HashMap<>();
//        for (String pair : allPairs) {
//            abcPairs.put(pair.charAt(0), pair.charAt(1));   // TO DO - we assume that the pairs would always be "x,y", maybe they are not.
//            // (for instance "x, y"). TO DO - handle this
//            abcPairs.put(pair.charAt(1), pair.charAt(0));   // TO DO - we assume that the pairs would always be "x,y", maybe they are not.
//            // (for instance "x, y"). TO DO - handle this
//        }
//        return abcPairs;

        HashMap<Character, Character> abcPairs = new HashMap<>();
        for (Pair<Character,Character> pair : pairList) {
            abcPairs.put(pair.getKey(), pair.getValue());
            abcPairs.put(pair.getValue(), pair.getKey());
        }
        return abcPairs;
    }

    @Override
    public char returnCharacterPair(char input) {
        char res = input;

        if (abcPairs.containsKey(input))
            res = abcPairs.get(input);

        return res;
    }

    @Override
    public void UpdatePairs(List<Pair<Character, Character>> pairList) {
        this.abcPairs.clear();
        this.abcPairs.putAll(generateInputIntoPairs(pairList));
    }

    @Override
    public void addPair(char a, char b) {
        this.abcPairs.put(a, b);
        this.abcPairs.put(b, a);
    }
}
