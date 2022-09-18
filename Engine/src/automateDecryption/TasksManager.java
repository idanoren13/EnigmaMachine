package automateDecryption;

import enigmaEngine.MachineCode;
import enigmaEngine.WordsDictionary;
import enigmaEngine.interfaces.EnigmaEngine;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.*;

public class TasksManager extends Task<Boolean> {
    private static final int MIN_NUMBER_OF_AGENTS = 2;
    private static final int MAX_NUMBER_OF_AGENTS = 50;
    private static final int BLOCKINGQUEUE_SIZE = 50;

    private int numberOfAgents;
    private final BlockingQueue<MachineCode> machineCodeBlockingQueue;
    private final BlockingQueue<Pair<List<String>, MachineCode>> outputBlockingQueue;
    private String encryptedText;
    private final ExecutorService tasksPool;
    private long taskSize = 100;
    private EnigmaEngine enigmaEngine;
    private WordsDictionary wordsDictionary;
    private Difficulty difficulty;


    public TasksManager() {
        this.numberOfAgents = 10;
        this.machineCodeBlockingQueue = new ArrayBlockingQueue<>(BLOCKINGQUEUE_SIZE);
        this.outputBlockingQueue = new ArrayBlockingQueue<>(BLOCKINGQUEUE_SIZE);
        this.encryptedText = null;
        this.tasksPool = Executors.newFixedThreadPool(numberOfAgents);
    }

    public TasksManager(int numberOfAgents, String encryptedText) {
        this.numberOfAgents = numberOfAgents;
        this.machineCodeBlockingQueue = new ArrayBlockingQueue<>(BLOCKINGQUEUE_SIZE);
        this.outputBlockingQueue = new ArrayBlockingQueue<>(BLOCKINGQUEUE_SIZE);
        this.encryptedText = encryptedText;
        tasksPool = Executors.newFixedThreadPool(numberOfAgents);
    }

    public void initialize(EnigmaEngine enigmaEngine, Difficulty difficulty) {
        this.enigmaEngine = enigmaEngine;
        this.wordsDictionary = enigmaEngine.getWordsDictionary();
        this.difficulty = difficulty;
        System.out.println("Number of agents: " + numberOfAgents);
    }

    @Override
    protected Boolean call() throws Exception {
        System.out.println("Starting TasksManager");
        DecryptionManager decryptionManager = new DecryptionManager(enigmaEngine, machineCodeBlockingQueue, difficulty, encryptedText, taskSize);
        decryptionManager.calculateCombinationNumber();
        decryptionManager.initializeMachineCode();
        Thread decryptionManagerThread = new Thread(decryptionManager);
        decryptionManagerThread.start();
        if (decryptionManagerThread.isAlive()) {
            System.out.println("DecryptionManager is alive");
        }
        else {
            System.out.println("DecryptionManager is dead");
        }
        for (int i = 0; i < numberOfAgents; i++) {
            tasksPool.execute(new Agent(i, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputBlockingQueue, taskSize));
        }

        tasksPool.shutdown();
        if (tasksPool.isShutdown()) {
            System.out.println("ExecutorService is shutdown");
        }

        return true;
    }

    public void test() {
        System.out.println("Starting TasksManager");
        DecryptionManager decryptionManager = new DecryptionManager(enigmaEngine, machineCodeBlockingQueue, difficulty, encryptedText, taskSize);
        decryptionManager.calculateCombinationNumber();
        decryptionManager.initializeMachineCode();
        Thread decryptionManagerThread = new Thread(decryptionManager);
        decryptionManagerThread.start();
//        for (int i = 0; i < numberOfAgents; i++) {
//            tasksPool.execute(new Agent(i, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputBlockingQueue, taskSize));
//        }
//        tasksPool.execute(new Agent(0, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputBlockingQueue, taskSize));
//
//        tasksPool.execute(new Agent(1, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputBlockingQueue, taskSize));
//        tasksPool.execute(new Agent(2, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputBlockingQueue, taskSize));
//        for(int i = 0; i < numberOfAgents; i++){
//            new Thread(new Agent(i, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputBlockingQueue, taskSize)).start();
//        }
        new Thread(new Agent(0, enigmaEngine.cloneMachine(), machineCodeBlockingQueue, encryptedText, outputBlockingQueue, taskSize)).start();
        new Thread(new Agent(1, enigmaEngine.cloneMachine(), machineCodeBlockingQueue, encryptedText, outputBlockingQueue, taskSize)).start();
        new Thread(new Agent(2, enigmaEngine.cloneMachine(), machineCodeBlockingQueue, encryptedText, outputBlockingQueue, taskSize)).start();


        tasksPool.shutdown();
        if (tasksPool.isShutdown()) {
            System.out.println("ExecutorService is shutdown");
        }
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    public long getTaskSize() {
        return taskSize;
    }

    public void setEncryptedText(String encryptedText) {
        this.encryptedText = encryptedText;
    }

    public void setTaskSize(long taskSize) {
        this.taskSize = taskSize;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isPaused() {
        return false;//TODO
    }
}

