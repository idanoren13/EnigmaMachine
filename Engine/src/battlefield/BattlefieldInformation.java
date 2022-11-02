package battlefield;

import enigmaEngine.schemaBinding.CTEBattlefield;
import immutables.engine.Difficulty;

public class BattlefieldInformation {
    private String name;
    private String uBoatName;
    private int numberOfAllies;
    private int currentNumberOfAllies;
    private Difficulty difficulty;

    public static BattlefieldInformation loadBattlefieldFromXML(CTEBattlefield battlefield) {
        BattlefieldInformation battlefieldInformation = new BattlefieldInformation();
        battlefieldInformation.name = battlefield.getBattleName();
        battlefieldInformation.numberOfAllies = battlefield.getAllies();
        battlefieldInformation.difficulty = Difficulty.valueOf(battlefield.getLevel().toUpperCase());
        battlefieldInformation.currentNumberOfAllies = 0;

        return battlefieldInformation;
    }

    public String getName() {
        return name;
    }

    public String getuBoatName() {
        return uBoatName;
    }

    public int getNumberOfAllies() {
        return numberOfAllies;
    }

    public int getCurrentNumberOfAllies() {
        return currentNumberOfAllies;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setuBoatName(String uBoatName) {
        this.uBoatName = uBoatName;
    }
}
