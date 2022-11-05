package managers;

import Entities.AgentEntity;

import java.util.HashMap;
import java.util.Map;

public class AgentManager {
    private final Map<String, AgentEntity> agents;

    public AgentManager() {
        agents = new HashMap<>();
    }

    public void addAgent(AgentEntity agent) {
        agents.put(agent.getAgentName(), agent);
    }

    public AgentEntity getAgent(String allyName) {
        return agents.get(allyName);
    }
}
