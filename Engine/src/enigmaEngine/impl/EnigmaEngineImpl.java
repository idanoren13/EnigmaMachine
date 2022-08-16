package enigmaEngine.impl;

import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.PlugBoard;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;
import immutables.engine.EngineDTO;
import immutables.engine.EngineDTOSelectedParts;
import javafx.util.Pair;

import java.util.*;

public class EnigmaEngineImpl implements EnigmaEngine {
    private final HashMap<Integer, Rotor> rotors;
    private final HashMap<Reflector.ReflectorID, Reflector> reflectors;
    private PlugBoard plugBoard;
    private final String machineABC;
    private final Map<Character, Character> machineABCMap;
    private final Stack<Rotor> rotorStackRightToLeft;
    private final Stack<Rotor> rotorStackLeftToRight;
    private List<Integer> selectedRotors;
    private Reflector selectedReflector;
    private List<Character> startingCharacters;
    private int messagesSentCounter;


    public EnigmaEngineImpl(HashMap<Integer, Rotor> rotors, HashMap<Reflector.ReflectorID, Reflector> reflectors, PlugBoard plugBoard, String abc) {
        this.rotors = rotors;
        this.reflectors = reflectors;
        this.plugBoard = plugBoard;
        this.machineABC = abc;
        this.rotorStackRightToLeft = new Stack<>();
        this.rotorStackLeftToRight = new Stack<>();
        this.startingCharacters = new ArrayList<>();
        this.machineABCMap = new HashMap<>();
        this.messagesSentCounter = 0;
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
    public String processMessage(String input) throws InvalidCharactersException {
        input = input.toUpperCase();
        if (stringToList(input).stream().anyMatch(c -> !machineABCMap.containsKey(c))) {
            throw new InvalidCharactersException("Starting characters must be in the machine ABC");
        }
        StringBuilder output = new StringBuilder();
        messagesSentCounter++;
        for (int i = 0; i < input.length(); i++) {
            output.append(activate(input.charAt(i)));
        }

        return output.toString();
    }



    @Override
    public void setSelectedRotors(List<Integer> rotorsIDInorder, List<Character> startingPositions) throws InvalidCharactersException, InvalidRotorException {
        checkSelectedRotors(rotorsIDInorder);
        this.selectedRotors = rotorsIDInorder;
        setStartingCharacters(startingPositions);
    }

    @Override
    public void setStartingCharacters(List<Character> startingCharacters) throws InvalidCharactersException {
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

    @Override
    public void setSelectedReflector(Reflector.ReflectorID selectedReflectorID) throws InvalidReflectorException {
        if (!reflectors.containsKey(selectedReflectorID)) {
            throw new InvalidReflectorException("Reflector not found");
        }
        this.selectedReflector = reflectors.get(selectedReflectorID);
    }

    @Override
    public void setPlugBoard(List<Pair<Character, Character>> plugBoard) throws InvalidPlugBoardException {
        checkPlugBoard(plugBoard);
        this.plugBoard = new PlugBoardImpl(plugBoard);
    }

    @Override
    public void reset() {
        this.rotors.forEach((rotorID, rotor) -> rotor.resetRotor());
        try {
            setSelectedRotors(this.selectedRotors, this.startingCharacters);
        } catch (InvalidCharactersException | InvalidRotorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EngineDTO getEngineDTO() {
        return new EngineDTO(rotors.size(),
                reflectors.size(),
                plugBoard.getPairs(),
                selectedReflector == null ? "" : selectedReflector.getReflectorID().toString(),
                charsAtWindows(),
                getSelectedRotorsAndNotchesDistances(), messagesSentCounter);
    }

    @Override
    public EngineDTOSelectedParts getSelectedParts() {
        return new EngineDTOSelectedParts(rotors.size(), reflectors.size(), stringToList(machineABC));
    }

    private List<Character> charsAtWindows() {
        List<Character> charsAtWindows = new ArrayList<>();
        for (Rotor rotor : rotorStackRightToLeft) {
            charsAtWindows.add(rotor.peekWindow());
        }
        return charsAtWindows;
    }

    @Override
    public PlugBoard getPlugBoard() {
        return this.plugBoard;
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

    private void checkStartingCharacters(List<Character> startingCharacters) throws InvalidCharactersException {
        if (startingCharacters.size() != this.selectedRotors.size()) {
            throw new InvalidCharactersException("Starting characters must be the same size as the number of selected rotors");
        }
        if (startingCharacters.stream().anyMatch(c -> !this.machineABCMap.containsKey(c))) {
            throw new InvalidCharactersException("Starting characters must be valid");
        }
    }

    private void checkSelectedRotors(List<Integer> rotorsIDs) throws InvalidRotorException {
        if (rotorsIDs.size() > rotors.size()) {
            throw new InvalidRotorException("Too many rotors selected");
        }
        if (rotorsIDs.stream().anyMatch(rotorID -> !rotors.containsKey(rotorID))) {
            throw new InvalidRotorException("Invalid rotor selected");
        }
        if (new HashSet<>(rotorsIDs).size() != rotorsIDs.size()) {
            throw new InvalidRotorException("Duplicate rotors selected");
        }
    }

    private List<Pair<Integer, Integer>> getSelectedRotorsAndNotchesDistances() {
        List<Pair<Integer, Integer>> notchPositionsByOrder = new ArrayList<>();
        for (Integer selectedRotor : selectedRotors) {
            notchPositionsByOrder.add(new Pair<>(selectedRotor, Math.abs(rotors.get(selectedRotor).getNotch() - rotors.get(selectedRotor).getNumberOfRotations()) + 1));
        //TODO: notch distance calculation is not correct
        }

        return notchPositionsByOrder;
    }

    private void checkPlugBoard(List<Pair<Character, Character>> plugBoard) throws InvalidPlugBoardException {
        this.plugBoard = new PlugBoardImpl();

        for (Pair<Character, Character> pair : plugBoard) {
            if (!machineABCMap.containsKey(pair.getKey()) || !machineABCMap.containsKey(pair.getValue()) || this.plugBoard.containsPair(pair)) {
                throw new InvalidPlugBoardException("Plug board contains invalid characters");
            }

            this.plugBoard.addPair(pair.getKey(), pair.getValue());
        }
    }

    private List<Character> stringToList(String input) {
        List<Character> output = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            output.add(input.charAt(i));
        }
        return output;
    }
}

