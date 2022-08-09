package enigmaEngine.impl;

import enigmaEngine.Engine;
import enigmaEngine.Rotatable;
import enigmaEngine.Rotor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class RotorImpl implements Rotor, Rotatable {
    private final int id;
    private final int notch;
    private int countRotations;
    private final int startIndex;   //TODO: initiate starting position of the rotor by startIndex
    private final List<Character> rightSide;
    private final List<Character> leftSide;
    private final int rotorSize;
    private Method rotateNextRotor;


    public RotorImpl(int id, int notch, int startIndex, List<Character> rightSide, List<Character> leftSide) {
        this.id = id;
        this.notch = notch;
        this.startIndex = startIndex;
        this.rightSide = rightSide;
        this.leftSide = leftSide;
        this.countRotations = 0;
        this.rotorSize = rightSide.size();
        this.rotateNextRotor = null;
    }

    public int getOutputIndex(int inputIndex, Engine.Direction dir) {
        int outputIndex = -1;
        //make it better
        if (dir == Engine.Direction.RIGHT) {
            outputIndex = rightSide.indexOf(leftSide.get(inputIndex));
        } else {
            outputIndex = leftSide.indexOf(rightSide.get(inputIndex));
        }
//        if (dir == Engine.Direction.LEFT) {
//            outputIndex = findCharIndexInList(leftSide, rightSide.get(inputIndex));
//        } else {
//            outputIndex = findCharIndexInList(rightSide, leftSide.get(inputIndex));
//        }

        return outputIndex;
    }

//    private int findCharIndexInList(List<Character> list, Character ch) {
//        int index = -1;
//
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i) == ch) {
//                index = i;
//                break;
//            }
//        }
//
//        return index;
//    }

    @Override
    public char makeFirstImport(int input, Engine.Direction dir) {
        return 0;
    }

    @Override
    public int makeSecondImport(char input, Engine.Direction dir) {
        return 0;
    }

    @Override
    public void rotate() {
        Collections.rotate(rightSide, -1);
        Collections.rotate(leftSide, -1);
        this.countRotations = (countRotations + 1) % rotorSize;

        try {
            if (isNotchOnTop() && rotateNextRotor != null) { //rotates the next rotor if needed
                rotateNextRotor.invoke(null);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void rotateNext() {

    }

    private boolean isNotchOnTop() {
        return countRotations == notch;
    }

    public void setRotateNextRotor(Method rotateNextRotor) {
        this.rotateNextRotor = rotateNextRotor;
    }
}
