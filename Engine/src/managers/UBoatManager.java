package managers;

import Entities.UBoatEntity;

import java.util.HashMap;
import java.util.Map;

public class UBoatManager {
    private final Map<String, UBoatEntity> UBoats = new HashMap<>();

    public void addUBoat(String uBoatName, UBoatEntity uBoatEntity) {
        UBoats.put(uBoatName, uBoatEntity);
        uBoatEntity.setUBoatName(uBoatName);
    }

    public UBoatEntity getUBoat(String i_UBoatName) {
        return UBoats.get(i_UBoatName);
    }

    public boolean uBoatIsNotExists(String name) {
        return !UBoats.containsKey(name);
    }
}
