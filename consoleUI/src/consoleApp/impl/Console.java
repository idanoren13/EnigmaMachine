package consoleApp.impl;

import consoleApp.interfaces.Input;
import enigmaEngine.InitializeEnigmaEngine;
import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.EnigmaEngine;
import immutables.engine.EngineDTO;
import immutables.engine.EngineDTOSelectedParts;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.*;

public class Console implements Input {

    private Scanner scanner;
    private EnigmaEngine engine;
    private int numOfDecryptions;
    private List<String> stringInput;
    private List<String> stringOutput;
    private List<Integer> timeStamps;
    private List<String> codeTransforms;

    private MachineHistoryAndStatistics machineHistoryAndStatistics;

    public Console() {
        this.scanner = new Scanner(System.in);
        this.engine = null;
        this.numOfDecryptions = 0;
        this.stringInput = new ArrayList<>();
        this.stringOutput = new ArrayList<>();
        this.timeStamps = null;
        this.codeTransforms = new ArrayList<>();
    }

    public Scanner getScanner() {
        return scanner;
    }

    public Console(EnigmaEngine machine) {
        this.engine = machine;
        this.stringInput = null;
        this.stringOutput = null;
        this.timeStamps = null;
    }

    @Override
    public Boolean readMachineFromXMLFile() {
        EnigmaEngine tempEngine;
        System.out.println("Enter a valid full path to your file, including file name and its file extension.");

        try {
            tempEngine = new InitializeEnigmaEngine().initializeEngine(InitializeEnigmaEngine.SourceMode.XML, this.scanner.nextLine());
        } catch (InvalidRotorException | InvalidABCException | InvalidReflectorException | JAXBException |
                 FileNotFoundException | UnknownSourceException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }

        this.engine = tempEngine;
        System.out.println("Machine successfully initialized.");

        return true;
    }

    @Override
    public void getMachineSpecs() {
        if (codeTransforms.isEmpty()) {
            System.out.println("Machine was not generated.");
            return;
        }

        EngineDTO DTO = engine.getEngineDTO();
        StringBuilder sb = new StringBuilder();
        sb.append("Machine specs:\n");
        sb.append("\tMax Number Of Rotors: ").append(DTO.getTotalNumberOfRotors()).append("\n");
        sb.append("\tCurrent Selected Rotors: ").append(DTO.getSelectedRotors().size()).append("\n");
        sb.append("\tNumber Of Reflectors: ").append(DTO.getTotalReflectors()).append("\n");
        sb.append("\tMessages Processed: ").append(DTO.getMessagesSentCounter()).append("\n");
        sb.append("\tFirst Machine State: ").append(codeTransforms.get(0)).append("\n");
        sb.append("\tCurrent Machine State: ").append(currentMachineState(DTO)).append("\n");

        System.out.println(sb.toString());
    }

    private StringBuilder currentMachineState(EngineDTO DTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        for (Pair<Integer, Integer> rotorIDAndNotchPosition : DTO.getSelectedRotorsAndNotchesPosition()) {
            sb.append(rotorIDAndNotchPosition.getKey()).append('(').append(rotorIDAndNotchPosition.getValue()).append("),");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(">");

        sb.append("<");
        DTO.currentSelectedRotorsPositions().forEach(sb::append);
        sb.append(">");

        sb.append("<").append(DTO.getSelectedReflector()).append(">");

        if (!DTO.getPlugBoard().isEmpty()) {
            sb.append("<");
            Map<Character, Character> plugBoardDP = new HashMap<>(DTO.getPlugBoard());

            List<Map.Entry<Character, Character>> plugBoard = new ArrayList<>(plugBoardDP.entrySet());
            Set<Pair<Character, Character>> plugBoardSet = new HashSet<>();

            for (Map.Entry<Character, Character> entry : plugBoard) {
                Pair<Character, Character> pair = new Pair<>(entry.getValue(), entry.getKey());
                if (!plugBoardSet.contains(pair)) {
                    plugBoardSet.add(new Pair<>(entry.getKey(), entry.getValue()));
                }
            }

            plugBoardSet.forEach(entry -> sb.append(entry.getKey()).append("|").append(entry.getValue()).append(","));
            sb.deleteCharAt(sb.length() - 1);
            sb.append(">");
        }

        return sb;
    }

    @Override
    public void initializeEnigmaCodeManually() {
        boolean isValid = false;
        String selectedRotors;
        String allStartingPositions;
        String reflectorNumber;
        String allPlugBoardPairs;
        InitCode initCode = new InitCode();

        while (!isValid) {
            try {
                System.out.println("Enter your desired rotor IDs separated by a comma without spaces.");
                selectedRotors = this.scanner.nextLine();
                System.out.println("Enter your desired rotor starting positions no separation between them.");
                allStartingPositions = this.scanner.nextLine();
                engine.setSelectedRotors(initCode.createSelectedRotorsDeque(selectedRotors), initCode.createStartingCharactersList(allStartingPositions));
                isValid = true;
            } catch (InvalidRotorException | InvalidCharactersException | NumberFormatException e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }

        isValid = false;
        while (!isValid) {
            try {
                System.out.println("Enter your desired reflector ID please enter the number using romans numerals (I, II, III, IV, V)");
                reflectorNumber = this.scanner.nextLine();
                engine.setSelectedReflector(initCode.getReflectorID(reflectorNumber));
                isValid = true;
            } catch (InvalidReflectorException | IllegalArgumentException e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }

        isValid = false;
        while (!isValid) {
            try {
                System.out.println("Enter your desired plug board pairs with no separation.");
                allPlugBoardPairs = this.scanner.nextLine();
                engine.setPlugBoard(initCode.createPlugBoard(allPlugBoardPairs));
                isValid = true;
            } catch (InvalidPlugBoardException e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }


        codeTransforms.add(currentMachineState(engine.getEngineDTO()).toString());
    }

    @Override
    public void initializeEnigmaCodeAutomatically() {
        InitCode initCode = new InitCode();
        String selectedRotors;
        String allStartingPositions;
        String reflectorID;
        String randomPlugBoard;
        int randomNumberOfRotors;
        Random random = new Random();

        EngineDTOSelectedParts partsForRandom = engine.getSelectedParts();
        randomNumberOfRotors = random.nextInt(partsForRandom.getNumberOfRotors() - 1) + 2;

        selectedRotors = initCode.pickRandomRotors(randomNumberOfRotors, partsForRandom.getNumberOfRotors());
        allStartingPositions = initCode.pickRandomStartingCharacters(partsForRandom.getABC(), randomNumberOfRotors);
        try {
            engine.setSelectedRotors(initCode.createSelectedRotorsDeque(selectedRotors), initCode.createStartingCharactersList(allStartingPositions));
        } catch (InvalidCharactersException | InvalidRotorException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        reflectorID = initCode.pickRandomReflectorID(partsForRandom.getNumberOfReflectors());
        try {
            engine.setSelectedReflector(initCode.getReflectorID(reflectorID));
        } catch (InvalidReflectorException | IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        randomPlugBoard = initCode.pickRandomPlugBoard(partsForRandom.getABC());
        try {
            engine.setPlugBoard(initCode.createPlugBoard(randomPlugBoard));
        } catch (InvalidPlugBoardException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        codeTransforms.add(currentMachineState(engine.getEngineDTO()).toString());
        System.out.println("Automatically initialized code: " + codeTransforms.get(codeTransforms.size() - 1));
    }

    @Override
    public void encryptInput() {
        int timeStart, timeEnd;
        String output;
        String input;
        if (codeTransforms.isEmpty()) {
            System.out.println("Machine was not generated.");
            return;
        }
        System.out.println("Enter your message to encrypt.");
        input = this.scanner.nextLine();
        try {
            timeStart = (int) System.nanoTime();
            output = engine.encryptDecrypt(input);
            timeEnd = (int) System.nanoTime();
        } catch (InvalidCharactersException e) {
            System.out.println("Exception: " + e.getMessage());
            return;
        }

        stringInput.add(input);
        stringOutput.add(output);
        timeStamps.add(timeEnd - timeStart);
        System.out.println("Encrypted message: " + stringOutput.get(stringOutput.size() - 1));

        codeTransforms.add(currentMachineState(engine.getEngineDTO()).toString());

    }

    @Override
    // Reset last
    public void resetMachine() {
        if (codeTransforms.isEmpty()) {
            System.out.println("Machine was not generated.");
            return;
        }
        engine.reset();
        System.out.println("Machine successfully reset.");
    }

    @Override
    public void getMachineStatisticsAndHistory() {
        StringBuilder sb = new StringBuilder();
        for (int i= 0; i < codeTransforms.size(); i++) {
            sb.append(codeTransforms.get(i)).append(" :\n")
                    .append(stringInput.get(i)).append("->")
                    .append(stringOutput.get(i))
                    .append("(").append(timeStamps.get(i)).append(" nano-seconds)");
            System.out.println(sb.toString());
            sb.delete(0, sb.length());
        }
    }

    @Override
    public void exitMachine() {
        System.out.println("Goodbye!");
    }
}
