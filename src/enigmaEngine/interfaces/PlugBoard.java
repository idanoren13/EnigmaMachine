package enigmaEngine.interfaces;

import java.util.HashMap;

public interface PlugBoard {
    char returnCharacterPair(char character);
    HashMap<Character, Character> generateInputIntoPairs(String input); // if input = null -> return null
}
