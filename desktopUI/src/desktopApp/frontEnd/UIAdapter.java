package desktopApp.frontEnd;

import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private Consumer<String> concatenatedString;
    private Runnable updateDistinct;
    private Consumer<Integer> updateTotalProcessedMissions;

    public UIAdapter(Consumer<String> introduceNewWord, Runnable updateDistinct, Consumer<Integer> updateTotalProcessedWords) {
        this.concatenatedString = introduceNewWord;
        this.updateDistinct = updateDistinct;
        this.updateTotalProcessedMissions = updateTotalProcessedWords;
    }

    public void addNewWord(String histogramData) {
        Platform.runLater(
                () -> {
                    concatenatedString.accept(histogramData);
                    updateDistinct.run();
                }
        );
    }

    public void updateTotalProcessedMissions(int delta) {
        Platform.runLater(
                () -> updateTotalProcessedMissions.accept(delta)
        );
    }

}
