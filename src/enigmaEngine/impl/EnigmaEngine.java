package enigmaEngine.impl;

import enigmaEngine.interfaces.PlugBoard;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class EnigmaEngine {
    private final HashMap<Integer, Rotor> rotors;
    private final HashMap<Reflector.ReflectorID, Reflector> reflectors;
    private final PlugBoard plugBoard;
    private final String machineABC;
    private final Stack<Rotor> rotorStackRightToLeft;
    private final Stack<Rotor> rotorStackLeftToRight;
    private List<Integer> selectedRotors;
    private Reflector selectedReflector;
    private List<Character> startingCharacters;

    public EnigmaEngine(HashMap<Integer, Rotor> rotors, HashMap<Reflector.ReflectorID, Reflector> reflectors, PlugBoard plugBoard, String abc) {
        this.rotors = rotors;
        this.reflectors = reflectors;
        this.plugBoard = plugBoard;
        this.machineABC = abc;
        this.rotorStackRightToLeft = new Stack<>();
        this.rotorStackLeftToRight = new Stack<>();
        this.startingCharacters = new ArrayList<>();
    }

    public char activate(char input) {

        //rotate the first rotor
        rotorStackRightToLeft.peek().rotate();

        char temp = plugBoard.returnCharacterPair(input);
        int index = machineABC.indexOf(temp);

        index = runRotorPipelineStack(rotorStackRightToLeft, rotorStackLeftToRight, index, Rotor.Direction.LEFT); //pipeline to the reflector
        index = selectedReflector.findPairByIndex(index);
        index = runRotorPipelineStack(rotorStackLeftToRight, rotorStackRightToLeft, index,  Rotor.Direction.RIGHT); //pipeline from the reflector
        temp = machineABC.charAt(index);
        return plugBoard.returnCharacterPair(temp);
    }

    private int runRotorPipelineStack(Stack<Rotor> pipelineStack, Stack<Rotor> stackToBeFilled, int index,  Rotor.Direction dir) {
        int outputIndex = index;
        while (!pipelineStack.isEmpty()) {
            outputIndex = pipelineStack.peek().getOutputIndex(outputIndex, dir);
            stackToBeFilled.push(pipelineStack.pop());
        }
        return outputIndex;
    }

    //creates the array of the selected rotors in order
    public void setSelectedRotors(List<Integer> rotorsIDInorder, ArrayList<Character> startingPositions) {
        this.selectedRotors = rotorsIDInorder;
        this.startingCharacters = startingPositions;

        for (int i = 0 ; i < selectedRotors.size(); i++) {
            rotors.get(selectedRotors.get(i)).setStartIndex(startingCharacters.get(i));
        }

        connectRotors();
        this.selectedRotors.forEach(rotorID -> this.rotorStackRightToLeft.push(this.rotors.get(rotorID)));
    }

    private void connectRotors() {
        disconnectAllRotors();
        rotors.get(selectedRotors.get(0)).setRotateNextRotor(null);

        for (int i = selectedRotors.size() - 1; i > 0; i--) {
            rotors.get(selectedRotors.get(i)).setRotateNextRotor(rotors.get(selectedRotors.get(i - 1)));
        }
    }

    private void disconnectAllRotors() {
        rotors.keySet().forEach(key -> rotors.get(key).setRotateNextRotor(null));
    }

    public void setSelectedReflector(Reflector.ReflectorID selectedReflectorID) {
        this.selectedReflector = reflectors.get(selectedReflectorID);
    }
}

