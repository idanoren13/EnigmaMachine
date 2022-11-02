package battlefield;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Battlefield implements Serializable {
    private final BattlefieldInformation battlefieldInformation;
    private final List<Ally> Allies;

    public Battlefield(BattlefieldInformation battlefieldInformation) {
        this.battlefieldInformation = battlefieldInformation;
        this.Allies = new ArrayList<>();

    }
}
