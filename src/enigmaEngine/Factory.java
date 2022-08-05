package enigmaEngine;

import java.util.ArrayList;

public interface Factory {
    enum ReflectorID {
        I,
        II,
        III,
        IV,
        V
    }
    Rotor createNewRotor(ArrayList<Character> right, ArrayList<Character> left, int notch, int id, char startingIndex);
    Reflector createNewReflector(ArrayList<Integer> right, ArrayList<Integer> left, ReflectorID id);
}
