package enigmaEngine.interfaces;

import java.io.IOException;
import java.io.Serializable;

public interface Rotor extends Rotatable, Serializable {
    int getNumberOfRotations();

    Character peekWindow();

    enum Direction {
        LEFT, RIGHT
    }

    int getNotchIndex();

    int getOutputIndex(int inputIndex, Direction dir);
    int getStartIndex();

    void setStartIndex(char startCharacter);

    void setRotateNextRotor(Rotatable rotateNextRotor);

    void resetRotor();

    Rotor cloneRotor() throws IOException, ClassNotFoundException;
}