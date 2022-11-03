package automateDecryption;

import enigmaEngine.MachineCode;
import enigmaEngine.WordsDictionary;
import enigmaEngine.interfaces.EnigmaEngine;
import immutables.engine.Difficulty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TasksManager extends Task<Boolean> {
    volatile boolean halt = false;
    private static final int MIN_NUMBER_OF_AGENTS = 2;
    private static final int MAX_NUMBER_OF_AGENTS = 50;
    private static final int BLOCKINGQUEUE_SIZE = 50;

    private long totalMissions = -1;

    private int numberOfAgents;
    private final BlockingQueue<MachineCode> machineCodeBlockingQueue;
    private final Queue<Pair<List<String>, MachineCode>> outputQueue;
    private String encryptedText;
    private final ExecutorService tasksPool;
    private long taskSize = 100;
    private EnigmaEngine enigmaEngine;
    private WordsDictionary wordsDictionary;
    private Difficulty difficulty;
    private Consumer<Runnable> onCancel;
    private Consumer<String> onCandidateWordsFound;
    private Consumer<? extends Number> onProgressChanged;
    private SimpleDoubleProperty progressProperty;

    Thread decryptionManagerThread;
    Thread candidateWordsOutputThread;
    private boolean paused = false;
    private Object pauseLock = new Object();

    private PropertyChangeSupport progress = new PropertyChangeSupport(this);

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
        progressProperty = new SimpleDoubleProperty(0);
    }

    @Override
    protected Boolean call() throws Exception {
        System.out.println("Starting TasksManager");
        DecryptionManager decryptionManager = new DecryptionManager(enigmaEngine, machineCodeBlockingQueue, difficulty, encryptedText, taskSize);
        decryptionManager.initializeMachineCode();
        this.totalMissions = decryptionManager.getTotalMissions();
        decryptionManagerThread = new Thread(decryptionManager);
        decryptionManagerThread.start();



        List<Future<?>> futures = new CopyOnWriteArrayList<>();

        for (int i = 0; i < numberOfAgents; i++) {
            futures.add(tasksPool.submit(new Agent(i, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputQueue, taskSize, (Consumer<Integer>) onProgressChanged)));
        }

        candidateWordsOutputThread = new Thread(new CandidateWords(outputQueue, onCandidateWordsFound));
        candidateWordsOutputThread.start();

        tasksPool.shutdown();
        if (tasksPool.isShutdown()) {
            System.out.println("ExecutorService is shutdown");
        }

        while (!tasksPool.isTerminated()) {
            try {
                Thread.sleep(1);  //TODO: very bad practice
            } catch (InterruptedException e) {
                halt = true;
                System.out.println("Stopped");
            }
        }

        candidateWordsOutputThread.interrupt();
        return true;
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
        return paused;
    }

    public void setOnCandidateWordsFound(Consumer<String> onCandidateWordsFound) {
        this.onCandidateWordsFound = onCandidateWordsFound; }

    public void setOnProgressChanged(Consumer<? extends Number> onProgressChanged) {
        this.onProgressChanged = onProgressChanged;
    }

    public void stop() {
        halt = true;
        decryptionManagerThread.interrupt();
        candidateWordsOutputThread.interrupt();
        tasksPool.shutdownNow();
        resetTaskManager();
        System.out.println("TasksManager stopped");
    }

    private void resetTaskManager() {
        this.machineCodeBlockingQueue.clear();
        this.outputQueue.clear();
        this.encryptedText = null;
//        this.tasksPool.shutdownNow();
        this.totalMissions = 0;
        this.progressProperty.set(0);
    }

    public ObservableValue<? extends Number> getProgressProperty() {
        return progressProperty;
    }
}