package consoleApp.impl;

import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.interfaces.Reflector;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InitCode {

    public ArrayList<Integer> createSelectedRotorsDeque(String selectedRotors) throws NumberFormatException {
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
    //TODO: check if this pattern is valid by Aviad
    public String pickRandomRotors(int rotorsCount, int totalRotors) {
        List<Integer> allIDs = new  ArrayList<>();
        List<Integer> rotorIDs = new ArrayList<>();

        IntStream.rangeClosed(1, totalRotors).forEach(allIDs::add); //list 1 to totalRotors [1, 2, 3, ..., totalRotors]
        Collections.shuffle(allIDs, new Random());
        IntStream.rangeClosed(1, rotorsCount).forEach(i -> rotorIDs.add(allIDs.get(i - 1)));

        return rotorIDs.stream().
                map(String::valueOf).
                collect(Collectors.joining(",")); // [48, 59] -> "48,59" (List->String)
    }

    public String pickRandomStartingCharacters(List<Character> machineABC, int rotorsCount) {
        StringBuilder res = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < rotorsCount; i++) {
            res.append(machineABC.get(rand.nextInt(machineABC.size())));
        }

        return res.toString();
    }

    //TODO: Check roman numerials are valid by Aviad
    public String pickRandomReflectorID(int reflectorsIDSize) {
        HashMap<Integer, String> allIDs = new HashMap<>();
        allIDs.put(1, "I");
        allIDs.put(2, "II");
        allIDs.put(3, "III");
        allIDs.put(4, "IV");
        allIDs.put(5, "V");
        Random rand = new Random();

        return allIDs.get(rand.nextInt(reflectorsIDSize) + 1);
    }

    public String pickRandomPlugBoard(List<Character> machineABC) {
        List<Character> shuffledABC = new ArrayList<>(machineABC);
        Random rand = new Random();
        Collections.shuffle(shuffledABC);
        StringBuilder res = new StringBuilder();
        int abcPairs = rand.nextInt(machineABC.size() / 2);

        shuffledABC.subList(0, abcPairs * 2).forEach(res::append);

        return res.toString();
    }

    public Reflector.ReflectorID getReflectorID(String reflector) throws InvalidReflectorException, IllegalArgumentException {
        int reflectorID = romansToInt(reflector);
        if (reflectorID < 1 || reflectorID > 5) {
            throw new InvalidReflectorException("Invalid reflector ID");
        }

        return Reflector.ReflectorID.values()[reflectorID - 1];
    }

    private int romansToInt(String roman) throws IllegalArgumentException {
        int result = 0;
        for (int i = 0; i < roman.length(); i++) {
            char c = roman.charAt(i);
            if (c == 'I') {
                result += 1;
            } else if (c == 'V') {
                result += 5;
            } else {
                throw new IllegalArgumentException("Invalid roman numeral" + roman);
            }
        }

        return result;
    }
}