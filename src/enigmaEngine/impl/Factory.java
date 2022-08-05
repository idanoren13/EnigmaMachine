package enigmaEngine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static enigmaEngine.Engine.getPaperEnigmaABC;

public class Factory implements enigmaEngine.Factory {

    @Override
    public Rotor createNewRotor(ArrayList<Character> right, ArrayList<Character> left, int notch, int id, char startingIndex) {
        String abc = getPaperEnigmaABC();
        List<Character> intToChar = new ArrayList<>();
        HashMap<Character, Integer> charToInt = new HashMap<>();

        // For each character in the String
        // add it to the List
        assert abc != null;
        for (char ch : abc.toCharArray()) intToChar.add(ch);
        for (int i = 0; i < intToChar.size(); i++) {
            charToInt.put(intToChar.get(i), i);
        }
        int rotorIndex = charToInt.get(startingIndex);
        HashMap<Integer, Character> rightToLeftFirstDictionary = null, leftToRightFirstDictionary = null;
        HashMap<Character, Integer> rightToLeftSecondDictionary = null, leftToRightSecondDictionary = null;
        // TO DO: Make the dictionaries from "intToChar" and "charToInt"
        return new Rotor(
                id, rotorIndex, notch,
                rightToLeftFirstDictionary, leftToRightFirstDictionary, rightToLeftSecondDictionary, leftToRightSecondDictionary
        );
    }

    @Override
    public Reflector createNewReflector(ArrayList<Integer> right, ArrayList<Integer> left, ReflectorID id) {
        HashMap<Integer, Integer> indexPairs = new HashMap<>();
        for (int i = 0; i < left.size(); i++) {
            indexPairs.put(left.get(i), right.get(i));
            indexPairs.put(right.get(i), left.get(i));
        }
        return new Reflector(indexPairs, id);
    }
}
