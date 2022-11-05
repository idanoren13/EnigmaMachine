package frontEnd.controllers;

import immutables.AllyDTO;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class AgentAppController {
    @FXML
    public ScrollPane login;
    @FXML
    private ScrollPane contestComponent;
    @FXML
    private ContestScreenController contestComponentController;
    @FXML
    private AgentLoginController loginController;

    private String agentName;

    private AllyDTO selectedAlly;
    private int threadsNumber;
    private int missionSize;

    @FXML
    public void initialize() {
        if (contestComponentController != null  && loginController != null) {
            contestComponentController.setMainController(this);
            loginController.setMainController(this);
        }
    }

    public void endLogin() {
        login.setVisible(false);
    }

    public void setName(String userName) {
        agentName = userName;
    }

    public String getAgentName() {
        return agentName;
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
}
