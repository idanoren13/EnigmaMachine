//package enigmaEngine.impl;
//
//import enigmaEngine.Engine.Direction;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import static enigmaEngine.Engine.getPaperEnigmaABC;
//
//public class Rotor implements enigmaEngine.Rotor {
//    private final int id;
//    private final int startIndex; // starting index of the rotor, user picks 1 index from the whole ABC
//    private int progress; // amount of times the rotor had made a progress
//    private final int notch; // The index of the notch
//    private final HashMap<Integer, Character> rightToLeftFirstDictionary, leftToRightFirstDictionary; // TO DO - this may be final field
//    private final HashMap<Character, Integer> rightToLeftDictSecondDictionary, leftToRightDictSecondDictionary; // TO DO - this may be final field
//
//    public Rotor(
//            int id, int startIndex, int notch,
//            HashMap<Integer, Character> rightToLeftDict1, HashMap<Integer, Character> leftToRightDict1,
//            HashMap<Character, Integer> rightToLeftDict2, HashMap<Character, Integer> leftToRightDict2) {
//        this.id = id;
//        this.startIndex = startIndex;
//        this.progress = 0;
//        this.notch = notch;
//        this.rightToLeftFirstDictionary = rightToLeftDict1;
//        this.leftToRightFirstDictionary = leftToRightDict1;
//        this.rightToLeftDictSecondDictionary = rightToLeftDict2;
//        this.leftToRightDictSecondDictionary = leftToRightDict2;
//    }
//
//
//    public int getStartingIndex() {
//        return startIndex;
//    }
//    public int getProgress() {
//        return progress;
//    }
//    public int getNotch() {
//        return notch;
//    }
//    public HashMap<Integer, Character> getRightToLeftFirstDictionary() {
//        return rightToLeftFirstDictionary;
//    }
//    public HashMap<Integer, Character> getLeftToRightFirstDictionary() {
//        return leftToRightFirstDictionary;
//    }
//    public HashMap<Character, Integer> getRightToLeftSecondDictionary() {
//        return rightToLeftDictSecondDictionary;
//    }
//    public HashMap<Character, Integer> getLeftToRightDictSecondDictionary() {
//        return leftToRightDictSecondDictionary;
//    }
//
//    @Override
//    public void rotate() {
//        this.progress++;
//    }
//
//
//    @Override
//    public char makeFirstImport(int input, Direction dir) {
//        // char->int
//        // The input is an integer and the output is a character
//        if (dir == Direction.LEFT) {
//            return this.rightToLeftFirstImport(input);
//        }
//        else if (dir == Direction.RIGHT) {
//            return this.leftToRightFirstImport(input);
//        }
//        throw new AssertionError("Unknown direction: " + dir);
//    }
//    public char rightToLeftFirstImport(int input) {
//        // char->int
//        // The input is an integer and the output is a character
//        assert getPaperEnigmaABC() != null;
//        return (this.rightToLeftFirstDictionary.get((input - this.getStartingIndex() + this.getProgress()) % getPaperEnigmaABC().length()));
//    }
//    public char leftToRightFirstImport(int input) {
//        // char->int
//        // The input is an integer and the output is a character
//        assert getPaperEnigmaABC() != null;
//        return (this.leftToRightFirstDictionary.get((input - this.getStartingIndex() + this.getProgress()) % getPaperEnigmaABC().length()));
//    }
//
//    @Override
//    public int makeSecondImport(char input, Direction dir) {
//        // char->int
//        // The input is a character and the output is an integer
//        if (dir == Direction.LEFT) {
//            return this.rightToLeftSecondImport(input);
//        }
//        else if (dir == Direction.RIGHT) {
//            return this.leftToRightSecondImport(input);
//        }
//        throw new AssertionError("Unknown direction: " + dir);
//    }
//    public int rightToLeftSecondImport(char input) {
//        // char->int
//        // The input is a character and the output is an integer
//        assert getPaperEnigmaABC() != null;
//        return (this.rightToLeftDictSecondDictionary.get(input) - this.getStartingIndex() + this.getProgress()) % getPaperEnigmaABC().length();
//    }
//    public int leftToRightSecondImport(char input) {
//        // char->int
//        // The input is a character and the output is an integer
//        assert getPaperEnigmaABC() != null;
//        return (this.leftToRightDictSecondDictionary.get(input) - this.getStartingIndex() + this.getProgress()) % getPaperEnigmaABC().length();
//    }
//}
