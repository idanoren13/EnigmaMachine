package automateDecryption;

import enigmaEngine.MachineCode;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.Reflector;
import immutables.engine.EngineDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class DecryptionManager implements Runnable {

    private final EnigmaEngine enigmaEngine;
    private long totalMissions, currentProgress;
    private final BlockingQueue<MachineCode> queue;
    //    private final BlockingQueue<Runnable> taskPoolQueue;
    private final Difficulty difficulty;
    private final String encryptedText;
    private MachineCode machineCode;
//    private final ExecutorService executor;

    private long taskSize;

    public DecryptionManager(EnigmaEngine enigmaEngine, BlockingQueue<MachineCode> MachineCodeDTOQueue, Difficulty difficulty, String encryptedText, long taskSize) {
        this.enigmaEngine = enigmaEngine;
        this.queue = MachineCodeDTOQueue;
//        this.taskPoolQueue = taskPoolQueue;
        this.difficulty = difficulty;
        this.encryptedText = encryptedText;
//        this.executor = executor;
        this.taskSize = taskSize;
        currentProgress = 0;

        calculateMissionsNumber();
    }

    private void calculateMissionsNumber() {
        EngineDTO engineDTO = enigmaEngine.getEngineDTO();
        int ABCSize = enigmaEngine.getABCSize();
        switch (difficulty) {
            case EASY:
                totalMissions = (long) Math.pow(ABCSize, engineDTO.getSelectedRotors().size());
                break;
            case MEDIUM:
                totalMissions = (long) Math.pow(ABCSize, engineDTO.getSelectedRotors().size()) * engineDTO.getTotalReflectors();
                break;
            case HARD:
                totalMissions = (long) Math.pow(ABCSize, engineDTO.getSelectedRotors().size()) * engineDTO.getTotalReflectors() * factorial(engineDTO.getSelectedRotors().size());
                break;
            case IMPOSSIBLE:
                totalMissions = 1_000_000_000_000L;
                break;
            default:
        }
    }

    private long factorial(int size) {
        long result = 1;
        for (int i = 1; i <= size; i++) {
            result *= i;
        }
        return result;
    }

    public void initializeMachineCode() {
        MachineCode tempMachineCode = this.enigmaEngine.getMachineCodeDTO();
        switch (difficulty) {
            case EASY:
                machineCode = new MachineCode(tempMachineCode.getRotorsIDInorder(), setStartingPositions(tempMachineCode.getStartingPositions()), tempMachineCode.getSelectedReflectorID(), tempMachineCode.getPlugBoard(), enigmaEngine.getABC());
                break;
            case MEDIUM:
                machineCode = new MachineCode(tempMachineCode.getRotorsIDInorder(), setStartingPositions(tempMachineCode.getStartingPositions()), Reflector.ReflectorID.I, tempMachineCode.getPlugBoard(), enigmaEngine.getABC());
                break;
            case HARD:
                machineCode = tempMachineCode;
                break;
            case IMPOSSIBLE:
                machineCode = tempMachineCode;
                break;
            default:
        }
    }

    private List<Character> setStartingPositions(List<Character> tempStartingPositions) {
        List<Character> res = new ArrayList<>();
        for (int i = 0; i < tempStartingPositions.size(); i++) {
            res.add(enigmaEngine.getABC().charAt(0));
        }

        return res;
    }


    private void advanceMachineCode() {
        switch (difficulty) {
            case EASY:
                advanceMachineCodeEasy();
                break;
            case MEDIUM:
                advanceMachineCodeMedium();
                break;
            case HARD:
//                advanceMachineCodeHard();
                break;
            case IMPOSSIBLE:
//                advanceMachineCodeImpossible();
                break;
            default:
        }
    }

    private void advanceMachineCodeMedium() {

    }

    synchronized private void advanceMachineCodeEasy() {
        for (int i = 0; i < taskSize; i++) {
            machineCode.increment();
            currentProgress++;
            // totalMissions--;
        }
    }

    @Override
    public void run() {
        while (currentProgress < totalMissions) { // (0 < totalMissions) {
//            System.out.println("Combination: " + combination);
//            System.out.println("MachineCode: " + machineCode.getStartingPositions());
//            System.out.println("Thread: " + Thread.currentThread().getName());
            if(queue.offer(machineCode)) {
                advanceMachineCode();
            }
        }
    }

    public long getTotalMissions() {
        return totalMissions;
    }
}
