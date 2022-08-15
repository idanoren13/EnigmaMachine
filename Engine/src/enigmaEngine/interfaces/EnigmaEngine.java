package enigmaEngine.interfaces;

import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.InvalidStartingCharacters;
import immutables.engine.EngineDTO;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public interface EnigmaEngine {

    HashMap<Integer, Rotor> getRotors();

    String getMachineABC();
    char activate(char input);

    String encryptDecrypt(String input);

    void setSelectedRotors(List<Integer> rotorsIDInorder, List<Character> startingPositions) throws InvalidStartingCharacters, InvalidRotorException;
    void setStartingCharacters(List<Character> startingCharacters) throws InvalidStartingCharacters;

    void setSelectedReflector(Reflector.ReflectorID selectedReflectorID) throws InvalidReflectorException;

    void setPlugBoard(List<Pair<Character,Character>> plugBoard) throws InvalidPlugBoardException;

    void reset();

    EngineDTO getEngineDTO();

    PlugBoard getPlugBoard();
}
