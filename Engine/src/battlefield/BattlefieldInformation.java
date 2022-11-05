package battlefield;

import enigmaEngine.schemaBinding.CTEBattlefield;
import immutables.Difficulty;

public class BattlefieldInformation {
    private String name;
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

    public int getNumberOfAllies() {
        return numberOfAllies;
    }

    public int getCurrentNumberOfAllies() {
        return currentNumberOfAllies;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void incrementCurrentNumberOfAllies(){
        currentNumberOfAllies++;
    }

}
