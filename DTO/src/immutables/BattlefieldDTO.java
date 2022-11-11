package immutables;

import java.io.Serializable;

public class BattlefieldDTO implements Serializable {
    private final String name;
    private final int numberOfAllies;
    private final int currentNumberOfAllies;
    private final Difficulty difficulty;

    public BattlefieldDTO(String name, int numberOfAllies, int currentNumberOfAllies, Difficulty difficulty) {
        this.name = name;
        this.numberOfAllies = numberOfAllies;
        this.currentNumberOfAllies = currentNumberOfAllies;
        this.difficulty = difficulty;
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

    @Override
    public String toString() {
        return  "Battle name=" + name +
                " , Allies= " +currentNumberOfAllies +" / "+ numberOfAllies +
                " , difficulty= " + difficulty;
    }
}
