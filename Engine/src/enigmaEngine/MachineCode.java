package enigmaEngine;

import immutables.ReflectorID;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class MachineCode implements Serializable {
    private final List<Integer> rotorsIDInorder;
    private final List<Character> startingPositions;
    private final ReflectorID selectedReflectorID;
    private final List<Pair<Character, Character>> plugBoard;
    private final int ABCSize;
    private final String ABC;

    public MachineCode(List<Integer> rotorsIDInorder, List<Character> startingPositions, ReflectorID selectedReflectorID, List<Pair<Character, Character>> plugBoard, String abc) {
        this.rotorsIDInorder = rotorsIDInorder;
        this.startingPositions = startingPositions;
        this.selectedReflectorID = selectedReflectorID;
        this.plugBoard = plugBoard;
        ABC = abc;
        ABCSize = abc.length();
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

    public void increment() {
        int i = startingPositions.size() - 1;
        while (i >= 0) {
            int index = ABC.indexOf(startingPositions.get(i));
            if (index == ABCSize - 1) {
                startingPositions.set(i, ABC.charAt(0));
                i--;
            } else {
                startingPositions.set(i, ABC.charAt(index + 1));
                break;
            }
        }
    }

    public MachineCode clone() {
        List<Integer> rotorsIDInorderClone = rotorsIDInorder.stream().map(i -> i).collect(Collectors.toList());
        List<Character> startingPositionsClone = startingPositions.stream().map(i -> i).collect(Collectors.toList());
        List<Pair<Character, Character>> plugBoardClone = plugBoard.stream().map(i -> new Pair<>(i.getKey(), i.getValue())).collect(Collectors.toList());
        return new MachineCode(rotorsIDInorderClone, startingPositionsClone, selectedReflectorID, plugBoardClone, ABC);
    }

    @Override
    public String toString() {
        return "rotorsIDInorder=" + rotorsIDInorder +
                ", startingPositions=" + startingPositions +
                ", selectedReflectorID=" + selectedReflectorID;
    }
}