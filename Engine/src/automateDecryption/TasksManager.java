package automateDecryption;

import enigmaEngine.MachineCodeDTO;
import enigmaEngine.WordsDictionary;
import enigmaEngine.interfaces.EnigmaEngine;
import javafx.concurrent.Task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TasksManager extends Task<Boolean> {
    private static final int MIN_NUMBER_OF_AGENTS= 2;
    private static final int MAX_NUMBER_OF_AGENTS= 50;
    private static final int BLOCKINGQUEUE_SIZE= 5000;

    private final int numberOfAgents;
    private final BlockingQueue<MachineCodeDTO> queue;
    private final String encryptedText;
    private final ExecutorService tasksPool;
    private long taskSize = 10;
    private EnigmaEngine enigmaEngine;
    private WordsDictionary wordsDictionary;
    private Difficulty difficulty;

    public TasksManager(int numberOfAgents, String encryptedText) {
        this.numberOfAgents = numberOfAgents;
        this.queue = new ArrayBlockingQueue<>(BLOCKINGQUEUE_SIZE);//TODO:
        this.encryptedText = encryptedText;
        tasksPool = Executors.newFixedThreadPool(this.numberOfAgents, AgentFactory.getInstance());
    }

    public void initialize(EnigmaEngine enigmaEngine, Difficulty difficulty) {
        this.enigmaEngine = enigmaEngine;
        this.wordsDictionary = enigmaEngine.getWordsDictionary();
        this.difficulty = difficulty;
        System.out.println("Number of agents: " + numberOfAgents);
    }

    @Override
    protected Boolean call() throws Exception {//TODO: Add Task Size
        System.out.println("Starting TasksManager");
        DecryptionManager decryptionManager = new DecryptionManager(enigmaEngine, queue, difficulty, taskSize);
        decryptionManager.calculateCombinationNumber();
        decryptionManager.initializeMachineCode();
        Thread decryptionManagerThread = new Thread(decryptionManager);
        decryptionManagerThread.start();
        for (int i = 0; i < numberOfAgents; i++) {
            tasksPool.execute(new Agent(i, encryptedText, enigmaEngine, wordsDictionary, queue, taskSize));
        }
        if (!decryptionManagerThread.isAlive() && queue.isEmpty()) {
            tasksPool.shutdown();
        }

        return true;
    }

    public long getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(long taskSize) {
        this.taskSize = taskSize;
    }
}

