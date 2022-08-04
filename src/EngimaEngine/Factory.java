package EngimaEngine;

public interface Factory {
    public enum ReflectorID {
        I,
        II,
        III,
        IV,
        V;
    }
    Rotor createNewRotor(String right, String left, int notch, int id, String startingIndex);
    Reflector createNewReflector(String right, String left, ReflectorID id);
}
