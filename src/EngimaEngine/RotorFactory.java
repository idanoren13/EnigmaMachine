package EngimaEngine;

public interface RotorFactory {
    Rotor createNewRotor(int id, String right, String left, int notch);
}
