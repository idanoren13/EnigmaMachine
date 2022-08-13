package immutables.userInterface;

import Resources.PairClass;
import enigmaEngine.interfaces.Reflector;

import java.util.*;
import java.util.stream.Collectors;

// UI->DTO->Engine
// A DTO for sections 3, 4
public class InitCode {
    private Deque<Integer> selectedRotors; // ArrayDeque<Integer>, LIFO collection (as same as stack)
    private List<Character> startingCharacters;
    private Reflector.ReflectorID reflector;
    private List<PairClass<Character, Character>> abcPairs;
    private List<Integer> notchIndexes;

    // For manual machine initialization
    public InitCode(String selectedRotors, String startingCharacters, int reflectorID, String plugBoardInput, List<Integer> allNotchIndexes) {
        this.selectedRotors = createSelectedRotorsDeque(selectedRotors);
        this.startingCharacters = createStartingCharactersList(startingCharacters);
        this.reflector = Reflector.ReflectorID.values()[reflectorID - 1]; // Returned value is 1-5
        this.abcPairs = createPlugBoardPairs(plugBoardInput);
        this.notchIndexes = mapRelevantNotchIndexes(allNotchIndexes);
    }

    // For automatic machine initialization
    public InitCode(String machineABC, int rotorsCount, int totalRotors, List<String> reflectorsID, List<Integer> allNotchIndexes) {
        this.selectedRotors = createSelectedRotorsDeque(pickRandomRotors(rotorsCount, totalRotors));
        this.startingCharacters = createStartingCharactersList(pickRandomStartingCharacters(machineABC, rotorsCount));
        this.reflector = Reflector.ReflectorID.values()[pickRandomReflectorID(reflectorsID)]; // Returned value is 0-4
        this.abcPairs = createPlugBoardPairs(createRandomABCString(machineABC));
        this.notchIndexes = mapRelevantNotchIndexes(allNotchIndexes);
    }

    // Non-Randoms

    private Deque<Integer> createSelectedRotorsDeque(String selectedRotors) {
        String[] stringRotors = selectedRotors.split(",");
        int intRotors[] = Arrays.stream(stringRotors).mapToInt(Integer::parseInt).toArray();
        List<Integer> listRotors = Arrays.stream(intRotors).boxed().collect(Collectors.toList());
        return new ArrayDeque<>(listRotors);
    }

    private List<Character> createStartingCharactersList(String startingCharacters) {
        List<Character> listCharacters = new ArrayList<>();
        for (char ch : startingCharacters.toCharArray()) {
            listCharacters.add(ch);
        }
        return listCharacters;
    }

    private List<PairClass<Character, Character>> createPlugBoardPairs(String abcString) {
        char abcArr[] = abcString.toCharArray();
        List<PairClass<Character, Character>> abcPairs = new ArrayList<>();
        for (int i = 0; i < abcArr.length; i += 2) {
            abcPairs.add(new PairClass<Character, Character>(abcArr[i], abcArr[i + 1]));
        }
        return abcPairs;
    }

    // Randoms
    private String pickRandomRotors(int rotorsCount, int totalRotors) {
        // Will generate from left (last) to right (first) rotor-IDs
        // rotorsCount - rotors the machine will use
        // totalRotors - the maximum ID of the given rotors in the XML file
        HashMap<Integer, Boolean> allIDs = new HashMap<Integer, Boolean>();
        List<Integer> rotorIDs = new ArrayList<>();
        Random rand = new Random(0);
        for (int i = 0; i < totalRotors; i++) {
            allIDs.put(i, false);
        }
        for (int i = 0; i < rotorsCount; i++) {
            int randomID = rand.nextInt() % allIDs.size();
            rotorIDs.add(randomID);
            allIDs.remove(randomID);
        }
        return rotorIDs.stream().
                map(String::valueOf).
                collect(Collectors.joining(",")); // [48, 59] -> "48,59" (List->String)
    }
    private String pickRandomStartingCharacters(String machineABC, int rotorsCount) {
        String res = "";
        Random rand = new Random(0);

        for (int i = 0; i < rotorsCount; i++) {
            int randomABCIndex = rand.nextInt() % machineABC.length();
            res = res + machineABC.charAt(randomABCIndex);
        }
        return res;
    }
    private int pickRandomReflectorID(List<String> reflectorsID) {
        HashMap<String, Boolean> allIDs = new HashMap<String, Boolean>(){{
            put("I", false);
            put("II", false);
            put("III", false);
            put("VI", false);
            put("V", false);
        }};
        for (String singleID : reflectorsID) {
            if (allIDs.containsValue(singleID) == true) {
                allIDs.put(singleID, true);
            }
        }
        Random rand = new Random(0);
        int randomID = 0;
        Boolean notFoundID = true;
        do {
            randomID = rand.nextInt() % 5;
            if (allIDs.get(randomID).equals(true)) {
                notFoundID = false;
            }
        } while(notFoundID.equals(true));
        return randomID;
    }

    private String createRandomABCString(String machineABC) {
        StringBuilder mutableABC = new StringBuilder(machineABC);
        String finalString = "";
        int abcPairs = new Random().nextInt() % ((machineABC.length() / 2) + 1); // TODO: check if the value makes sense
        Random rand = new Random(0);

        for (int i = 0; i < abcPairs; i++) {
            for (int j = 0; j < 2; j++) {
                int index = rand.nextInt(mutableABC.length() + 1);
                finalString += mutableABC.charAt(index);
                mutableABC.deleteCharAt(index);
            }
        }

        return finalString;
    }

    private List<Integer> mapRelevantNotchIndexes(List<Integer> allNotchIndexes) {
        List<Integer> relevantNotches = new ArrayList<>();
        for (Integer rotorIndex : this.selectedRotors) {
            relevantNotches.add(0, allNotchIndexes.get(rotorIndex));
        }
        if (relevantNotches.size() == 0) {
            return null;
        }
        else {
            return relevantNotches;
        }
    }

    public Deque<Integer> getSelectedRotors() {
        return this.selectedRotors;
    }

    public List<Character> getStartingCharacters() {
        return this.startingCharacters;
    }

    public Reflector.ReflectorID getReflector() {
        return this.reflector;
    }

    public List<PairClass<Character, Character>> getAbcPairs() {
        return this.abcPairs;
    }

    public List<Integer> getNotchIndexes() {
        return this.notchIndexes;
    }
}
