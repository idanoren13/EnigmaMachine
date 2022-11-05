package Entities;

import immutables.AllyDTO;

import java.util.ArrayList;
import java.util.List;

public class AllyEntity {
    private final String allyName;
    private final List<AgentEntity> agents;
    private Boolean isReady;
    private int missionSize;

    public AllyEntity(String allyName) {
        this.allyName = allyName;
        this.isReady = false;
        agents = new ArrayList<>();
        missionSize = 1000;
    }

    public AllyDTO getAllyDTO() {
        return new AllyDTO(allyName,agents.size(), missionSize, !isReady);
    }

    public void addAgent(AgentEntity agent) {
        agents.add(agent);
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }
}
