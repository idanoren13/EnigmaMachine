package automateDecryption;

import enigmaEngine.MachineCode;
import enigmaEngine.interfaces.EnigmaEngine;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TasksManager extends Task<Boolean> {
    private static final int MIN_NUMBER_OF_AGENTS = 2;
    private static final int MAX_NUMBER_OF_AGENTS = 50;
    private static final int BLOCKINGQUEUE_SIZE = 50;

    private int numberOfAgents;
    private final BlockingQueue<MachineCode> machineCodeBlockingQueue;
    private final Queue<Pair<List<String>, MachineCode>> outputQueue;
    private String encryptedText;
    private final ExecutorService tasksPool;
    private long taskSize = 30;
    private EnigmaEngine enigmaEngine;
    private EnigmaEngine enigmaEngineClone;
    private Difficulty difficulty;
    private boolean isFinished = false;
    private Consumer<String> onCandidateWordsFound;
    private Thread decryptionManagerThread;
    private Thread candidateWordsOutputThread;

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
        this.difficulty = difficulty;
        System.out.println("Number of agents: " + numberOfAgents);
    }

    @Override
    protected Boolean call() throws Exception {
        enigmaEngineClone = enigmaEngine.cloneMachine();
        System.out.println("Enigma engine clone: " + enigmaEngine.processMessage(encryptedText) + " " + enigmaEngine.getMachineCode().toString());
        enigmaEngineClone.reset();
        isFinished = false;
        System.out.println("Starting TasksManager");
        DecryptionManager decryptionManager = new DecryptionManager(enigmaEngine, machineCodeBlockingQueue, difficulty, encryptedText, taskSize);
        decryptionManager.initializeMachineCode();
        decryptionManagerThread = new Thread(decryptionManager);
        decryptionManagerThread.start();

        updateProgress(0, decryptionManager.getTotalMissions());

        List<Future<?>> futures = new CopyOnWriteArrayList<>();

        for (int i = 0; i < numberOfAgents; i++) {
            futures.add(tasksPool.submit(new Agent(i, enigmaEngineClone.cloneMachine(), machineCodeBlockingQueue, encryptedText, outputQueue, taskSize)));
        }


        candidateWordsOutputThread = new Thread(new CandidateWords(outputQueue, onCandidateWordsFound));
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

        isFinished = true;

        return true;
    }

    public void test() {
//        try {
//            enigmaEngineClone = enigmaEngine.cloneMachine();
//            System.out.println("Enigma clone: "+ enigmaEngineClone.getMachineCodeDTO().toString());
//            System.out.println("Enigma engine: "+ enigmaEngine.getMachineCodeDTO().toString());
//            enigmaEngineClone.reset();
//            System.out.println("Enigma engine clone: " + enigmaEngineClone.processMessage(encryptedText) + " " + enigmaEngineClone.getMachineCodeDTO().toString());
//        } catch (InvalidCharactersException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println("Starting TasksManager");
        DecryptionManager decryptionManager = new DecryptionManager(enigmaEngine, machineCodeBlockingQueue, difficulty, encryptedText, taskSize);
        decryptionManager.initializeMachineCode();
        decryptionManagerThread = new Thread(decryptionManager);
        decryptionManagerThread.start();

        List<Future<?>> futures = new CopyOnWriteArrayList<>();

        for (int i = 0; i < numberOfAgents; i++) {
            futures.add(tasksPool.submit(new Agent(i, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputQueue, taskSize)));
        }


        Thread candidateWordsOutputThread = new Thread(new CandidateWords(outputQueue, onCandidateWordsFound));
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

    public void setOnCandidateWordsFound(Consumer<String> onCandidateWordsFound) {
        this.onCandidateWordsFound = onCandidateWordsFound;
    }

    public void stop() {
        tasksPool.shutdownNow();
        decryptionManagerThread.interrupt();
        candidateWordsOutputThread.interrupt();
        initialize(enigmaEngine, difficulty);
        System.out.println("TasksManager stopped");
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }
}