package battlefield;

import Entities.AllyEntity;
import enigmaEngine.schemaBinding.CTEBattlefield;
import immutables.AllyDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Battlefield implements Serializable {
    private final BattlefieldInformation battlefieldInformation;
    private final List<AllyEntity> Allies;

    public Battlefield(CTEBattlefield cteBattlefield) {
        this.battlefieldInformation = BattlefieldInformation.loadBattlefieldFromXML(cteBattlefield);
        this.Allies = new ArrayList<>();

    }

    public BattlefieldInformation getBattlefieldInformation() {
        return battlefieldInformation;
    }

    public void addAlly(AllyEntity ally) {
        Allies.add(ally);
        battlefieldInformation.incrementCurrentNumberOfAllies();
    }

    public List<AllyDTO> getAllies() {
        List<AllyDTO> allyDTOList = new ArrayList<>();
        for (AllyEntity ally : Allies) {
            allyDTOList.add(ally.getAllyDTO());
        }
        return allyDTOList;
    }
}
