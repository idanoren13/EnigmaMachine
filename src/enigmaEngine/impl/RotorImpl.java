package enigmaEngine.impl;

import enigmaEngine.interfaces.Rotatable;
import enigmaEngine.interfaces.Rotor;

import java.util.Collections;
import java.util.List;

public class RotorImpl implements Rotor, Rotatable {
    private final int id;
    private final int notch;
    private int countRotations;
    private int startIndex;
    private final List<Character> rightSide;
    private final List<Character> leftSide;
    private final int rotorSize;
    private Rotatable rotateNextRotor;

    public RotorImpl(int id, int notch, List<Character> rightSide, List<Character> leftSide) {
        this.id = id;
        this.notch = notch;
        this.rightSide = rightSide;
        this.leftSide = leftSide;
        this.countRotations = 0;
        this.rotorSize = rightSide.size();
        this.rotateNextRotor = null;
        this.startIndex = 0;
    }

    @Override
    public int getOutputIndex(int inputIndex, Direction dir) {
        if (dir == Direction.RIGHT) {
            return rightSide.indexOf(leftSide.get(inputIndex));
        } else {
            return leftSide.indexOf(rightSide.get(inputIndex));
        }
//        if (dir == Engine.Direction.LEFT) {
//            outputIndex = findCharIndexInList(leftSide, rightSide.get(inputIndex));
//        } else {
//            outputIndex = findCharIndexInList(rightSide, leftSide.get(inputIndex));
//        }
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
    public void setStartIndex(char startCharacter) {
        this.startIndex = this.rightSide.indexOf(startCharacter);

        for (int i = 0; i < this.startIndex; i++) {
            rotate();
        }
    }
    
    @Override
    public void rotate() {
        Collections.rotate(rightSide, -1);
        Collections.rotate(leftSide, -1);
        this.countRotations = (countRotations + 1) % (rotorSize - 1);

        if (isNotchOnTop() && rotateNextRotor != null) { //rotates the next rotor if needed
            rotateNextRotor.rotate();
        }
    }

    private boolean isNotchOnTop() {
        return countRotations == notch;
    }

    @Override
    public void setRotateNextRotor(Rotatable rotateNextRotor) {
        this.rotateNextRotor = rotateNextRotor;
    }
}
