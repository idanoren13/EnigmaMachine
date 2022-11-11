package Entities;

import automateDecryption.DecryptionManager;
import enigmaEngine.MachineCode;
import immutables.AllyDTO;
import immutables.CandidateDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AllyEntity implements Serializable {
    private final String allyName;

    private final List<AgentEntity> agents;
    private final List<CandidateDTO> candidates;
    private Boolean isReady;
    private int missionSize;
    private DecryptionManager decryptionManager;
    private BlockingQueue<MachineCode> machineCodeBlockingQueue;
    private String uBoatName;
    private UBoatEntity uBoatEntity;

    public AllyEntity(String allyName) {
        this.allyName = allyName;
        this.isReady = false;
        agents = new ArrayList<>();
        missionSize = 100;
        candidates = new ArrayList<>();
        machineCodeBlockingQueue = new ArrayBlockingQueue<>(1000);
    }

    public AllyDTO getAllyDTO() {
        return new AllyDTO(allyName, agents.size(), missionSize, !isReady);
    }

    public void addAgent(AgentEntity agent) {
        agents.add(agent);
    }

    public List<AgentEntity> getAgents() {
        return agents;
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }

    public void setCandidates(CandidateDTO[] candidates) {
        Collections.addAll(this.candidates, candidates);
    }

    public synchronized List<MachineCode> getMissions(int missionSize) {
        List<MachineCode> missions = new ArrayList<>();

        while (machineCodeBlockingQueue.size() < missionSize) {
            if (uBoatEntity.isContestEnded()) {
                return missions;
            }
        }

        machineCodeBlockingQueue.drainTo(missions, missionSize);
        return missions;
    }

    public void setReady(Boolean isReady) {
        this.isReady = isReady;
    }

    public Boolean getIsReady() {
        return isReady;
    }


    public void setUBoatName(String uBoatName) {
        this.uBoatName = uBoatName;
    }

    public String getUBoatName() {
        return uBoatName;
    }

    public void setDecryptionManager(DecryptionManager decryptionManager) {
        this.decryptionManager = decryptionManager;
    }

    public int getMissionSize() {
        return missionSize;
    }

    public BlockingQueue<MachineCode> getMachineCodeBlockingQueue() {
        return machineCodeBlockingQueue;
    }

    public void startContest() {

        Thread startContestThread = new Thread(decryptionManager);
        startContestThread.start();
    }

    public void setUBoatEntity(UBoatEntity uBoatEntity) {
        this.uBoatEntity = uBoatEntity;
    }
}
