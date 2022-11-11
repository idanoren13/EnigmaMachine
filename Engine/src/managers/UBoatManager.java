package managers;

import Entities.UBoatEntity;
import immutables.ContestDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UBoatManager implements Serializable {
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

    public void removeUBoat(String i_UBoatName) {
        UBoats.remove(i_UBoatName);
    }

    public List<ContestDTO> getAllContests() {
        List<ContestDTO> contestDTOS = new ArrayList<>();
        for (Map.Entry<String, UBoatEntity> entry : UBoats.entrySet()) {
            contestDTOS.add(entry.getValue().getContest());
        }
        
        return contestDTOS;
    }
}
