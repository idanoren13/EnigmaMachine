package enigmaEngine;

import enigmaEngine.interfaces.Reflector;
import javafx.util.Pair;

import java.util.List;

public class MachineCode {
    private final List<Integer> rotorsIDInorder;
    private final List<Character> startingPositions;
    private final Reflector.ReflectorID selectedReflectorID;
    private final List<Pair<Character, Character>> plugBoard;
    private final int ABCSize;
    private final String ABC;

    public MachineCode(List<Integer> rotorsIDInorder, List<Character> startingPositions, Reflector.ReflectorID selectedReflectorID, List<Pair<Character, Character>> plugBoard, String abc) {
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

    public Reflector.ReflectorID getSelectedReflectorID() {
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

//    public static void main(String[] args) {
//        List<Integer> rotorsIDInorder = new ArrayList<>();
//        rotorsIDInorder.add(1);
//        rotorsIDInorder.add(2);
//        rotorsIDInorder.add(3);
//        List<Character> startingPositions = new ArrayList<>();
//        startingPositions.add('A');
//        startingPositions.add('A');
//        startingPositions.add('A');
//        Reflector.ReflectorID selectedReflectorID = Reflector.ReflectorID.I;
//        List<Pair<Character, Character>> plugBoard = new ArrayList<>();
//        MachineCode mc = new MachineCode(rotorsIDInorder, startingPositions, selectedReflectorID, plugBoard, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
//        for (int i = 0; i < 400; i++) {
//            System.out.println(mc.toString());
//            mc.increment();
//        }
//    }

    @Override
    public String toString() {
        return "rotorsIDInorder=" + rotorsIDInorder +
                ", startingPositions=" + startingPositions +
                ", selectedReflectorID=" + selectedReflectorID;
    }
}
