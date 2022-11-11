package frontEnd.controllers;

import com.google.gson.Gson;
import enigmaEngine.MachineCode;
import enigmaEngine.WordsDictionary;
import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.interfaces.EnigmaEngine;
import immutables.CandidateDTO;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static utils.Constants.GET_MISSIONS;
import static utils.Constants.SEND_CANDIDATES;

public class AgentRunner implements Runnable {

    private final int id;
    private final String encryptedText;
    private EnigmaEngine enigmaEngine;
    private final WordsDictionary wordsDictionary;
    private final BlockingQueue<MachineCode> machineCodeInputQueue;
    private final Queue<Pair<List<String>, MachineCode>> outputQueue;
    private final Consumer<Integer> onProgressChanged;
    private final long taskSize;
    private AgentAppController agentAppController;


    public AgentRunner(int id, EnigmaEngine enigmaEngine, BlockingQueue<MachineCode> machineCodeBlockingQueue, String encryptedText, Queue<Pair<List<String>, MachineCode>> outputQueue, long taskSize, Consumer<Integer> onProgressChanged, AgentAppController agentAppController) {
        this.wordsDictionary = enigmaEngine.getWordsDictionary();
        this.outputQueue = outputQueue;
        this.taskSize = taskSize;
        this.id = id;
        this.encryptedText = encryptedText;
        this.enigmaEngine = enigmaEngine.deepClone();
        this.machineCodeInputQueue = machineCodeBlockingQueue;
        this.onProgressChanged = onProgressChanged;
        this.agentAppController = agentAppController;

    }


    //    @Override
    protected void call() throws Exception {
        while (true) {
            boolean isFound = false;
            if (machineCodeInputQueue.isEmpty()) {
                sendCandidates();
                getMissionFromServer();
            }
            MachineCode machineCode = machineCodeInputQueue.poll(2000, TimeUnit.MILLISECONDS);
            String processedText = "";
            if (machineCode == null) {
                break;
            }
            for (int i = 0; i < taskSize; i++) {
                try {
                    this.enigmaEngine.setEngineConfiguration(machineCode);
                } catch (InvalidCharactersException | InvalidRotorException | InvalidReflectorException |
                         InvalidPlugBoardException ignored) {
                    System.out.println("Invalid machine code");
                }
                System.out.println("Agent " + id + " is processing " + machineCode.toString());

                enigmaEngine.reset();
                enigmaEngine.setEngineConfiguration(machineCode);
                processedText = enigmaEngine.processMessage(encryptedText);

                if (wordsDictionary.isCandidateString(processedText)) {
                    outputQueue.add(new Pair<>(wordsDictionary.candidateWords(processedText), machineCode));
                    System.out.println("Agent " + id + " found a candidate " + processedText + " " + machineCode.toString());
                }


                machineCode.increment();
                Platform.runLater(() -> {
                    onProgressChanged.accept(1);
                });
                enigmaEngine.reset();
            }
        }
    }

    private void getMissionFromServer() {
        String url = HttpUrl.parse(GET_MISSIONS).newBuilder()
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
                machineCodeInputQueue.put(machineCode);
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
            candidateDTOS.add(new CandidateDTO(
                    String.join(" ", candidate.getKey()),
                    candidate.getValue(),
                    agentAppController.getAgentName()));
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

    @Override
    public void run() {
        try {
            call();
        } catch (InterruptedException e) {
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAgentAppController(AgentAppController agentAppController) {
        this.agentAppController = agentAppController;
    }
}
