package consoleApp.impl;

import consoleApp.interfaces.Input;
import enigmaEngine.InitializeEnigmaEngine;
import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.EnigmaEngine;
import immutables.engine.EngineDTO;
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
    public void readMachineFromXMLFile() {
        System.out.println("Enter a valid full path to your file, including file name and its file extension.");

        try {
            engine = new InitializeEnigmaEngine().initializeEngine(InitializeEnigmaEngine.SourceMode.XML, this.scanner.nextLine());
        } catch (InvalidRotorException | InvalidABCException | InvalidReflectorException | JAXBException |
                 FileNotFoundException | UnknownSourceException e) {
            System.out.println("Exception: " + e.getMessage());
            engine = null;
            return;
        }

        System.out.println("Machine successfully initialized.");
    }

    @Override
    public void getMachineSpecs() {
        EngineDTO DTO = engine.getEngineDTO();
        StringBuilder sb = new StringBuilder();
        sb.append("Machine specs:\n");
        sb.append("\tMax Number Of Rotors: ").append(DTO.getTotalNumberOfRotors()).append("\n");
        sb.append("\tCurrent Selected Rotors: ").append(DTO.getSelectedRotors().size()).append("\n");
        sb.append("\tNumber Of Reflectors: ").append(DTO.getTotalReflectors()).append("\n");
        sb.append("\tMessages Processed: ").append(numOfDecryptions).append("\n");
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

        sb.append("<").append(DTO.currentSelectedRotorsPositions()).append(">");//TODO: implement toString
        sb.append("<").append(DTO.getSelectedReflector()).append(">");

        if (DTO.getPlugBoard() != null) {
            sb.append("<");
            //TODO: implement not working good bad logic, do not touch it ill fix it later
            Map<Character, Character> plugBoardDP =  new HashMap<>(DTO.getPlugBoard());
            for (Map.Entry<Character, Character> pair : DTO.getPlugBoard().entrySet()) {
                sb.append(pair.getKey()).append('|').append(pair.getValue()).append(",");
                if (!plugBoardDP.isEmpty()) {
                    plugBoardDP.remove(pair.getValue());
                    plugBoardDP.remove(pair.getKey());
                }
            }

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
            } catch (InvalidRotorException | InvalidStartingCharacters | NumberFormatException e) {
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
    public void initializePaperEnigmaCodeAutomatically() {
    //TODO: implement, if you need more data, create new DTO class and use the data from there

    }

    @Override
    public void encryptInput() {
        System.out.println("Enter your message to encrypt.");
        String input = this.scanner.nextLine();
        stringInput.add(input);
        stringOutput.add(engine.encryptDecrypt(input));
        System.out.println("Encrypted message: " + stringOutput.get(stringOutput.size() - 1));

        codeTransforms.add(currentMachineState(engine.getEngineDTO()).toString());

    }

    @Override
    // Reset last
    public void resetMachine() {
        engine.reset();
        System.out.println("Machine successfully reset.");
    }

    @Override
    public void getMachineStatisticsAndHistory() {

    }

    @Override
    public void exitMachine() {
        // Does nothing
    }
}
