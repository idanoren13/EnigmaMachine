package frontEnd.controllers;

import automateDecryption.Agent;
import automateDecryption.CandidateWords;
import com.google.gson.Gson;
import enigmaEngine.EnigmaMachineFromXML;
import enigmaEngine.MachineCode;
import enigmaEngine.WordsDictionary;
import enigmaEngine.interfaces.EnigmaEngine;
import immutables.CandidateDTO;
import immutables.Difficulty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import javax.xml.bind.JAXBException;
import java.beans.PropertyChangeSupport;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static utils.Constants.GET_MISSION;
import static utils.Constants.SEND_CANDIDATES;

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

//    public frontEnd.controllers.TaskController() {
//    }

    public void initialize(InputStream xml, Difficulty difficulty, String encryptedText) {
        this.encryptedText = encryptedText;

        try {
            this.enigmaEngine = new EnigmaMachineFromXML().getEnigmaEngineFromInputStream(xml);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        this.wordsDictionary = enigmaEngine.getWordsDictionary();
        this.difficulty = difficulty;
        System.out.println("Number of agents: " + numberOfAgents);
        progressProperty = new SimpleDoubleProperty(0);
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
            futures.add(tasksPool.submit(new Agent(i, enigmaEngine, machineCodeBlockingQueue, encryptedText, outputQueue, taskSize, onProgressChanged)));
        }

        candidateWordsOutputThread = new Thread(new CandidateWords(outputQueue, onCandidateWordsFound));
        candidateWordsOutputThread.start();

        tasksPool.shutdown();

        while (isContestActive) {
            sendCandidates();
            getMissionFromServer();
        }

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

    private void getMissionFromServer() {
        String url = HttpUrl.parse(GET_MISSION).newBuilder()
                .addQueryParameter("allyName", agentAppController.getAllyName())
                .addQueryParameter("missionSize", String.valueOf(agentAppController.getMissionSize()))
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to join contest").showAndWait();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    Gson gson = new Gson();
                    MachineCode machineCode = gson.fromJson(responseString, MachineCode.class);
                    fillQueue(machineCode);
                }
            }
        });
    }

    private void fillQueue(MachineCode machineCode) {
//        synchronized (fillQueueLock) {
        for (int i = 0; i < agentAppController.getMissionSize(); i++) {
            try {
                machineCodeBlockingQueue.put(machineCode);
                machineCode.increment();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        }
    }

    private void sendCandidates() {
        Gson gson = new Gson();
        List<Pair<List<String>, MachineCode>> candidates = new ArrayList<>(outputQueue);
        agentAppController.showCandidates(candidates);
        List<CandidateDTO> candidateDTOS = new ArrayList<>();
        for (Pair<List<String>, MachineCode> candidate : candidates) {
            candidateDTOS.add(new CandidateDTO(String.join(" ", candidate.getKey()), candidate.getValue()));
        }
        String url = HttpUrl.parse(SEND_CANDIDATES).newBuilder()
                .addQueryParameter("agentName", agentAppController.getAgentName())
                .addQueryParameter("allyName", agentAppController.getAllyName())
                .addQueryParameter("candidates", gson.toJson(candidateDTOS))
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to join contest").showAndWait();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                }
            }
        });
    }
}
