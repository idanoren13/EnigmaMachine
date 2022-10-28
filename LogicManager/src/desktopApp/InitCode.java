package desktopApp;

import enigmaEngine.exceptions.InvalidPlugBoardException;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

// Made this class final
// All it's functions are now static
// It has no constructors
// Another static class we use in Java is NIO.Files
final public class InitCode {

    private InitCode() {

    }
    public static ArrayList<Integer> createSelectedRotorsList(String selectedRotors) throws NumberFormatException {
        String[] stringRotors = selectedRotors.split(",");
        int[] intRotors = Arrays.stream(stringRotors).mapToInt(Integer::parseInt).toArray();
        if (intRotors.length == 1) {
            throw new InputMismatchException("The given input contains one rotor. It needs to contain at least 2.");
        }
        else if (intRotors.length == 0) {
            throw new InputMismatchException("The given input contains zero rotors. It needs to contain at least 2.");
        }
        return Arrays.stream(intRotors).boxed().collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Character> createStartingCharactersList(String startingCharacters) {
        startingCharacters = startingCharacters.toUpperCase();
        List<Character> listCharacters = new ArrayList<>();
        for (char ch : startingCharacters.toCharArray()) {
            listCharacters.add(ch);
        }

        return listCharacters;
    }

    public static List<Pair<Character, Character>> createPlugBoard(String abcString) throws InvalidPlugBoardException {
        char[] abcArr = abcString.toUpperCase().toCharArray();
        List<Pair<Character, Character>> abcPairs = new ArrayList<>();

        if (abcArr.length % 2 == 1) {
            throw new InvalidPlugBoardException("Plug board must have even number of pairs.");
        }
        if (getUniqueCharacters(abcArr) != abcArr.length) {
            throw new InvalidPlugBoardException("Plug board must have unique characters.");
        }
        for (int i = 0; i < abcArr.length; i += 2) {
            abcPairs.add(new Pair<>(abcArr[i], abcArr[i + 1]));
        }

        return abcPairs;
    }

    private static int getUniqueCharacters(char[] abcArr) {
        HashSet<Character> abcSet = new HashSet<>();
        for (char c : abcArr) {
            abcSet.add(c);
        }

        return abcSet.size();
    }


}