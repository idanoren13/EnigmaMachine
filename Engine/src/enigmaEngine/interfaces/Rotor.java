package enigmaEngine.interfaces;

public interface Rotor extends Rotatable {

    Character peekWindow();

    enum Direction {
        LEFT, RIGHT
    }

    int getNotchIndex();

    int getOutputIndex(int inputIndex, Direction dir);

    void setStartIndex(char startCharacter);

    void setRotateNextRotor(Rotatable rotateNextRotor);

    void resetRotor();

    Rotor cloneRotor();

}