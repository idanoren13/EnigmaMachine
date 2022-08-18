package consoleApp.impl;

import consoleApp.exceptions.NoMachineGeneratedException;
import consoleApp.exceptions.UserQuitException;
import consoleApp.historyAndStatistics.MachineCodeData;
import consoleApp.historyAndStatistics.MachineHistoryAndStatistics;
import consoleApp.interfaces.Input;
import enigmaEngine.InitializeEnigmaEngine;
import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.Reflector;
import immutables.engine.EngineDTO;
import immutables.engine.EngineDTOSelectedParts;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Console implements Input {

    private final Scanner scanner;
    private EnigmaEngine engine;
    private MachineHistoryAndStatistics machineHistoryAndStatistics;

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
        } catch (InvalidMachineException | InvalidRotorException | InvalidABCException | InvalidReflectorException | JAXBException |
                 FileNotFoundException | UnknownSourceException | RuntimeException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }

        this.engine = tempEngine;
        System.out.println("Machine successfully initialized.");

        return true;
    }

    @Override
    public void getMachineSpecs() throws NoMachineGeneratedException {
        try {
            if (machineHistoryAndStatistics.isEmpty()) {
                throw new NoMachineGeneratedException("no machine was generated.");
            }

            EngineDTO DTO = engine.getEngineDTO();
            String str = "Machine specs:\n" +
                    "\tMax Number Of Rotors: " + DTO.getTotalNumberOfRotors() + "\n" +
                    "\tCurrent Selected Rotors: " + DTO.getSelectedRotors().size() + "\n" +
                    "\tNumber Of Reflectors: " + DTO.getTotalReflectors() + "\n" +
                    "\tMessages Processed: " + DTO.getMessagesSentCounter() + "\n" +
                    "\tFirst Machine State: " + machineHistoryAndStatistics.getFirstMachineCode() + "\n" +
                    "\tCurrent Machine State: " + currentMachineState(DTO) + "\n";

            System.out.println(str);
        } catch (NoMachineGeneratedException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private StringBuilder currentMachineState(EngineDTO DTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        for (Pair<Integer, Integer> rotorIDAndNotchPosition : DTO.getSelectedRotorsAndNotchesPosition()) {
            sb.append(rotorIDAndNotchPosition.getKey()).append('(').append(rotorIDAndNotchPosition.getValue()).append("),");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append("><");
        DTO.currentSelectedRotorsPositions().forEach(sb::append);
        sb.append("><");
        sb.append(DTO.getSelectedReflector()).append(">");

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
    public boolean initializeEnigmaCodeManually() {
        InitCode initCode = new InitCode();
        List<Integer> selectedRotorsDeque;
        resetMachine();
        try {
            selectedRotorsDeque = getRotorsFromUserInput(initCode);
            getStartingPositionsFromUserInput(selectedRotorsDeque, initCode);
            getReflectorFromUserInput();
            getPlugBoardPairsFromUserInput(initCode);
        } catch (RuntimeException e) {
            System.out.println("You are being sent back to main menu. Enigma engine code is not modified.");
            return false;
        }

        machineHistoryAndStatistics.add(new MachineCodeData(currentMachineState(this.engine.getEngineDTO()).toString()));

        return true;
    }

    private List<Integer> getRotorsFromUserInput(InitCode initCode) {
        List<Integer> selectedRotorsDeque;
        String selectedRotors;
        while (true) {
            try {
                System.out.println("Enter your desired rotor IDs separated by a comma without spaces.");
                System.out.println("If you are willing to go back to main menu, type 'OUT !'.");
                selectedRotors = this.scanner.nextLine();
                if (selectedRotors.equals("OUT !")) {
                    throw new UserQuitException("");
                }
                selectedRotorsDeque = initCode.createSelectedRotorsDeque(selectedRotors); // TODO: bad design, but required condition here. TO ASK AVIAD
                List<Integer> finalSelectedRotorsDeque = selectedRotorsDeque;
                if (initCode.createSelectedRotorsDeque(selectedRotors).stream().anyMatch(rotorID -> rotorID > finalSelectedRotorsDeque.size())) {
                    throw new InvalidRotorException("Invalid rotor ID selected. Please insert an ID from 1 to "
                            + this.engine.getRotors().size() + ".");
                }
                break;
            } catch (InvalidRotorException | NumberFormatException e) {
                System.out.println("Exception: " + e.getMessage());
            } catch (UserQuitException e) {
                throw new RuntimeException(e);
            }
        }
        return selectedRotorsDeque;
    }
    private void getStartingPositionsFromUserInput(List<Integer> selectedRotorsDeque, InitCode initCode) {
        String allStartingPositions;
        while (true) {
            try {
                System.out.println("Enter all your desired rotors starting positions without separation between them.");
                System.out.println("If you are willing to go back to main menu, type 'OUT !'.");
                allStartingPositions = this.scanner.nextLine();
                if (allStartingPositions.equalsIgnoreCase("OUT !")) {
                    throw new UserQuitException("");
                }
                this.engine.setSelectedRotors(selectedRotorsDeque, initCode.createStartingCharactersList(allStartingPositions));
                break;
            } catch (InvalidRotorException | InvalidCharactersException | NumberFormatException e) {
                System.out.println("Exception: " + e.getMessage());
            } catch (UserQuitException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void getReflectorFromUserInput() {
        int reflectorNumber;
        while (true) {
            try {
                System.out.println("Enter your desired reflector ID. Please enter the number using one of these numerals:\n"
                        + "1. For reflector I\n" + "2. For reflector II\n" + "3. For reflector III\n" + "4. For reflector IV\n" + "5. For reflector V\n");
                System.out.println("If you are willing to go back to main menu, type '-1'.");
                reflectorNumber = Integer.parseInt(this.scanner.nextLine());
                if (reflectorNumber == -1) {
                    throw new UserQuitException("");
                }
                if (reflectorNumber < 1 || reflectorNumber > 5) {
                    throw new InvalidReflectorException(reflectorNumber + " is an invalid reflector ID.");
                }
                this.engine.setSelectedReflector(Reflector.ReflectorID.values()[reflectorNumber - 1]);
                break;
            } catch (InvalidReflectorException | IllegalArgumentException e) {
                System.out.println("Exception: " + e.getMessage());
            } catch (UserQuitException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getPlugBoardPairsFromUserInput(InitCode initCode) {
        String allPlugBoardPairs;
        while (true) {
            try {
                System.out.println("Enter all your desired plug board pairs without separation between them.");
                System.out.println("If you are willing to go back to main menu, type 'OUT !'.");
                allPlugBoardPairs = this.scanner.nextLine();
                if (allPlugBoardPairs.equals("OUT !")) {
                    throw new UserQuitException("");
                }
                this.engine.setPlugBoard(initCode.createPlugBoard(allPlugBoardPairs));
                break;
            } catch (InvalidPlugBoardException e) {
                System.out.println("Exception: " + e.getMessage());
            } catch (UserQuitException e) {
                throw new RuntimeException(e);
            }
        }
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

        EngineDTOSelectedParts partsForRandom = this.engine.getSelectedParts();
        randomNumberOfRotors = random.nextInt(partsForRandom.getNumberOfRotors() - 1) + 2;

        selectedRotors = initCode.pickRandomRotors(randomNumberOfRotors, partsForRandom.getNumberOfRotors());
        allStartingPositions = initCode.pickRandomStartingCharacters(partsForRandom.getABC(), randomNumberOfRotors);
        try {
            this.engine.setSelectedRotors(initCode.createSelectedRotorsDeque(selectedRotors), initCode.createStartingCharactersList(allStartingPositions));
        } catch (InvalidCharactersException | InvalidRotorException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        reflectorID = initCode.pickRandomReflectorID(partsForRandom.getNumberOfReflectors());
        try {
            this.engine.setSelectedReflector(initCode.getReflectorID(reflectorID));
        } catch (InvalidReflectorException | IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        randomPlugBoard = initCode.pickRandomPlugBoard(partsForRandom.getABC());
        try {
            this.engine.setPlugBoard(initCode.createPlugBoard(randomPlugBoard));
        } catch (InvalidPlugBoardException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        machineHistoryAndStatistics.add(new MachineCodeData(currentMachineState(this.engine.getEngineDTO()).toString()));
        System.out.println("Automatically initialized code: " + machineHistoryAndStatistics.getCurrentMachineCode());
    }

    @Override
    public void getMessageAndProcessIt() {
        int timeStart, timeEnd;
        String output;
        String input;
        if (machineHistoryAndStatistics.isEmpty()) {
            System.out.println("No Enigma engine code was initialized.");
            return;
        }
        System.out.println("Enter your message to process.");
        input = this.scanner.nextLine().toUpperCase();
        try {
            timeStart = (int) System.nanoTime();
            output = this.engine.processMessage(input);
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
            System.out.println("This is the first Enigma code insertion.");
            return;
        }
        this.engine.reset();

        System.out.println("Machine successfully reset.");
    }

    @Override
    public void getMachineStatisticsAndHistory() {
        try {
            if (machineHistoryAndStatistics.isEmpty()) {
                throw new NoMachineGeneratedException("no machine was generated.");
            }
            System.out.println(this.machineHistoryAndStatistics);
        } catch (NoMachineGeneratedException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public void exitMachine() {
        System.out.println("Goodbye!");
    }

    @Override
    public void saveGame() {
        try {
            if (machineHistoryAndStatistics.isEmpty()) {
                throw new NoMachineGeneratedException("no machine was generated.");
            }
            System.out.println("You are about to save Enigma engine code to a file.");

            String fileNameIncludingFullPath = getFilePathFromUser();
            saveFileInPath(fileNameIncludingFullPath + ".slz");
        } catch (NoMachineGeneratedException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private String getFilePathFromUser() {
        boolean nullString;
        String fileNameIncludingFullPath;
        do {
            System.out.println("Enter a valid full path to your file, including file name ONLY and EXCLUDING its file extension.");
            fileNameIncludingFullPath = this.scanner.nextLine();
            nullString = (fileNameIncludingFullPath.equals(""));
            if (nullString) {
                System.out.println("No input was entered.");
            }
        } while (nullString);
        return fileNameIncludingFullPath;
    }

    private void saveFileInPath(String fileNameIncludingFullPath) {
        try {
            if (Files.exists(Paths.get(fileNameIncludingFullPath)) == true) {
                System.out.println("There is already a file with this name in the given path.");
                System.out.println("Do you want to proceed and save your new file? Type Y/N.");
                boolean validInput;
                String proceedOrNot = "";
                do {
                    proceedOrNot = this.scanner.nextLine().toUpperCase();
                    if (proceedOrNot.equals("Y") || proceedOrNot.equals("N")) {
                        validInput = true;
                    }
                    else {
                        System.out.println("The input given is not Y nor N.");
                        validInput = false;
                    }
                } while (!validInput);
                if (proceedOrNot.equals("N")) {
                    System.out.println("The file was not saved.");
                    return;
                }
            }
            ObjectOutputStream fileToSerialize = new ObjectOutputStream(
                    new FileOutputStream(
                            fileNameIncludingFullPath)
            );
            fileToSerialize.writeObject(this.engine);
            fileToSerialize.writeObject(this.machineHistoryAndStatistics);
            fileToSerialize.flush();

            System.out.println("File has been successfully saved!");
        } catch (FileNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public void loadGame() {
        System.out.println("You are about to load Enigma engine code from a file.");

        String fileNameIncludingFullPath = getFilePathFromUser();
        loadFileInPath(fileNameIncludingFullPath + ".slz");
    }

    private void loadFileInPath(String fileNameIncludingFullPath) {
        try {
            if (Files.notExists(Paths.get(fileNameIncludingFullPath)) == true) {
                throw new FileNotFoundException("the given file name '" + fileNameIncludingFullPath + "' not found.");
            }
            ObjectInputStream fileToDeserialize = new ObjectInputStream(
                    new FileInputStream(
                            fileNameIncludingFullPath)
            );
            this.engine = (EnigmaEngine)fileToDeserialize.readObject();
            this.machineHistoryAndStatistics = (MachineHistoryAndStatistics)fileToDeserialize.readObject();

            System.out.println("File has been successfully loaded!");
        } catch (FileNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Exception: there is a problem with loading the file.");
        }
    }
}