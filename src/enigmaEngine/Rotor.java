package enigmaEngine;

import enigmaEngine.Engine.Direction;

import java.lang.reflect.InvocationTargetException;

public interface Rotor {
    char makeFirstImport(int input, Direction dir); // int->char
    int makeSecondImport(char input, Direction dir); // char->int
    void rotate() throws InvocationTargetException, IllegalAccessException;
}
