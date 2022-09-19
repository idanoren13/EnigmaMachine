package automateDecryption;

import enigmaEngine.MachineCode;
import enigmaEngine.WordsDictionary;
import enigmaEngine.interfaces.EnigmaEngine;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class TasksManager extends Task<Boolean> {
    private static final int MIN_NUMBER_OF_AGENTS = 2;
    private static final int MAX_NUMBER_OF_AGENTS = 50;
    private static final int BLOCKINGQUEUE_SIZE = 50;

    private int numberOfAgents;
    private final BlockingQueue<MachineCode> machineCodeBlockingQueue;
    private final Queue<Pair<List<String>, MachineCode>> outputQueue;
    private String encryptedText;
    private final ExecutorService tasksPool;
    private long taskSize = 100;
    private EnigmaEngine enigmaEngine;
    private WordsDictionary wordsDictionary;
    private Difficulty difficulty;


    public TasksManager() {
        this.numberOfAgents = 10;
        this.machineCodeBlockingQueue = new ArrayBlockingQueue<>(BLOCKINGQUEUE_SIZE);
        this.outputQueue = new ConcurrentLinkedQueue<>();
        this.encryptedText = null;
        this.tasksPool = Executors.newFixedThreadPool(numberOfAgents);
    }

    public TasksManager(int numberOfAgents, String encryptedText) {
        this.numberOfAgents = numberOfAgents;
        this.machineCodeBlockingQueue = new ArrayBlockingQueue<>(BLOCKINGQUEUE_SIZE);
        this.outputQueue = new ConcurrentLinkedQueue<>();
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
        } else {
            System.out.println("DecryptionManager is dead");
        }
        for (int i = 0; i < numberOfAgents; i++) {
            tasksPool.execute(new Agent(i, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputQueue, taskSize));
        }

        tasksPool.shutdown();
        if (tasksPool.isShutdown()) {
            System.out.println("ExecutorService is shutdown");
        }
        new Thread(new CandidateWords(outputQueue)).start();

        return true;
    }

    public void test() {
        System.out.println("Starting TasksManager");
        DecryptionManager decryptionManager = new DecryptionManager(enigmaEngine, machineCodeBlockingQueue, difficulty, encryptedText, taskSize);
        decryptionManager.calculateCombinationNumber();
        decryptionManager.initializeMachineCode();
        Thread decryptionManagerThread = new Thread(decryptionManager);
        decryptionManagerThread.start();

        List<Future<?>> futures = new CopyOnWriteArrayList<>();

        for (int i = 0; i < numberOfAgents; i++) {
            futures.add(tasksPool.submit(new Agent(i, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputQueue, taskSize)));
        }


        Thread candidateWordsOutputThread = new Thread(new CandidateWords(outputQueue));
        candidateWordsOutputThread.start();

        tasksPool.shutdown();
        if (tasksPool.isShutdown()) {
            System.out.println("ExecutorService is shutdown");
        }


        while (!tasksPool.isTerminated()) {
            try {
                Thread.sleep(200);  //TODO: very bad practice
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        candidateWordsOutputThread.interrupt();
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

