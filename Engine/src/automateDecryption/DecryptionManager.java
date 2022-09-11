package automateDecryption;

import enigmaEngine.MachineCodeDTO;
import enigmaEngine.interfaces.EnigmaEngine;
import immutables.engine.EngineDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class DecryptionManager implements Runnable {

    private final EnigmaEngine enigmaEngine;
    private double combination;
    BlockingQueue<MachineCodeDTO> queue;
    private final Difficulty difficulty;
    private MachineCodeDTO machineCode;

    public DecryptionManager(EnigmaEngine enigmaEngine, BlockingQueue<MachineCodeDTO> MachineCodeDTOQueue, Difficulty difficulty) {
        this.enigmaEngine = enigmaEngine;
        this.queue = MachineCodeDTOQueue;
        this.difficulty = difficulty;
    }

    public void calculateCombinationNumber() {
        EngineDTO engineDTO = enigmaEngine.getEngineDTO();
        int ABCSize = enigmaEngine.getABCSize();
        switch (difficulty) {
            case EASY:
                combination = Math.pow(ABCSize, engineDTO.getSelectedRotors().size());
                break;
            case MEDIUM:
                combination = 100000000;
                break;
            case HARD:
                combination = 10000000000.0;
                break;
            case IMPOSSIBLE:
                combination = 1000000000.0;
                break;
            default:
        }
    }

    public void initializeMachineCode() {
        MachineCodeDTO tempMachineCode = this.enigmaEngine.getMachineCodeDTO();
        switch (difficulty) {
            case EASY:
                machineCode = new MachineCodeDTO(tempMachineCode.getRotorsIDInorder(), setStartingPositions(tempMachineCode.getStartingPositions()), tempMachineCode.getSelectedReflectorID(), tempMachineCode.getPlugBoard());
                break;
            case MEDIUM:
                machineCode = tempMachineCode;
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
//                advanceMachineCodeMedium();
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

    private void advanceMachineCodeEasy() {
        List<Character> startingPositions = machineCode.getStartingPositions();
        int ABCSize = enigmaEngine.getABCSize();
        int i = startingPositions.size() - 1;
        while (i >= 0) {
            if (startingPositions.get(i) == enigmaEngine.getABC().charAt(ABCSize - 1)) {
                startingPositions.set(i, enigmaEngine.getABC().charAt(0));
                i--;
            } else {
                startingPositions.set(i, enigmaEngine.getABC().charAt(enigmaEngine.getABC().indexOf(startingPositions.get(i)) + 1));
                break;
            }
        }

        machineCode = new MachineCodeDTO(machineCode.getRotorsIDInorder(), startingPositions, machineCode.getSelectedReflectorID(), machineCode.getPlugBoard());
    }

    @Override
    public void run() {
        try {
            if (queue.offer(machineCode)) {
                advanceMachineCode();
                combination--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
