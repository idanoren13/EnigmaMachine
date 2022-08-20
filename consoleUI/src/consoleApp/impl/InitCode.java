package consoleApp.impl;

import enigmaEngine.exceptions.InvalidPlugBoardException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class InitCode {

    public ArrayList<Integer> createSelectedRotorsList(String selectedRotors) throws NumberFormatException {
        String[] stringRotors = selectedRotors.split(",");
        int[] intRotors = Arrays.stream(stringRotors).mapToInt(Integer::parseInt).toArray();

        return Arrays.stream(intRotors).boxed().collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Character> createStartingCharactersList(String startingCharacters) {
        startingCharacters = startingCharacters.toUpperCase();
        List<Character> listCharacters = new ArrayList<>();
        for (char ch : startingCharacters.toCharArray()) {
            listCharacters.add(ch);
        }

        return listCharacters;
    }

    public List<Pair<Character, Character>> createPlugBoard(String abcString) throws InvalidPlugBoardException {
        char[] abcArr = abcString.toUpperCase().toCharArray();
        List<Pair<Character, Character>> abcPairs = new ArrayList<>();

        if (abcArr.length % 2 == 1) {
            throw new InvalidPlugBoardException("Plug board must have even number of pairs");
        }
        HashSet<Character> abcSet = new HashSet<>();
        if (getUniqueCharacters(abcArr) != abcArr.length) {
            throw new InvalidPlugBoardException("Plug board must have unique characters");
        }
        for (int i = 0; i < abcArr.length; i += 2) {
            abcPairs.add(new Pair<>(abcArr[i], abcArr[i + 1]));
        }

        return abcPairs;
    }

    private int getUniqueCharacters(char[] abcArr) {
        HashSet<Character> abcSet = new HashSet<>();
        for (char c : abcArr) {
            abcSet.add(c);
        }

        return abcSet.size();
    }


}