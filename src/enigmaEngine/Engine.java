package enigmaEngine;

import enigmaEngine.impl.PlugBoard;

import java.util.ArrayList;

public interface Engine {

    enum Direction {
        LEFT, RIGHT
    }
    // Input to objects-wise
    char run(ArrayList<Integer> rotorIndexArr, char reflectorIndex, char input); // Things to do in constructor
    // Function to implement:
    /***
     * void getAmountRotors(); -> May needed.
    * HashMap<Integer, Rotors> pickRotors(); -> Pick k rotors, from total n rotors exist. Integer = ID, Rotors = Rotor object instance
     */
    PlugBoard callPlugBoard();
    Rotor callRotor(int index);
    Reflector callReflector(char index);

    // TO DO - import ABC String from XML file and return its String in the hierarchy of CTE-Enigma->CTE-Machine->ABC->content
    static String getPaperEnigmaABC() {
        return null; // Line 23
    }
}
