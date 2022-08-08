//package enigmaEngine.impl;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Engine implements enigmaEngine.Engine {
//    // private Direction direction;
//    // private ArrayList<Character> intToChar;
//    // private HashMap<Character, Integer> charToArray;
//    private final HashMap<Integer, Rotor> rotors; // From 1 to ?
//
//    private final ArrayList<Rotor> _rotors = new ArrayList<Rotor>();
//    private final HashMap<Integer, Reflector> reflectors; // From I to V
//    private final PlugBoard plugBoard;
//
//    public Engine() {
//        this.rotors = new HashMap<Integer, Rotor>();
//        this.reflectors = new HashMap<Integer, Reflector>();
//        this.plugBoard = new PlugBoard();
//    }
//
//    public Engine(HashMap<Integer, Rotor> rotors, HashMap<Integer, Reflector> reflectors, PlugBoard plugBoard) {
//        this.rotors = rotors;
//        this.reflectors = reflectors;
//        this.plugBoard = plugBoard;
//    }
//
//    //    public Direction getDirection() {
////        return direction;
////    }
//    public HashMap<Integer, Rotor> getRotors() {
//        return rotors;
//    }
//
//    public HashMap<Integer, Reflector> getReflectors() {
//        return reflectors;
//    }
//
//    public PlugBoard getPlugBoard() {
//        return plugBoard;
//    }
//
////    public void setDirection(Direction direction) {
////        this.direction = direction;
////    }
//
////    public static void main(String[] args) {
////        Factory rotor; // Create rotors
////        Factory reflector; // Create reflectors
////        Factory plugBoard; // Create plug board
////    }
//
//    @Override
//    public char run(ArrayList<Integer> rotorIndexArr, char reflectorIndex, char input) {
//        Reflector reflector = callReflector(reflectorIndex);
//        int intInput = 0, intOutput = 0;
//        // Step 1: plug board character switch, if plug board exists
//        if (getPlugBoard() != null) {
//            input = getPlugBoard().returnCharacterPair(input);
//        }
//        // TO DO: Generate "key" input to output, based on the keyboard dictionary (char->int)
//        // Step 2: Going over all rotors
//        for (int i = 0; i < rotorIndexArr.size(); i++) {
//            Rotor currRotor = callRotor(rotorIndexArr.get(i));
//            char intToCharImport = currRotor.makeFirstImport(intInput, Direction.LEFT); // int->char
//            intOutput = currRotor.makeSecondImport(intToCharImport, Direction.LEFT); // char->int
//            intInput = intOutput; // f(x)=y
//            if (i == 0) {
//                currRotor.rotate(); // index++ // TO DO: may move this upward
//            } else {
//                // TO DO - check this
//                if (callRotor(rotorIndexArr.get(i - 1)).getNotch() == callRotor(rotorIndexArr.get(i - 1)).getProgress()) {
//                    currRotor.rotate();
//                }
//            }
//        }
//        // Step 3: Going over the reflector
//        intInput = reflector.findPairByIndex(intInput);
//        // Step 4: Going over all rotors
//        for (int i = rotorIndexArr.size() - 1; i >= 0; i--) {
//            Rotor currRotor = callRotor(rotorIndexArr.get(i));
//            char intToCharImport = currRotor.makeFirstImport(intInput, Direction.RIGHT); // int->char
//            intOutput = currRotor.makeSecondImport(intToCharImport, Direction.RIGHT); // char->int
//            intInput = intOutput; // f(x)=y
//        }
//        // Step 5: plug board character switch, if plug board exists
//        // TO DO: Generate "key" input to output, based on the keyboard dictionary (int->char)
//        char output = 0;
//        if (getPlugBoard() != null) {
//            return getPlugBoard().returnCharacterPair(output);
//        } else {
//            return output;
//        }
//    }
//
//    @Override
//    public PlugBoard callPlugBoard() {
//        return getPlugBoard();
//    }
//
//    @Override
//    public Rotor callRotor(int index) {
//        return getRotors().get(index);
//    }
//
//    @Override
//    public Reflector callReflector(char index) {
//        return getReflectors().get(index);
//    }
//
//    private void rotateRotorsUpwards(){
//
//    }
//
//}
