package battlefield;

import Entities.AllyEntity;
import automateDecryption.DecryptionManager;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.schemaBinding.CTEBattlefield;
import immutables.AllyDTO;
import immutables.Difficulty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Battlefield implements Serializable {
    private final BattlefieldInformation battlefieldInformation;
    private final List<AllyEntity> allies;

    public Battlefield(CTEBattlefield cteBattlefield) {
        this.battlefieldInformation = BattlefieldInformation.loadBattlefieldFromXML(cteBattlefield);
        this.allies = new ArrayList<>();

    }

    public BattlefieldInformation getBattlefieldInformation() {
        return battlefieldInformation;
    }

    public void addAlly(AllyEntity ally) {
        allies.add(ally);
        battlefieldInformation.incrementCurrentNumberOfAllies();
    }

    public List<AllyDTO> getAllies() {
        List<AllyDTO> allyDTOList = new ArrayList<>();
        for (AllyEntity ally : allies) {
            allyDTOList.add(ally.getAllyDTO());
        }
        return allyDTOList;
    }

    public Boolean isAlliesReady() {
        for (AllyEntity ally : allies) {
            if (!ally.getIsReady()) {
                return false;
            }
        }

        return true;
    }


    public Difficulty getDifficulty() {
        return battlefieldInformation.getDifficulty();
    }

    public void startContest(EnigmaEngine enigmaEngine, String encryptedMessage) {
        for (AllyEntity ally : allies) {
            ally.setDecryptionManager(new DecryptionManager(
                    enigmaEngine.deepClone(),
                    ally.getMachineCodeBlockingQueue(),
                    battlefieldInformation.getDifficulty(),
                    encryptedMessage,
                    ally.getMissionSize()));
            ally.startContest();
        }

    }
}
