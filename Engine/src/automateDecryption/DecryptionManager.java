package automateDecryption;

import enigmaEngine.WordsDictionary;
import enigmaEngine.interfaces.EnigmaEngine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DecryptionManager implements Runnable {
    private static final int MIN_NUMBER_OF_AGENTS= 2;
    private static final int MAX_NUMBER_OF_AGENTS= 50;
    private int maxAgents;
    private WordsDictionary wordsDictionary;
    private EnigmaEngine enigmaEngine;
    private ExecutorService executorService;
    private String encryptedText;

    public DecryptionManager(int maxAgents, WordsDictionary wordsDictionary, EnigmaEngine enigmaEngine) {
        this.maxAgents = maxAgents;
        this.wordsDictionary = wordsDictionary;
        this.enigmaEngine = enigmaEngine;
        this.executorService = new ThreadPoolExecutor(MIN_NUMBER_OF_AGENTS, MAX_NUMBER_OF_AGENTS, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), AgentFactory.getInstance());

    }



    @Override
    public void run() {
        //TODO: implement
    }
}
