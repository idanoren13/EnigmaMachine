package automateDecryption;

import enigmaEngine.MachineCode;
import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class CandidateWords implements Runnable {
    private final BlockingQueue<Pair<List<String>, MachineCode>> outputQueue;

    public CandidateWords(BlockingQueue<Pair<List<String>, MachineCode>> outputQueue) {
        this.outputQueue = outputQueue;
    }

//    @Override
//    protected Pair<List<String>, MachineCode> call() throws Exception {
////        System.out.println("Candidate Words: " + outputQueue.peek().getKey());
//        return outputQueue.poll();
//    }

    @Override
    public void run() {
        while(true) {
            Pair<List<String>, MachineCode> pair = null;
            try {
                pair = outputQueue.poll(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (pair == null) {
                break;
            }
            System.out.println("Candidate Words: " + pair.getKey()+ "\nMachine Code: " + pair.getValue().toString());
        }
    }
}
