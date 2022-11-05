package Entities;

import automateDecryption.DecryptionManager;
import enigmaEngine.MachineCode;
import immutables.AllyDTO;
import immutables.CandidateDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AllyEntity implements Serializable {
    private final String allyName;
    private final List<AgentEntity> agents;
    private final List<CandidateDTO> candidates;
    private Boolean isReady;
    private int missionSize;
    private DecryptionManager decryptionManager;
    BlockingQueue<MachineCode> MachineCodeDTOQueue;

    public AllyEntity(String allyName) {
        this.allyName = allyName;
        this.isReady = false;
        agents = new ArrayList<>();
        missionSize = 1000;
        candidates = new ArrayList<>();
    }

    public AllyDTO getAllyDTO() {
        return new AllyDTO(allyName, agents.size(), missionSize, !isReady);
    }

    public void addAgent(AgentEntity agent) {
        agents.add(agent);
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }

    public void setCandidates(CandidateDTO[] candidates) {
        Collections.addAll(this.candidates, candidates);
    }

    public MachineCode getMission(int missionSize) {
        synchronized (this) {
            MachineCode mission = MachineCodeDTOQueue.poll();

            return mission;
        }
    }

    public void setReady(Boolean isReady) {
        this.isReady = isReady;
    }

    public Boolean getIsReady() {
        return isReady;
    }
}
