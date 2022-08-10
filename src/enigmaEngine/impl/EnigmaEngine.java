package enigmaEngine.impl;

import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.InvalidStartingCharacters;
import enigmaEngine.interfaces.PlugBoard;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;

import java.util.*;

public class EnigmaEngine {
    private final HashMap<Integer, Rotor> rotors;
    private final HashMap<Reflector.ReflectorID, Reflector> reflectors;
    private final PlugBoard plugBoard;
    private final String machineABC;
    private final Map<Character, Character> machineABCMap;
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
        this.machineABCMap = new HashMap<>();
        for (int i = 0; i < machineABC.length(); i++) {
            machineABCMap.put(machineABC.charAt(i), machineABC.charAt(i));
        }
    }

    public char activate(char input) {

        //rotate the first rotor
        rotorStackRightToLeft.peek().rotate();

        char temp = plugBoard.returnCharacterPair(input);
        int index = machineABC.indexOf(temp);

        index = runRotorPipelineStack(rotorStackRightToLeft, rotorStackLeftToRight, index, Rotor.Direction.LEFT); //pipeline to the reflector
        index = selectedReflector.findPairByIndex(index);
        index = runRotorPipelineStack(rotorStackLeftToRight, rotorStackRightToLeft, index, Rotor.Direction.RIGHT); //pipeline from the reflector
        temp = machineABC.charAt(index);
        return plugBoard.returnCharacterPair(temp);
    }

    public void setSelectedRotors(List<Integer> rotorsIDInorder, List<Character> startingPositions) throws InvalidStartingCharacters, InvalidRotorException {
        checkSelectedRotors(rotorsIDInorder);
        this.selectedRotors = rotorsIDInorder;
        setStartingCharacters(startingPositions);
    }

    public void setStartingCharacters(List<Character> startingCharacters) throws InvalidStartingCharacters {
        checkStartingCharacters(startingCharacters);
        this.rotorStackLeftToRight.clear();
        this.rotorStackRightToLeft.clear();
        this.startingCharacters = startingCharacters;
        disconnectAllRotors();

        for (int i = 0; i < selectedRotors.size(); i++) {
            rotors.get(selectedRotors.get(i)).setStartIndex(startingCharacters.get(i));
        }

        connectRotors();
        this.selectedRotors.forEach(rotorID -> this.rotorStackRightToLeft.push(this.rotors.get(rotorID)));
    }

    public void setSelectedReflector(Reflector.ReflectorID selectedReflectorID) {
        this.selectedReflector = reflectors.get(selectedReflectorID);
    }

    public void reset() {
        this.selectedRotors.forEach(rotorID -> this.rotors.get(rotorID).resetRotor());
        try {
            setSelectedRotors(this.selectedRotors, this.startingCharacters);
        } catch (InvalidStartingCharacters | InvalidRotorException e) {
            throw new RuntimeException(e);
        }
    }

    private int runRotorPipelineStack(Stack<Rotor> pipelineStack, Stack<Rotor> stackToBeFilled, int index, Rotor.Direction dir) {
        int outputIndex = index;
        while (!pipelineStack.isEmpty()) {
            outputIndex = pipelineStack.peek().getOutputIndex(outputIndex, dir);
            stackToBeFilled.push(pipelineStack.pop());
        }
        return outputIndex;
    }

    private void connectRotors() {
        disconnectAllRotors();
        rotors.get(selectedRotors.get(0)).setRotateNextRotor(null);

        for (int i = selectedRotors.size() - 1; i > 0; i--) {
            rotors.get(selectedRotors.get(i)).setRotateNextRotor(rotors.get(selectedRotors.get(i - 1)));
        }
    }

    private void disconnectAllRotors() {
        this.rotors.keySet().forEach(key -> this.rotors.get(key).setRotateNextRotor(null));
    }

    private void checkStartingCharacters(List<Character> startingCharacters) throws InvalidStartingCharacters {
        if (startingCharacters.size() != this.selectedRotors.size()) {
            throw new InvalidStartingCharacters("Starting characters must be the same size as the number of selected rotors");
        }
        if (startingCharacters.stream().anyMatch(c -> !this.machineABCMap.containsKey(c))) {
            throw new InvalidStartingCharacters("Starting characters must be valid");
        }
    }

    private void checkSelectedRotors(List<Integer> rotorsIDs) throws InvalidRotorException {
        if(rotorsIDs.size() > rotors.size()) {
            throw new InvalidRotorException("Too many rotors selected");
        }
        if(rotorsIDs.stream().anyMatch(rotorID -> !rotors.containsKey(rotorID))) {
            throw new InvalidRotorException("Invalid rotor selected");
        }
    }
}

