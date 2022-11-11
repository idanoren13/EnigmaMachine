package immutables;

import java.io.Serializable;

public class ContestDataDTO implements Serializable {
    private final String encryptedText;
    private final Difficulty difficulty;
    private final Boolean isStarted;

    public ContestDataDTO( String encryptedText, Difficulty difficulty, Boolean isStarted) {
        this.encryptedText = encryptedText;
        this.difficulty = difficulty;
        this.isStarted = isStarted;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getEncryptedText() {
        return encryptedText;
    }

    public Boolean getIsStarted() {
        return isStarted;
    }
}
