package enigmaEngine.impl;

import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.PlugBoard;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;
import enigmaEngine.DTO.EngineDTO;
import enigmaEngine.DTO.EngineSelectedPartsDTO;
import enigmaEngine.DTO.CodeGeneratorDTO;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;

public class EnigmaEngineImpl implements EnigmaEngine, Serializable {
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
    public char activate(char input) {

        // Rotates the first rotor
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
        this.messagesSentCounter = 0;
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
    public EngineSelectedPartsDTO getSelectedParts() {
        return new EngineSelectedPartsDTO(rotors.size(), reflectors.size(), stringToList(machineABC));
    }

    @Override
    public CodeGeneratorDTO getRandomGeneratorDTO(EngineSelectedPartsDTO partsForRandom) throws InvalidPlugBoardException, InvalidRotorException, InvalidReflectorException {
        return new CodeGeneratorDTO(getSelectedParts());
    }

    @Override
    public void setEnigmaCode(CodeGeneratorDTO partsForRandom) throws InvalidCharactersException, InvalidRotorException, InvalidReflectorException, InvalidPlugBoardException {
        setSelectedRotors(partsForRandom.getSelectedRotorsList(), partsForRandom.getAllStartingPositionsList());
        setSelectedReflector(partsForRandom.getReflectorID());
        setPlugBoard(partsForRandom.getRandomPlugBoardPairsList());
    }

    private List<Character> charsAtWindows() {
        List<Character> charsAtWindows = new ArrayList<>();
        for (Rotor rotor : rotorStackRightToLeft) {
            charsAtWindows.add(rotor.peekWindow());
        }
        return charsAtWindows;
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
            throw new InvalidRotorException("Too many rotors have been selected.");
        }
        if (rotorsIDs.stream().anyMatch(rotorID -> !rotors.containsKey(rotorID))) {
            throw new InvalidRotorException("An invalid rotor is selected.");
        }
        if (new HashSet<>(rotorsIDs).size() != rotorsIDs.size()) {
            throw new InvalidRotorException("Duplicate rotors selected.");
        }
    }

    private List<Pair<Integer, Integer>> getSelectedRotorsAndNotchesDistances() {
        List<Pair<Integer, Integer>> notchPositionsByOrder = new ArrayList<>();
        for (Integer selectedRotorIdx : this.selectedRotors) {
            Rotor currRotor = this.rotors.get(selectedRotorIdx);
            int machineABCLength = this.machineABC.length();
            int distanceFromWindow = Math.floorMod(currRotor.getNotch() - currRotor.getNumberOfRotations(), machineABCLength);
            notchPositionsByOrder.add(new Pair<>(selectedRotorIdx, distanceFromWindow));
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