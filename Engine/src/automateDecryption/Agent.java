package automateDecryption;

import enigmaEngine.MachineCode;
import enigmaEngine.WordsDictionary;
import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.interfaces.EnigmaEngine;
import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class Agent implements Runnable {

    private final int id;
    private final String encryptedText;
    private EnigmaEngine enigmaEngine;
    private final WordsDictionary wordsDictionary;
    private final BlockingQueue<MachineCode> machineCodeInputQueue;
    private final BlockingQueue<Pair<List<String>, MachineCode>> outputQueue;
    private final long taskSize;


//    public Agent(int id, String encryptedText, EnigmaEngine enigmaEngine, WordsDictionary wordsDictionary, BlockingQueue<MachineCode> queue, BlockingQueue<Pair<List<String>, MachineCode>> outputQueue, long taskSize) {
//        this.wordsDictionary = wordsDictionary;
//        this.outputQueue = outputQueue;
//        this.taskSize = taskSize;
//        this.id = id;
//        this.encryptedText = encryptedText;
//        this.enigmaEngine = enigmaEngine.cloneMachine();
//        this.machineCodeInputQueue = queue;
//
//    }

    public Agent(int id, EnigmaEngine enigmaEngine, BlockingQueue<MachineCode> machineCodeBlockingQueue, String encryptedText, BlockingQueue<Pair<List<String>, MachineCode>> outputQueue, long taskSize) {
        this.wordsDictionary = enigmaEngine.getWordsDictionary();
        this.outputQueue = outputQueue;
        this.taskSize = taskSize;
        this.id = id;
        this.encryptedText = encryptedText;
        this.enigmaEngine = enigmaEngine.cloneMachine();
        this.machineCodeInputQueue = machineCodeBlockingQueue;
//        try {
//            this.enigmaEngine.setEngineConfiguration(machineCodeBlockingQueue);
//        } catch (InvalidCharactersException | InvalidRotorException | InvalidReflectorException |
//                 InvalidPlugBoardException e) {
//            throw new RuntimeException(e);
//        }
    }


    //    @Override
    protected void call() throws Exception {
        while (true) {
            boolean isFound = false;
            MachineCode machineCode = machineCodeInputQueue.poll(5000, TimeUnit.MILLISECONDS);
            String processedText ="";
            if (machineCode == null) {
                break;
            }
            for (int i = 0; i < taskSize; i++) {
//            this.enigmaEngine.reset();
            this.enigmaEngine = enigmaEngine.cloneMachine();

                try {
                    this.enigmaEngine.setEngineConfiguration(machineCode);
                } catch (InvalidCharactersException | InvalidRotorException | InvalidReflectorException |
                         InvalidPlugBoardException ignored) {
                    System.out.println("Invalid machine code");
                }
//                System.out.println("Agent " + id + " is processing " + machineCode.toString());
                processedText = enigmaEngine.processMessage(encryptedText);
                if (wordsDictionary.isCandidateString(processedText)) {
                    outputQueue.put(new Pair<>(wordsDictionary.candidateWords(processedText), machineCode));
                    System.out.println("Agent " + Thread.currentThread().getName() + " found a candidate" + wordsDictionary.candidateWords(processedText));
                    isFound = true;
                    break;
                }
                machineCode.increment();

            }
            if(isFound){
                break;
            }
            System.out.println("Agent " + Thread.currentThread().getName() + " processed:" + processedText + " machine code:"+ machineCode.toString());
            sleep(200);
        }
    }

    @Override
    public void run() {
        try {
            call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
