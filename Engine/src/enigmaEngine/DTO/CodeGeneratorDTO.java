package enigmaEngine.DTO;

import enigmaEngine.InitCode;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.interfaces.Reflector;
import javafx.util.Pair;

import java.util.List;
import java.util.Random;

public class CodeGeneratorDTO {
    private final List<Integer> selectedRotorsList;
    private final List<Character> allStartingPositionsList;
    private final Reflector.ReflectorID reflectorID;
    List<Pair<Character, Character>> randomPlugBoardPairsList;
    public CodeGeneratorDTO(EngineSelectedPartsDTO partsForRandom) throws InvalidRotorException, InvalidReflectorException, InvalidPlugBoardException {
        InitCode initCode = new InitCode();
        int randomNumberOfRotors = new Random().nextInt(partsForRandom.getNumberOfRotors() - 1) + 2;
        String selectedRotors = initCode.pickRandomRotors(randomNumberOfRotors, partsForRandom.getNumberOfRotors());
        String allStartingPositions = initCode.pickRandomStartingCharacters(partsForRandom.getABC(), randomNumberOfRotors);
        String reflectorStringID = initCode.pickRandomReflectorID(partsForRandom.getNumberOfReflectors());
        String randomPlugBoard = initCode.pickRandomPlugBoard(partsForRandom.getABC());

        this.selectedRotorsList = initCode.createSelectedRotorsList(selectedRotors);
        this.allStartingPositionsList = initCode.createStartingCharactersList(allStartingPositions);
        this.reflectorID = initCode.getReflectorID(reflectorStringID);
        this.randomPlugBoardPairsList = initCode.createPlugBoard(randomPlugBoard);
    }

    public List<Integer> getSelectedRotorsList() {
        return this.selectedRotorsList;
    }

    public List<Character> getAllStartingPositionsList() {
        return this.allStartingPositionsList;
    }

    public Reflector.ReflectorID getReflectorID() {
        return this.reflectorID;
    }

    public List<Pair<Character, Character>> getRandomPlugBoardPairsList() {
        return this.randomPlugBoardPairsList;
    }
}
