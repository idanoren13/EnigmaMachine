package consoleApp.impl;

import consoleApp.historyAndStatistics.MachineActivateData;
import consoleApp.historyAndStatistics.MachineCodeData;
import consoleApp.historyAndStatistics.MachineHistoryAndStatistics;
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

    private final Scanner scanner;
    private EnigmaEngine engine;
    private final MachineHistoryAndStatistics machineHistoryAndStatistics;

    public Console() {
        this.scanner = new Scanner(System.in);
        this.engine = null;
        this.machineHistoryAndStatistics = new MachineHistoryAndStatistics();
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
        if (machineHistoryAndStatistics.isEmpty()) {
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
        sb.append("\tFirst Machine State: ").append(machineHistoryAndStatistics.getFirstMachineCode()).append("\n");
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
        resetMachine();

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
                engine.setSelectedReflector(initCode.getReflectorID(reflectorNumber));//TODO: check
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

        machineHistoryAndStatistics.add(new MachineCodeData(currentMachineState(engine.getEngineDTO()).toString()));
    }

    //TODO: Bad design, the automated should be in the engine class and the conversions will be in the UI
    @Override
    public void initializeEnigmaCodeAutomatically() {
        InitCode initCode = new InitCode();
        String selectedRotors;
        String allStartingPositions;
        String reflectorID;
        String randomPlugBoard;
        int randomNumberOfRotors;
        Random random = new Random();
        resetMachine();

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

        machineHistoryAndStatistics.add(new MachineCodeData(currentMachineState(engine.getEngineDTO()).toString()));
        System.out.println("Automatically initialized code: " + machineHistoryAndStatistics.getCurrentMachineCode());
    }

    @Override
    public void getMessageAndProcessIt() {
        int timeStart, timeEnd;
        String output;
        String input;
        if (machineHistoryAndStatistics.isEmpty()) {
            System.out.println("No engine code was initialized.");
            return;
        }
        System.out.println("Enter your message to process.");
        input = this.scanner.nextLine();
        try {
            timeStart = (int) System.nanoTime();
            output = engine.processMessage(input);
            timeEnd = (int) System.nanoTime();
        } catch (InvalidCharactersException e) {
            System.out.println("Exception: " + e.getMessage());
            return;
        }

        machineHistoryAndStatistics.addActivateDataToCurrentMachineCode(input, output, timeEnd - timeStart);
        System.out.println("Processed message: " + output);
    }

    @Override
    // Reset last
    public void resetMachine() {
        if (machineHistoryAndStatistics.isEmpty()) {
            System.out.println("Machine was not generated.");
            return;
        }
        engine.reset();

        System.out.println("Machine successfully reset.");
    }

    //TODo: create this method in the MachineHistoryAndStatistics class and activate it here
    @Override
    public void getMachineStatisticsAndHistory() {
        StringBuilder sb = new StringBuilder();
        sb.append("Machine statistics and history:\n");
        for (MachineCodeData machineCodeData : this.machineHistoryAndStatistics) {
            sb.append(machineCodeData.getMachineCode()).append("\n");
            int i = 1;
            for (MachineActivateData machineActivateData : machineCodeData.getMachineActivateData()) {
                sb.append("\t").append(i).append(". ").append(machineActivateData.getRawData()).append(" -> ")
                        .append(machineActivateData.getProcessedData()).append(" : ").append(machineActivateData.getTimeElapsed()).append("\n");
                i++;
            }
        }

        System.out.println(sb.toString());
    }

    @Override
    public void exitMachine() {
        System.out.println("Goodbye!");
    }
}
