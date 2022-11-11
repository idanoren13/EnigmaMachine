package managers;

import Entities.AgentEntity;
import Entities.AllyEntity;
import immutables.AllyDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlliesManager implements Serializable {

    private final Map<String, AllyEntity> allies;

    public AlliesManager() {
        allies = new HashMap<>();
    }

    public void addAlly(String allyName) {
        allies.put(allyName, new AllyEntity(allyName));
    }

    public AllyEntity getAlly(String allyName) {
        return allies.get(allyName);
    }

    public List<AllyDTO> getAllies() {
        List<AllyDTO> allyDTOList = new ArrayList<>();
        for (AllyEntity ally : allies.values()) {
            allyDTOList.add(ally.getAllyDTO());
        }
        return allyDTOList;
    }

    public void addAgentToAlly(String allyName, AgentEntity agent) {
        AllyEntity ally = allies.get(allyName);
        ally.addAgent(agent);
    }
}
