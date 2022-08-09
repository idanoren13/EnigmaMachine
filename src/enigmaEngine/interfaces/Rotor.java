package enigmaEngine.interfaces;

import enigmaEngine.impl.RotorImpl;

public interface Rotor extends Rotatable {
    enum Direction {
        LEFT, RIGHT
    }
    int getOutputIndex(int inputIndex, Direction dir);

    void setStartIndex(char startCharacter);

    public void setRotateNextRotor(Rotatable rotateNextRotor);
}
