package enigmaEngine.impl;

import java.util.HashMap;

public class PlugBoard implements enigmaEngine.PlugBoard {
    private final HashMap<Character, Character> abcPairs;

    public PlugBoard(String userInput) {
        this.abcPairs = generateInputIntoPairs(userInput);
    }

    @Override
    public HashMap<Character, Character> generateInputIntoPairs(String input) {
        String[] allPairs = input.split(",");
        HashMap<Character, Character> abcPairs = new HashMap<>();
        for (String pair : allPairs) {
            abcPairs.put(pair.charAt(0), pair.charAt(2));   // TO DO - we assume that the pairs would always be "x,y", maybe they are not.
                                                            // (for instance "x, y"). TO DO - handle this
            abcPairs.put(pair.charAt(2), pair.charAt(0));   // TO DO - we assume that the pairs would always be "x,y", maybe they are not.
            // (for instance "x, y"). TO DO - handle this
        }
        return abcPairs;
    }
    public HashMap<Character, Character> getAbcPairs() {
        return abcPairs;
    }

    @Override
    public char returnCharacterPair(char input) {
        return getAbcPairs().get(input);
    }
}
