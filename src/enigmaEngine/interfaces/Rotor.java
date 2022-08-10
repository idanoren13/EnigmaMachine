package enigmaEngine.interfaces;

public interface Rotor extends Rotatable {
    enum Direction {
        LEFT, RIGHT
    }
    int getOutputIndex(int inputIndex, Direction dir);

    void setStartIndex(char startCharacter);

    void setRotateNextRotor(Rotatable rotateNextRotor);

    void resetRotor();
}
