package enigmaEngine;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.impl.ReflectorImpl;
import enigmaEngine.impl.RotorImpl;
import enigmaEngine.interfaces.CreateAndValidateEnigmaComponents;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CreateAndValidateEnigmaComponentsImpl implements CreateAndValidateEnigmaComponents {

    private final String abc;
    private final HashMap<Character, Character> abcMap;

    private int currentRotorID, currentRotorNotch;

    public CreateAndValidateEnigmaComponentsImpl(String abc) {
        this.abc = abc.toUpperCase();
        this.abcMap = new HashMap<>();
    }

    /*
     * Creations
     * */
    @Override
    public Rotor createRotor(int id, int notch, List<Character> rightSide, List<Character> leftSide) throws InvalidRotorException {
        currentRotorID = id;
        currentRotorNotch = notch;
        validateRotor(rightSide, leftSide);
        return new RotorImpl(currentRotorID, currentRotorNotch, rightSide, leftSide);
    }

    @Override
    public Reflector createReflector(List<Integer> input, List<Integer> output, Reflector.ReflectorID id) throws InvalidReflectorException {
        for (int i = 0; i < input.size(); i++) {
            input.set(i, input.get(i) - 1);
            output.set(i, output.get(i) - 1);
        }

        validateReflector(input, output);

        return new ReflectorImpl(input, output, id);
    }

    @Override
    public void validateRotorsIDs(Map<Integer, Rotor> rotorMap) throws InvalidRotorException {
        if (rotorMap.keySet().stream().anyMatch(i -> i < 1 || i > rotorMap.size())) {
            throw new InvalidRotorException("Rotor ID must be sequential from 1 to" + rotorMap.size());
        }
    }

    @Override
    public void validateReflectorsIDs(Map<Reflector.ReflectorID, Reflector> reflectorsMap) throws InvalidReflectorException {
        if (reflectorsMap.keySet().stream().anyMatch(id -> id.ordinal() >= reflectorsMap.size())) {
            throw new InvalidReflectorException("Reflector ID must be sequential from 1 to" + reflectorsMap.size());
        }
    }

    /*
     * Validations
     * */
    @Override
    public void ValidateABC(String abc) throws InvalidABCException {

        if (abc.length() % 2 == 1) {
            throw new InvalidABCException("ABC must be an even number of characters");
        }

        for (int i = 0; i < abc.length(); i++) {
            if (this.abcMap.containsKey(abc.charAt(i))) {
                throw new InvalidABCException("Duplicate character in abc");
            }

            this.abcMap.put(abc.charAt(i), abc.charAt(i));
        }
    }

    private void validateRotor(List<Character> rightSide, List<Character> leftSide) throws InvalidRotorException {
        validateRotorId();
        validateRotorNotch();
        validateRotorSides(rightSide, leftSide);
    }

    private void validateRotorSides(List<Character> rightSide, List<Character> leftSide) throws InvalidRotorException {
        validateRotorSide(rightSide, "Right");
        validateRotorSide(leftSide, "Left");
    }

    private void validateRotorSide(List<Character> side, String sideName) throws InvalidRotorException {
        // check if all the characters in side unique
        if (new HashSet(side).size() != abc.length()) {
            throw new InvalidRotorException("Rotor " + currentRotorID + " " + sideName + " side " + ":characters must be unique");
        }

        for (Character character : side) {
            if (!abcMap.containsKey(character)) {
                throw new InvalidRotorException("Rotor " + currentRotorID + " " + sideName + " side " + ":characters must be in abc");
            }
        }
    }

    private void validateRotorNotch() throws InvalidRotorException {
        if (currentRotorNotch < 0 || currentRotorNotch >= abc.length()) {
            throw new InvalidRotorException("Rotor " + currentRotorID + " :notch must be in abc");
        }
    }

    private void validateRotorId() throws InvalidRotorException {
        if (currentRotorID < 0) {
            throw new InvalidRotorException("Rotor id must be natural number" + currentRotorID);
        }
    }

    private void validateReflector(List<Integer> input, List<Integer> output) throws InvalidReflectorException {
        if (input.size() != output.size()) {
            throw new InvalidReflectorException("Input and output must be the same size");
        }

        HashSet<Integer> inputSet = new HashSet<>(input);
        HashSet<Integer> outputSet = new HashSet<>(output);

        if (!inputSet.isEmpty() && !outputSet.isEmpty() && inputSet.stream().anyMatch(outputSet::contains)) {
            throw new InvalidReflectorException("Input and output must be disjoint");
        }

        inputSet.addAll(outputSet);
        if (inputSet.size() != abc.length()) {
            throw new InvalidReflectorException("Input and output must be the same size as abc length");
        }

        if (inputSet.stream().anyMatch(i -> i < 0 || i >= abc.length())) {
            throw new InvalidReflectorException("Input and output must be sequential from 1 to" + abc.length());
        }
    }

    private void validatePlugBoard(Map<Character, Character> plugBoard) throws InvalidPlugBoardException {
        if (plugBoard.keySet().stream().anyMatch(i -> i > (abc.length() / 2))) {
            throw new InvalidPlugBoardException("Plug board must be at most half as ABC" + abc.length());
        }
    }

}

