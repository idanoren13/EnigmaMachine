package enigmaEngine.impl;

import enigmaEngine.interfaces.PlugBoard;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class PlugBoardImpl implements PlugBoard, Serializable {
    private final HashMap<Character, Character> abcPairs;

    public PlugBoardImpl() {
        this.abcPairs = new HashMap<>();
    }

    public PlugBoardImpl(List<Pair<Character, Character>> pairList) {
        this.abcPairs = generateInputIntoPairs(pairList);
    }


    private HashMap<Character, Character> generateInputIntoPairs(List<Pair<Character, Character>> pairList) {
        HashMap<Character, Character> abcPairs = new HashMap<>();
        for (Pair<Character, Character> pair : pairList) {
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
    public void addPair(char a, char b) {
        this.abcPairs.put(a, b);
        this.abcPairs.put(b, a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public HashMap<Character, Character> getPairs() {
        return (HashMap<Character, Character>)this.abcPairs.clone();
    }

    @Override
    public boolean containsPair(Pair<Character, Character> pair) {
        return this.abcPairs.containsKey(pair.getKey()) && this.abcPairs.containsKey(pair.getValue());
    }
}