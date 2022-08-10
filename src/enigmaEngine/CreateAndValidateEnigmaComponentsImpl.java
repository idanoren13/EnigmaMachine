package enigmaEngine;

import enigmaEngine.exceptions.InvalidABCException;
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

public class CreateAndValidateEnigmaComponentsImpl implements CreateAndValidateEnigmaComponents {

    private final String abc;
    private final HashMap<Character, Character> abcMap;

    public CreateAndValidateEnigmaComponentsImpl(String abc) {
        this.abc = abc.toUpperCase();
        this.abcMap = new HashMap<>();
    }

    /*
    * Creations
    * */
    @Override
    public Rotor createRotor(int id, int notch, List<Character> rightSide, List<Character> leftSide) throws InvalidRotorException {
        validateRotor(id, notch, rightSide, leftSide);
        return new RotorImpl(id, notch, rightSide, leftSide);
    }

    @Override
    public Reflector createReflector(List<Integer> input, List<Integer> output, Reflector.ReflectorID id) throws InvalidReflectorException {
        validateReflector(input, output);
        return new ReflectorImpl(input, output, id);
    }

    /*
    * Validations
    * */
    @Override
    public void ValidateABC(String abc) throws InvalidABCException {

        if (abc.length() % 2 == 0) {
            throw new InvalidABCException("ABC must be an even number of characters");
        }

        for (int i = 0; i < abc.length(); i++) {
            if (this.abcMap.containsKey(abc.charAt(i))) {
                throw new InvalidABCException("Duplicate character in abc");
            }

            this.abcMap.put(abc.charAt(i), abc.charAt(i));
        }
    }

    private void validateRotor(int id, int notch, List<Character> rightSide, List<Character> leftSide) throws InvalidRotorException {
        validateRotorId(id);
        validateRotorNotch(notch);
        validateRotorSide(rightSide, "Right");
        validateRotorSide(leftSide, "Left");
    }

    private void validateRotorSide(List<Character> side, String sideName) throws InvalidRotorException {
        // check if all the characters in side unique
        if (new HashSet(side).size() != abc.length()) {
            throw new InvalidRotorException("Rotor" + sideName + " side characters must be unique");
        }

        for (Character character : side) {
            if (!abcMap.containsKey(character)) {
                throw new InvalidRotorException("Rotor" + sideName + " characters must be in abc");
            }
        }
    }

    private void validateRotorNotch(int notch) throws InvalidRotorException {
        if (notch < 0 || notch >= abc.length()) {
            throw new InvalidRotorException("Rotor notch must be in abc");
        }
    }

    private void validateRotorId(int id) throws InvalidRotorException {
        if (id > 0) {
            throw new InvalidRotorException("Rotor id must be natural number");
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
            throw new InvalidReflectorException("Input and output must be the same size as abc length");
        }
    }
}

