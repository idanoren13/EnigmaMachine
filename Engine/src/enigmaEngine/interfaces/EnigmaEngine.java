package enigmaEngine.interfaces;

import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import immutables.engine.EngineDTO;
import immutables.engine.EngineSelectedPartsDTO;
import immutables.engine.CodeGeneratorDTO;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public interface EnigmaEngine {

    HashMap<Integer, Rotor> getRotors();
    char activate(char input);
    String processMessage(String input) throws InvalidCharactersException;
    void setSelectedRotors(List<Integer> rotorsIDInorder, List<Character> startingPositions) throws InvalidCharactersException, InvalidRotorException;
    void setStartingCharacters(List<Character> startingCharacters) throws InvalidCharactersException;

    void setSelectedReflector(Reflector.ReflectorID selectedReflectorID) throws InvalidReflectorException;

    void setPlugBoard(List<Pair<Character,Character>> plugBoard) throws InvalidPlugBoardException;

    void reset();

    EngineDTO getEngineDTO();

    EngineSelectedPartsDTO getSelectedParts();

    CodeGeneratorDTO getRandomGeneratorDTO(EngineSelectedPartsDTO partsForRandom) throws InvalidPlugBoardException, InvalidRotorException, InvalidReflectorException;

    void setEnigmaCode(CodeGeneratorDTO partsForRandom) throws InvalidCharactersException, InvalidRotorException, InvalidReflectorException, InvalidPlugBoardException;
}