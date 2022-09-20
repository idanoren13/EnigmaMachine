package desktopApp.impl;

import automateDecryption.Difficulty;
import automateDecryption.TasksManager;
import desktopApp.InitCode;
import desktopApp.exceptions.NoMachineGeneratedException;
import desktopApp.historyAndStatistics.MachineCodeData;
import desktopApp.historyAndStatistics.MachineHistoryAndStatistics;
import desktopApp.interfaces.Input;
import desktopApp.interfaces.XMLLoader;
import enigmaEngine.InitializeEnigmaEngine;
import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.schemaBinding.CTEDecipher;
import enigmaEngine.schemaBinding.CTEEnigma;
import enigmaEngine.schemaBinding.CTEReflector;
import enigmaEngine.schemaBinding.CTERotor;
import immutables.engine.EngineDTO;
import javafx.beans.value.ObservableValue;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Console implements Input {
    private EnigmaEngine engine;
    private XMLLoader xmlLoader;

    private TasksManager bruteForceTaskManager;
    Difficulty difficulty;
    private MachineHistoryAndStatistics machineHistoryAndStatistics;

    public Console() {
        this.engine = null;
        this.machineHistoryAndStatistics = new MachineHistoryAndStatistics();
        this.bruteForceTaskManager = new TasksManager();
    }

    @Override
    public String getCurrentMachineState() {
        EngineDTO DTO = engine.getEngineDTO();
        return currentMachineState(DTO).toString();
    }

    public XMLLoader getXmlLoader() {
        return xmlLoader;
    }

    public TasksManager getBruteForceTaskManager() {
        return bruteForceTaskManager;
    }

    @Override
    public int getMessageCounter() {
        EngineDTO DTO = engine.getEngineDTO();
        return DTO.getMessagesSentCounter();
    }

    @Override
    public MachineHistoryAndStatistics getMachineHistoryStates() {
        return machineHistoryAndStatistics;
    }

    @Override
    public EnigmaEngine getEngine() {
        return this.engine;
    }

    @Override
    public void setEngine(EnigmaEngine engine) {
        this.engine = engine;
    }

    @Override
    public void readMachineFromXMLFile(String path) throws InvalidMachineException, JAXBException, InvalidRotorException, IOException, InvalidABCException, UnknownSourceException, InvalidReflectorException {
        this.engine = new InitializeEnigmaEngine().initializeEngine(InitializeEnigmaEngine.SourceMode.XML, path);
        createDTOFromXMLSource(path);
    }

    private void createDTOFromXMLSource(String path) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("enigmaEngine.schemaBinding");
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            CTEEnigma xmlFile = (CTEEnigma) jaxbUnmarshaller.unmarshal(Files.newInputStream(Paths.get(path)));
            // Ex1
            List<Integer> rotorsFromXML = xmlFile.getCTEMachine().getCTERotors().getCTERotor()
                    .stream().map(CTERotor::getId).collect(Collectors.toList());
            List<String> reflectorsFromXML = xmlFile.getCTEMachine().getCTEReflectors().getCTEReflector()
                    .stream().map(CTEReflector::getId).collect(Collectors.toList());
            List<Character> ABCFromXML = xmlFile.getCTEMachine()
                    .getABC().trim()
                    .chars().mapToObj(e -> (char) e).collect(Collectors.toList());

            Collections.sort(rotorsFromXML);
            Collections.sort(reflectorsFromXML);
            Collections.sort(ABCFromXML);
            // Ex2
            CTEDecipher decipher = xmlFile.getCTEDecipher();
            String excludedCharacters = decipher.getCTEDictionary().getExcludeChars();
            String nonSeparatedDictionaryWordsWithExcluded = decipher.getCTEDictionary().getWords().trim();
            String nonSeparatedDictionaryWordsValid = nonSeparatedDictionaryWordsWithExcluded;
            for (int i = 0; i < excludedCharacters.length(); i++) {
                nonSeparatedDictionaryWordsValid = nonSeparatedDictionaryWordsWithExcluded.replace(excludedCharacters.substring(i, i + 1), "");
            }
            List<String> dictionaryWords = new ArrayList<>(Arrays.asList(nonSeparatedDictionaryWordsValid.split(" ")));
            xmlLoader = new XMLLoaderImpl(rotorsFromXML, reflectorsFromXML, ABCFromXML, dictionaryWords, decipher.getAgents()); // TODO: if agents smaller than 2
        } catch (Exception e) {
        } // Irrelevant
    }

    @Override
    public void getMachineSpecs() {
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
                    "\tFirst Machine State: " + machineHistoryAndStatistics.getCurrentMachineCode() + "\n" +
                    "\tCurrent Machine State: " + currentMachineState(DTO) + "\n";

            System.out.println(str);
        } catch (NoMachineGeneratedException e) {
            System.out.println("Exception: " + e.getLocalizedMessage());
        }
    }

    // Function changed.
    // Machine engine code output previously was: <1(0),2(1),...,n(n-1)><A,...,A><I><A|B,C|D,...,(N-1)|N>
    // I changed it to: <1,2,...,n><A(0),...,A(n-1)><I><A|B,C|D,...,(N-1)|N>
    private StringBuilder currentMachineState(EngineDTO DTO) {
        List<Pair<Integer, Integer>> selectedRotorsAndNotchesPosition = DTO.getSelectedRotorsAndNotchesPosition();
        List<Character> selectedRotorsPositions = DTO.getCurrentSelectedRotorsPositions();
        int size = selectedRotorsAndNotchesPosition.size();
        StringBuilder rotorsSBPart1 = new StringBuilder();
        StringBuilder rotorsSBPart2 = new StringBuilder();
        StringBuilder finalSB = new StringBuilder();
        rotorsSBPart1.append("<");
        rotorsSBPart2.append("<");
        for (int i = 0; i < size; i++) {
            rotorsSBPart1.append(selectedRotorsAndNotchesPosition.get(i).getKey()).append(','); // Eventually it becomes <1,2,...,n,
            rotorsSBPart2.append(selectedRotorsPositions.get(i))
                    .append('(').append(selectedRotorsAndNotchesPosition.get(i).getValue()).append("),"); // Eventually it becomes <A(1),...,A(n),
        }

        rotorsSBPart1.deleteCharAt(rotorsSBPart1.length() - 1).append(">"); // Eventually it becomes <1,2,...,n>
        rotorsSBPart2.deleteCharAt(rotorsSBPart2.length() - 1).append("><"); // Eventually it becomes <A(1),...,A(n)><
        finalSB.append(rotorsSBPart1).append(rotorsSBPart2)
                .append(DTO.getSelectedReflector()).append(">"); // Eventually it becomes <1,2,...,n><A(1),...,A(n)><I>

        if (!DTO.getPlugBoard().isEmpty()) {
            StringBuilder plugBoardSB = createPlugBoardPairsStringBuilder(DTO.getPlugBoard());
            finalSB.append(plugBoardSB);
        } // Eventually it becomes <1,2,...,n><A(1),...,A(n)><I><A|B,C|D,...,(N-1)|N>

        return finalSB;
    }

    private StringBuilder createPlugBoardPairsStringBuilder(Map<Character, Character> enginePlugBoard) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        List<Map.Entry<Character, Character>> plugBoard = new ArrayList<>(new HashMap<>(enginePlugBoard).entrySet());
        Set<Pair<Character, Character>> plugBoardSet = new HashSet<>();

        for (Map.Entry<Character, Character> entry : plugBoard) {
            Pair<Character, Character> pair = new Pair<>(entry.getValue(), entry.getKey());
            if (!plugBoardSet.contains(pair)) {
                plugBoardSet.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }

        plugBoardSet.forEach(entry -> sb.append(entry.getKey()).append("|").append(entry.getValue()).append(","));
        sb.deleteCharAt(sb.length() - 1).append(">");

        return sb;
    }

    @Override
    // Changed method. Quitting is now case-insensitive +  I added a call to 'addEnigmaCode' and this adds the new Enigma code.
    public void initializeEnigmaCodeManually(String rotors, String startingPositions, String plugBoardPairs, String reflectorID) throws InputMismatchException, IllegalArgumentException, InvalidRotorException, InvalidReflectorException, InvalidPlugBoardException, InvalidCharactersException {
        List<Integer> selectedRotorsDeque;
        resetMachine();
        selectedRotorsDeque = getRotorsFromUserInput(rotors);
        getStartingPositionsFromUserInput(selectedRotorsDeque, startingPositions);
        getReflectorFromUserInput(reflectorID);
        getPlugBoardPairsFromUserInput(plugBoardPairs);

        addEnigmaCode("Manually");
    }

    // Added check case. For instance: now "2,2" or "0, 2" raises exception. Also added more information if a user inserts an invalid rotor ID.
    private List<Integer> getRotorsFromUserInput(String rotors) throws InvalidRotorException {
        List<Integer> selectedRotorsDeque = null;
        System.out.println("Enter your desired rotor IDs starting from 1, separated by a comma without spaces.");
        selectedRotorsDeque = InitCode.createSelectedRotorsList(rotors);
        if (new HashSet<>(selectedRotorsDeque).size() < selectedRotorsDeque.size()) {
            throw new InvalidRotorException("A rotor ID was inserted several times. Please insert only unique values.");
        }
        List<Integer> finalSelectedRotorsDeque = InitCode.createSelectedRotorsList(rotors);
        Integer invalidRotorID = InitCode.createSelectedRotorsList(rotors)
                .stream().filter(rotorID -> rotorID > engine.getRotors().size() || rotorID < 1)
                .findFirst().orElse(null); // Now we can track the wrong input instead of just getting an exception.

        if (invalidRotorID != null) {
            throw new InvalidRotorException(String.format("Invalid rotor ID was selected - '%d'. Please insert an ID from 1 to %d.",
                    invalidRotorID, this.engine.getRotors().size()));
        }
        return selectedRotorsDeque;
    }

    private void getStartingPositionsFromUserInput(List<Integer> selectedRotorsDeque, String startingPositions) throws InvalidCharactersException, InvalidRotorException {
        System.out.println("Enter all your desired rotors starting positions without separation between them.");
        this.engine.setSelectedRotors(selectedRotorsDeque, InitCode.createStartingCharactersList(startingPositions));
    }

    private void getReflectorFromUserInput(String reflectorID) throws InvalidReflectorException {
        int reflectorNumber;
        System.out.println("Enter your desired reflector ID. Please enter the number using one of these numerals:\n"
                + "1. For reflector I\n" + "2. For reflector II\n" + "3. For reflector III\n" + "4. For reflector IV\n" + "5. For reflector V\n");
        System.out.println("If you are willing to go back to main menu, type '-1'.");
        reflectorNumber = Reflector.ReflectorID.valueOf(reflectorID).ordinal() + 1;
        if (reflectorNumber < 1 || reflectorNumber > 5) {
            throw new InvalidReflectorException(reflectorNumber + " is an invalid reflector ID.");
        }
        this.engine.setSelectedReflector(Reflector.ReflectorID.values()[reflectorNumber - 1]);
    }

    private void getPlugBoardPairsFromUserInput(String plugBoardPairs) throws InvalidPlugBoardException {
        System.out.println("Enter all your desired plug board pairs without separation between them.");
        this.engine.setPlugBoard(InitCode.createPlugBoard(plugBoardPairs));
    }

    @Override
    // Function changed. I added a call to 'addEnigmaCode' and this adds the new Enigma code.
    public void initializeEnigmaCodeAutomatically() {
        this.engine.randomSelectedComponents();
        addEnigmaCode("Automatically");
    }

    // Created this two-lines method for each time user creates a new Enigma engine code.
    private void addEnigmaCode(String message) {
        machineHistoryAndStatistics.add(new MachineCodeData(currentMachineState(this.engine.getEngineDTO()).toString()));
        System.out.println(message + " initialized code: " + machineHistoryAndStatistics.getCurrentMachineCode());
    }

    @Override
    public String getMessageAndProcessIt(String messageInput, boolean bool) throws InvalidCharactersException {
        int timeStart, timeEnd;
        String messageOutput;
        if (bool) {
            System.out.println("Enter your message to process.");
        }
        timeStart = (int) System.nanoTime();
        messageOutput = this.engine.processMessage(messageInput);
        timeEnd = (int) System.nanoTime();

        if (bool) {
            machineHistoryAndStatistics.addActivateDataToCurrentMachineCode(messageInput, messageOutput, timeEnd - timeStart);
        }
        return messageOutput;
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
    public String getMachineStatisticsAndHistory() {
        return this.machineHistoryAndStatistics.toString();
    }

    @Override
    public void exitMachine() {
        System.out.println("Goodbye!");
    }

    public Set<String> getWordsDictionary() {
        return engine.getWordsDictionary().getWords();
    }

    public void setDMProperties(int agents, int missionSize, Difficulty difficulty) {
        if (bruteForceTaskManager.isRunning()) {
            System.out.println("Cannot change DM properties while running.");
        } else {
            bruteForceTaskManager.setNumberOfAgents(agents);
            bruteForceTaskManager.setTaskSize(missionSize);
            bruteForceTaskManager.setDifficulty(difficulty);
        }
    }

    public void startResumeDM() {
        if (bruteForceTaskManager.isRunning()) {
            System.out.println("Cannot start DM while running.");
        } else {
//            bruteForceTaskManager.setTextToCrack();
            bruteForceTaskManager.initialize(engine, Difficulty.EASY);
            new Thread(bruteForceTaskManager).start();
        }
    }

    public void setEncryptedText(String text) {
        bruteForceTaskManager.setEncryptedText(text);
    }

    public void stopDM() {
        bruteForceTaskManager.stop();
    }

    public ObservableValue<? extends Number> getProgressProperty() {
        return bruteForceTaskManager.getProgressProperty();
    }

    public void startDM() {
        bruteForceTaskManager.initialize(engine, difficulty);
        new Thread(bruteForceTaskManager).start();
    }
}