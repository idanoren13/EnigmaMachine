package enigmaEngine.interfaces;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.impl.ReflectorImpl;
import enigmaEngine.impl.RotorImpl;
import immutables.ReflectorID;

import java.util.List;
import java.util.Map;

public interface CreateAndValidateEnigmaComponents {
    void ValidateABC(String abc) throws InvalidABCException;
    Rotor createRotor(int id, int notch, List<Character> rightSide, List<Character> leftSide) throws InvalidRotorException;
    Reflector createReflector(List<Integer> input, List<Integer> output, ReflectorID id) throws InvalidReflectorException;
    void validateRotorsIDs(Map<Integer, RotorImpl> rotors) throws InvalidRotorException;
    void validateReflectorsIDs(Map<ReflectorID, ReflectorImpl> reflectors) throws InvalidReflectorException;

}