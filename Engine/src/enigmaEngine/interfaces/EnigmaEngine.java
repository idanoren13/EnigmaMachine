package enigmaEngine.interfaces;

import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.InvalidStartingCharacters;

import java.util.HashMap;
import java.util.List;

public interface EnigmaEngine {

    HashMap<Integer, Rotor> getRotors();
    String getMachineABC();
    char activate(char input);
    void setSelectedRotors(List<Integer> rotorsIDInorder, List<Character> startingPositions) throws InvalidStartingCharacters, InvalidRotorException;
    void setStartingCharacters(List<Character> startingCharacters) throws InvalidStartingCharacters;
    void reset();

}
