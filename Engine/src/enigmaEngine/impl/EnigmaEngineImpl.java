package enigmaEngine.impl;

import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.InvalidStartingCharacters;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.PlugBoard;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;

import java.util.*;

public class EnigmaEngineImpl implements EnigmaEngine {
    private final HashMap<Integer, Rotor> rotors;
    private final HashMap<Reflector.ReflectorID, Reflector> reflectors;
    private PlugBoard plugBoard;
    private final String machineABC;
    private final Map<Character, Character> machineABCMap;
    private final Deque<Rotor> rotorStackRightToLeft; // Stack is thread-safe, ArrayDeque is not. Both are LIFO
    private final Deque<Rotor> rotorStackLeftToRight; // Stack is thread-safe, ArrayDeque is not. Both are LIFO
    private List<Integer> selectedRotors;
    private Reflector selectedReflector;
    private List<Character> startingCharacters;

    public EnigmaEngineImpl(HashMap<Integer, Rotor> rotors, HashMap<Reflector.ReflectorID, Reflector> reflectors, PlugBoard plugBoard, String abc) {
        this.rotors = rotors;
        this.reflectors = reflectors;
        this.plugBoard = plugBoard;
        this.machineABC = abc;
        this.rotorStackRightToLeft = new ArrayDeque<>(); // Stack is thread-safe, ArrayDeque is not. Both are LIFO
        this.rotorStackLeftToRight = new ArrayDeque<>(); // Stack is thread-safe, ArrayDeque is not. Both are LIFO
        this.startingCharacters = new ArrayList<>();
        this.machineABCMap = new HashMap<>();
        for (int i = 0; i < machineABC.length(); i++) {
            machineABCMap.put(machineABC.charAt(i), machineABC.charAt(i));
        }
    }

    @Override
    public HashMap<Integer, Rotor> getRotors() {
        return this.rotors;
    }

    @Override
    public String getMachineABC() {
        return this.machineABC;
    }
    @Override
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

    @Override
    public void setSelectedRotors(List<Integer> rotorsIDInorder, List<Character> startingPositions) throws InvalidStartingCharacters, InvalidRotorException {
        checkSelectedRotors(rotorsIDInorder);
        this.selectedRotors = rotorsIDInorder;
        setStartingCharacters(startingPositions);
    }

    @Override
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

    public void setPlugBoard(PlugBoard plugBoard) {
        this.plugBoard = plugBoard;
    }

    @Override
    public void reset() {
        this.selectedRotors.forEach(rotorID -> this.rotors.get(rotorID).resetRotor());
        try {
            setSelectedRotors(this.selectedRotors, this.startingCharacters);
        } catch (InvalidStartingCharacters | InvalidRotorException e) {
            throw new RuntimeException(e);
        }
    }

    private int runRotorPipelineStack(Deque<Rotor> pipelineStack, Deque<Rotor> stackToBeFilled, int index, Rotor.Direction dir) {
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
        // TODO: is this useless?
        for (char c : startingCharacters) {
            if (this.machineABC.contains(String.valueOf(c)) == false) {
                throw new InvalidStartingCharacters("'" + c + "' does not appear in the machine abc.");
            }
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

