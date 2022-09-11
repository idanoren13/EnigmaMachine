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
import java.util.concurrent.BlockingQueue;

public class Agent extends Task<List<String>> {

    private final int id;
    private final String encryptedText;
    private final EnigmaEngine enigmaEngine;
    private final WordsDictionary wordsDictionary;
    private final BlockingQueue<MachineCodeDTO> queue;


    public Agent(int id, String encryptedText, EnigmaEngine enigmaEngine, WordsDictionary wordsDictionary, BlockingQueue<MachineCodeDTO> queue ) {
        this.wordsDictionary = wordsDictionary;
        this.id = Integer.parseInt(Thread.currentThread().getName());
        this.encryptedText = encryptedText;
        this.enigmaEngine = enigmaEngine.cloneMachine();
        this.queue = queue;

    }


    @Override
    protected List<String> call() throws Exception {
        try {
            this.enigmaEngine.setEngineConfiguration(queue.take());
        } catch (InvalidCharactersException | InvalidRotorException | InvalidReflectorException |
                 InvalidPlugBoardException ignored) { }
        String processedText = enigmaEngine.processMessage(encryptedText);
        return wordsDictionary.candidateWords(processedText);
    }
}
