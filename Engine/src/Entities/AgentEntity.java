package Entities;

import java.io.Serializable;

public class AgentEntity  implements Serializable {
    private final String agentName;
    private final String allyName;

    public AgentEntity(String name, String allyName) {
        this.agentName = name;
        this.allyName = allyName;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAllyName() {
        return allyName;
    }
}
