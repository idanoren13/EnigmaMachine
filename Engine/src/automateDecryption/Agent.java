package automateDecryption;

import enigmaEngine.MachineCodeDTO;
import enigmaEngine.WordsDictionary;
import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.interfaces.EnigmaEngine;
import javafx.concurrent.Task;

import java.util.List;

public class Agent extends Task<List<String>> {

    private final int id;
    private final String encryptedText;
    private final EnigmaEngine enigmaEngine;
    private final WordsDictionary wordsDictionary;


    public Agent(int id, String encryptedText, EnigmaEngine enigmaEngine, WordsDictionary wordsDictionary, MachineCodeDTO machineCodeDTO) {
        this.wordsDictionary = wordsDictionary;
        this.id = Integer.parseInt(Thread.currentThread().getName());
        this.encryptedText = encryptedText;
        this.enigmaEngine = enigmaEngine.cloneMachine();
        try {
            this.enigmaEngine.setEngineConfiguration(machineCodeDTO);
        } catch (InvalidCharactersException | InvalidRotorException | InvalidReflectorException |
                 InvalidPlugBoardException ignored) { }
    }


    @Override
    protected List<String> call() throws Exception {
        String processedText = enigmaEngine.processMessage(encryptedText);
        return wordsDictionary.candidateWords(processedText);
    }
}
