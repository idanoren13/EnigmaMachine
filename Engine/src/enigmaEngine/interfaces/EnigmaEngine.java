package enigmaEngine.interfaces;

import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import immutables.engine.EngineDTO;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public interface EnigmaEngine {

    HashMap<Integer, Rotor> getRotors();
    char activate(char input, boolean bool);

    String processMessage(String input, boolean bool) throws InvalidCharactersException;

    void setSelectedRotors(List<Integer> rotorsIDInorder, List<Character> startingPositions) throws InvalidCharactersException, InvalidRotorException;
    void setStartingCharacters(List<Character> startingCharacters) throws InvalidCharactersException;

    List<Reflector.ReflectorID> getReflectors();

    void setSelectedReflector(Reflector.ReflectorID selectedReflectorID) throws InvalidReflectorException;

    void setPlugBoard(List<Pair<Character,Character>> plugBoard) throws InvalidPlugBoardException;

    void reset();

    EngineDTO getEngineDTO();

    void randomSelectedComponents();
}