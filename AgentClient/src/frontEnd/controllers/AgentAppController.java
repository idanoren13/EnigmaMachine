package frontEnd.controllers;

import enigmaEngine.MachineCode;
import immutables.AllyDTO;
import immutables.ContestDataDTO;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;

public class AgentAppController {
    @FXML
    public ScrollPane login;
    @FXML
    private ScrollPane contestComponent;
    @FXML
    private AgentContestScreenController contestComponentController;
    @FXML
    private AgentLoginController loginController;

    private String agentName;

    private AllyDTO selectedAlly;
    private int threadsNumber;
    private int missionSize;
    TaskController taskController;


    @FXML
    public void initialize() {
        if (contestComponentController != null && loginController != null) {
            contestComponentController.setMainController(this);
            loginController.setMainController(this);
        }
    }

    public void endLogin() {
        login.setVisible(false);
        taskController = new TaskController(threadsNumber,this);
    }

    public void setName(String userName) {
        agentName = userName;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAllyName() {
        return selectedAlly.getAllyName();
    }


    public void setAllyTeam(AllyDTO selectedAlly) {
        this.selectedAlly = selectedAlly;
    }

    public void setThreadsNumber(Integer value) {
        threadsNumber = value;
    }

    public void setMissionSize(int parseInt) {
        missionSize = parseInt;
    }

    public int getMissionSize() {
        return missionSize;
    }

    public void showCandidates(List<Pair<List<String>, MachineCode>> outputQueue) {
        contestComponentController.showCandidates(outputQueue);
    }

    public void startContest(ContestDataDTO contestDataDTO) {
        taskController.initialize(contestDataDTO.getXmlEnigma(),contestDataDTO.getDifficulty(),contestDataDTO.getEncryptedText());
        taskController.start();
    }

    public void stopContest() {
        try {
            taskController.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
