package frontEnd.controllers;

import automateDecryption.CandidateWords;
import enigmaEngine.MachineCode;
import enigmaEngine.WordsDictionary;
import enigmaEngine.interfaces.EnigmaEngine;
import immutables.Difficulty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.Pair;

import java.beans.PropertyChangeSupport;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TaskController extends Thread implements Closeable {

    volatile boolean halt = false;
    private static final int BLOCKINGQUEUE_SIZE = 50000;

    private long totalMissions = -1;

    private int numberOfAgents;
    private BlockingQueue<MachineCode> machineCodeBlockingQueue;
    private Queue<Pair<List<String>, MachineCode>> outputQueue;
    private String encryptedText;
    private ExecutorService tasksPool;
    private long taskSize = 100;
    private EnigmaEngine enigmaEngine;
    private WordsDictionary wordsDictionary;
    private Difficulty difficulty;
    private Consumer<String> onCandidateWordsFound;
    private Consumer<Integer> onProgressChanged;
    private SimpleDoubleProperty progressProperty;
    Thread candidateWordsOutputThread;

    private final Object fillQueueLock = new Object();
    private AgentAppController agentAppController;

    private PropertyChangeSupport progress = new PropertyChangeSupport(this);
    private Timer ContestStatusTimer;
    private Boolean isContestActive = false;


    public TaskController(int numberOfAgents, AgentAppController agentAppController) {
        this.numberOfAgents = numberOfAgents;
        this.machineCodeBlockingQueue = new ArrayBlockingQueue<>(agentAppController.getMissionSize());
        this.outputQueue = new ConcurrentLinkedQueue<>();
        tasksPool = Executors.newFixedThreadPool(numberOfAgents);
        this.agentAppController = agentAppController;
    }

    public TaskController() {
        this.numberOfAgents = 1;
        this.machineCodeBlockingQueue = new ArrayBlockingQueue<>(1000);
        this.outputQueue = new ConcurrentLinkedQueue<>();
        tasksPool = Executors.newFixedThreadPool(numberOfAgents);
    }

//    public frontEnd.controllers.TaskController() {
//    }

    public void initialize(Difficulty difficulty, String encryptedText) {
        this.encryptedText = encryptedText;

        this.difficulty = difficulty;
        System.out.println("Number of agents: " + numberOfAgents);
        progressProperty = new SimpleDoubleProperty(0);
    }

    public void setEnigmaEngine(EnigmaEngine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        this.wordsDictionary = enigmaEngine.getWordsDictionary();

    }

    @Override
    public void run() {
//        System.out.println("Starting TasksManager");
//        DecryptionManager decryptionManager = new DecryptionManager(enigmaEngine, machineCodeBlockingQueue, difficulty, encryptedText, taskSize);
//        decryptionManager.initializeMachineCode();
//        this.totalMissions = decryptionManager.getTotalMissions();
//        decryptionManagerThread = new Thread(decryptionManager);
//        decryptionManagerThread.start();

        List<Future<?>> futures = new CopyOnWriteArrayList<>();

        for (int i = 0; i < numberOfAgents; i++) {
            futures.add(tasksPool.submit(new AgentRunner(i, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputQueue, taskSize, onProgressChanged, agentAppController)));
        }

        candidateWordsOutputThread = new Thread(new CandidateWords(outputQueue, onCandidateWordsFound));
        candidateWordsOutputThread.start();

        tasksPool.shutdown();

//        while (isContestActive) {
//            sendCandidates();
//            getMissionFromServer();
//        }

        while (!tasksPool.isTerminated()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                halt = true;
                System.out.println("Stopped");
            }
        }

        candidateWordsOutputThread.interrupt();
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

    public ObservableValue<? extends Number> getProgressProperty() {
        return progressProperty;
    }

    @Override
    public void close() throws IOException {
        this.machineCodeBlockingQueue.clear();
        this.outputQueue.clear();
        this.encryptedText = null;
//        this.tasksPool.shutdownNow();
        this.totalMissions = 0;
        this.progressProperty.set(0);
    }
}
