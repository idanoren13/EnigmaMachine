package immutables;

import java.io.InputStream;

public class ContestDataDTO {
    private final InputStream xmlEnigma;
    private final String encryptedText;
    private final Difficulty difficulty;
    private final Boolean isStarted;

    public ContestDataDTO(InputStream xmlEnigma, String encryptedText, Difficulty difficulty, Boolean isStarted) {
        this.xmlEnigma = xmlEnigma;
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

    public InputStream getXmlEnigma() {
        return xmlEnigma;
    }

    public Boolean getIsStarted() {
        return isStarted;
    }
}
