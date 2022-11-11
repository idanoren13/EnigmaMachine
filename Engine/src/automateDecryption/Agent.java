package automateDecryption;

import enigmaEngine.MachineCode;
import enigmaEngine.WordsDictionary;
import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.interfaces.EnigmaEngine;
import javafx.application.Platform;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Agent implements Runnable, Serializable {

    private final int id;
    private final String encryptedText;
    private EnigmaEngine enigmaEngine;
    private final WordsDictionary wordsDictionary;
    private final BlockingQueue<MachineCode> machineCodeInputQueue;
    private final Queue<Pair<List<String>, MachineCode>> outputQueue;
    private final Consumer<Integer> onProgressChanged;
    private final long taskSize;


    public Agent(int id, EnigmaEngine enigmaEngine, BlockingQueue<MachineCode> machineCodeBlockingQueue, String encryptedText, Queue<Pair<List<String>, MachineCode>> outputQueue, long taskSize, Consumer<Integer> onProgressChanged) {
        this.wordsDictionary = enigmaEngine.getWordsDictionary();
        this.outputQueue = outputQueue;
        this.taskSize = taskSize;
        this.id = id;
        this.encryptedText = encryptedText;
        this.enigmaEngine = enigmaEngine.deepClone();
        this.machineCodeInputQueue = machineCodeBlockingQueue;
        this.onProgressChanged = onProgressChanged;

    }


    //    @Override
    protected void call() throws Exception {
        while (true) {
            boolean isFound = false;
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

                if (machineCode.getStartingPositions().get(0) == Test.firstRotorStartingPosition
                        && machineCode.getStartingPositions().get(1) == Test.secondRotorStartingPosition
                        && machineCode.getStartingPositions().get(2) == Test.thirdRotorStartingPosition
                        && machineCode.getSelectedReflectorID() == Test.selectedReflectorID) {

//                    System.out.println("Agent " + id + " found the solution " + " " + machineCode.toString());

                    isFound = true;
//                    break;
                }
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
}
