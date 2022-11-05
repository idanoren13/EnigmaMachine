package managers;

import Entities.AllyEntity;

import java.util.HashMap;
import java.util.Map;

public class AlliesManager {

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
}
