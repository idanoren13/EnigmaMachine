package immutables;

public class AllyDTO {
    private final String allyName;
    private final int numberOfAgents;
    private final int missionSize;
    private final Boolean isAvailable;

    public AllyDTO(String allyName, int numberOfAgents, int missionSize, Boolean isAvailable) {
        this.allyName = allyName;
        this.numberOfAgents = numberOfAgents;
        this.missionSize = missionSize;
        this.isAvailable = isAvailable;
    }

    public String getAllyName() {
        return allyName;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public int getMissionSize() {
        return missionSize;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }
}
