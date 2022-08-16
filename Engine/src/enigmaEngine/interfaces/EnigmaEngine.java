package enigmaEngine.interfaces;

import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.InvalidCharactersException;
import immutables.engine.EngineDTO;
import immutables.engine.EngineDTOSelectedParts;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public interface EnigmaEngine {

    HashMap<Integer, Rotor> getRotors();

    String getMachineABC();
    char activate(char input);

    String processMessage(String input) throws InvalidCharactersException;

    void setSelectedRotors(List<Integer> rotorsIDInorder, List<Character> startingPositions) throws InvalidCharactersException, InvalidRotorException;
    void setStartingCharacters(List<Character> startingCharacters) throws InvalidCharactersException;

    void setSelectedReflector(Reflector.ReflectorID selectedReflectorID) throws InvalidReflectorException;

    void setPlugBoard(List<Pair<Character,Character>> plugBoard) throws InvalidPlugBoardException;

    void reset();

    EngineDTO getEngineDTO();

    EngineDTOSelectedParts getSelectedParts();

    PlugBoard getPlugBoard();
}
