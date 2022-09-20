package enigmaEngine.interfaces;

import enigmaEngine.MachineCode;
import enigmaEngine.WordsDictionary;
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

    int getABCSize();

    String getABC();

    char activate(char input);

    String processMessage(String input) throws InvalidCharactersException;

    void setSelectedRotors(List<Integer> rotorsIDInorder, List<Character> startingPositions) throws InvalidCharactersException, InvalidRotorException;

    void setStartingCharacters(List<Character> startingCharacters) throws InvalidCharactersException;

    List<Reflector.ReflectorID> getReflectors();

    void setSelectedReflector(Reflector.ReflectorID selectedReflectorID) throws InvalidReflectorException;

    void setPlugBoard(List<Pair<Character,Character>> plugBoard) throws InvalidPlugBoardException;

    void reset();

    EngineDTO getEngineDTO();

    MachineCode getMachineCode();

    void randomSelectedComponents();

    void setEngineConfiguration(MachineCode machineCode) throws InvalidCharactersException, InvalidRotorException, InvalidReflectorException, InvalidPlugBoardException;

    EnigmaEngine cloneMachine();

    WordsDictionary getWordsDictionary();
    void setWordsDictionary(WordsDictionary wordsDictionary);
}