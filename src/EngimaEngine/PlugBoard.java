package EngimaEngine;

import java.util.Dictionary;

public interface PlugBoard {
    String returnCharacterPair(String character);
    Dictionary<String, String> importPlugBoardInputToColumns(String input); // if input = null -> return null
}
