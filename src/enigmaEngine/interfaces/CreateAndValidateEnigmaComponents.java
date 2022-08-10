package enigmaEngine.interfaces;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;


import java.util.List;

public interface CreateAndValidateEnigmaComponents {
    void ValidateABC(String abc) throws InvalidABCException;
    Rotor createRotor(int id, int notch, List<Character> rightSide, List<Character> leftSide) throws InvalidRotorException;
    Reflector createReflector(List<Integer> input, List<Integer> output, Reflector.ReflectorID id) throws InvalidReflectorException;
}
