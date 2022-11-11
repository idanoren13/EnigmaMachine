package immutables;

import java.io.Serializable;

public class ContestDTO implements Serializable {
    private final String alliesFraction;
    private final String uBoatName;
    private final String status;
    private final String Difficulty;
    private final String BattleName;
    private final int currentNumberOfAllies;
    private final int totalNumberOfAllies;

    public ContestDTO(BattlefieldDTO battleFieldDTO, String i_uBoatName, String status) {
        uBoatName = i_uBoatName;
        this.status = status;
        alliesFraction = battleFieldDTO.getCurrentNumberOfAllies() + "/" + battleFieldDTO.getNumberOfAllies();
        Difficulty = battleFieldDTO.getDifficulty().toString();
        BattleName = battleFieldDTO.getName();
        currentNumberOfAllies = battleFieldDTO.getCurrentNumberOfAllies();
        totalNumberOfAllies = battleFieldDTO.getNumberOfAllies();
    }

//    public ContestDTO(String alliesFraction, String uBoatName, String status, String difficulty, String battleName){
//        this.alliesFraction = alliesFraction;
//        this.uBoatName = uBoatName;
//        this.status = status;
//        Difficulty = difficulty;
//        BattleName = battleName;
//
//    }

    public String getUBoatName() {
        return uBoatName;
    }

    public String getAlliesFraction() {
        return alliesFraction;
    }

    public String getStatus() {
        return status;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public String getBattleName() {
        return BattleName;
    }

    public int getCurrentNumberOfAllies() {
        return currentNumberOfAllies;
    }

    public int getTotalNumberOfAllies() {
        return totalNumberOfAllies;
    }

    public Boolean isFull(){
        return currentNumberOfAllies >= totalNumberOfAllies;
    }
}
