package desktopApp.frontEnd;

import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private Consumer<AgentData> concatenatedString;
    private Runnable updateDistinct;
    private Consumer<Integer> updateTotalProcessedMissions;

    public UIAdapter(Consumer<AgentData> introduceNewAgent, Runnable updateDistinct, Consumer<Integer> updateTotalProcessedMissions) {
        this.concatenatedString = introduceNewAgent;
        this.updateDistinct = updateDistinct;
        this.updateTotalProcessedMissions = updateTotalProcessedMissions;
    }

    public void addNewWord(AgentData agentData) {
        Platform.runLater(
                () -> {
                    concatenatedString.accept(agentData);
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
