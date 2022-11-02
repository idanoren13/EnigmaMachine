package enigmaEngine;

import immutables.engine.ReflectorID;
import javafx.util.Pair;

import java.util.List;

public class MachineCodeDTO {
    private final List<Integer> rotorsIDInorder;
    private final List<Character> startingPositions;
    private final ReflectorID selectedReflectorID;
    private final List<Pair<Character,Character>> plugBoard;

    public MachineCodeDTO(List<Integer> rotorsIDInorder, List<Character> startingPositions, ReflectorID selectedReflectorID, List<Pair<Character,Character>> plugBoard) {
        this.rotorsIDInorder = rotorsIDInorder;
        this.startingPositions = startingPositions;
        this.selectedReflectorID = selectedReflectorID;
        this.plugBoard = plugBoard;
    }

    public List<Character> getStartingPositions() {
        return startingPositions;
    }

    public List<Integer> getRotorsIDInorder() {
        return rotorsIDInorder;
    }

    public ReflectorID getSelectedReflectorID() {
        return selectedReflectorID;
    }

    public List<Pair<Character, Character>> getPlugBoard() {
        return plugBoard;
    }
}
