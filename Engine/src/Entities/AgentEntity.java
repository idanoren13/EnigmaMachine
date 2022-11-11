package Entities;

import immutables.CandidateDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AgentEntity  implements Serializable {
    private final String agentName;
    private final String allyName;
    private Long totalMissions;
    private Long totalFinished;
    private Integer candidatesDelivered;
    private List<CandidateDTO> candidatesDTO;

    public AgentEntity(String name, String allyName) {
        this.agentName = name;
        this.allyName = allyName;
        this.candidatesDTO = new ArrayList<>();
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAllyName() {
        return allyName;
    }

    public List<CandidateDTO> getCandidatesDTO() {
        return candidatesDTO;
    }

    public Integer getCandidatesDelivered() {
        return candidatesDelivered;
    }

    public void setCandidatesDelivered(Integer candidatesDelivered) {
        this.candidatesDelivered = candidatesDelivered;
    }

    public Long getTotalFinished() {
        return totalFinished;
    }

    public void setTotalFinished(Long totalFinished) {
        this.totalFinished = totalFinished;
    }

    public Long getTotalMissions() {
        return totalMissions;
    }

    public void setTotalMissions(Long totalMissions) {
        this.totalMissions = totalMissions;
    }

    public Collection<? extends CandidateDTO> getCandidates() {
        return candidatesDTO;
    }
}
