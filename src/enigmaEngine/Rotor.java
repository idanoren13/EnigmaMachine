package enigmaEngine;

import enigmaEngine.Engine.Direction;

public interface Rotor {
    char makeFirstImport(int input, Direction dir); // int->char
    int makeSecondImport(char input, Direction dir); // char->int
    void increaseRotor();
}
