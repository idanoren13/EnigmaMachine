package immutables;

import java.io.Serializable;

public class AgentDataDTO implements Serializable {
    private final String name;
    private final Long totalMissions;
    private final Long totalFinished;
    private final Integer candidatesDelivered;
    private final String progress;


    public AgentDataDTO(String name, Long totalMissions, Long totalFinished, Integer candidatesDelivered) {
        this.name = name;
        this.totalMissions = totalMissions;
        this.totalFinished = totalFinished;
        this.candidatesDelivered = candidatesDelivered;
        this.progress = String.format("%d/%d", totalFinished, totalMissions);
    }

    public String getName() {
        return name;
    }

    public Long getTotalMissions() {
        return totalMissions;
    }

    public Long getTotalFinished() {
        return totalFinished;
    }

    public Integer getCandidatesDelivered() {
        return candidatesDelivered;
    }

    public String getProgress() {
        return progress;
    }
}
