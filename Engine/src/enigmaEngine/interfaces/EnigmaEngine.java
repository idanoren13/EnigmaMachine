package enigmaEngine.interfaces;

import battlefield.Battlefield;
import enigmaEngine.MachineCode;
import enigmaEngine.WordsDictionary;
import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import immutables.BattlefieldDTO;
import immutables.EngineDTO;
import immutables.ReflectorID;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public interface EnigmaEngine extends Serializable {
    HashMap<Integer, Rotor> getRotors();

    int getABCSize();

    String getABC();

    char activate(char input);

    String processMessage(String input) throws InvalidCharactersException;

    void setSelectedRotors(List<Integer> rotorsIDInorder, List<Character> startingPositions) throws InvalidCharactersException, InvalidRotorException;

    void setStartingCharacters(List<Character> startingCharacters) throws InvalidCharactersException;

    List<ReflectorID> getReflectors();

    void setSelectedReflector(ReflectorID selectedReflectorID) throws InvalidReflectorException;

    void setPlugBoard(List<Pair<Character,Character>> plugBoard) throws InvalidPlugBoardException;

    void reset();

    EngineDTO getEngineDTO();

    MachineCode getMachineCode();

    void randomSelectedComponents();

    void setEngineConfiguration(MachineCode machineCode) throws InvalidCharactersException, InvalidRotorException, InvalidReflectorException, InvalidPlugBoardException;

    EnigmaEngine cloneMachine();

    WordsDictionary getWordsDictionary();
    void setWordsDictionary(WordsDictionary wordsDictionary);

    EnigmaEngine deepClone();
    public BattlefieldDTO getBattlefieldDTO();

    Battlefield getBattlefield();
}