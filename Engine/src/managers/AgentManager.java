package managers;

import Entities.AgentEntity;

import java.util.HashMap;
import java.util.Map;

public class AgentManager {
    private final Map<String, AgentEntity> agents;

    public AgentManager() {
        agents = new HashMap<>();
    }

    public void addAlly(String allyName) {
        agents.put(allyName, new AgentEntity(allyName));
    }

    public AgentEntity getAlly(String allyName) {
        return agents.get(allyName);
    }
}
