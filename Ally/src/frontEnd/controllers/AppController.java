package frontEnd.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class AppController {
    @FXML
    public ScrollPane login;
    @FXML
    private ScrollPane contestComponent;
    @FXML
    private ScrollPane dashBoardComponent;

    @FXML
    private ContestScreenController contestComponentController;
    @FXML
    private DashBoardController dashBoardComponentController;
    @FXML
    private HeaderController headerComponentController;
    @FXML
    private LoginController loginController;

    private String allyName;

    @FXML
    public void initialize() {
        if (headerComponentController != null && contestComponentController != null && dashBoardComponentController != null && loginController != null) {
            headerComponentController.setMainController(this);
            contestComponentController.setMainController(this);
            dashBoardComponentController.setMainController(this);
            loginController.setMainController(this);
        }
    }

    public void changeToDashBoard() {
        contestComponent.setVisible(false);
        dashBoardComponent.setVisible(true);
    }

    public void changeToContest() {
        contestComponent.setVisible(true);
        dashBoardComponent.setVisible(false);
    }

    public void endLogin() {
        login.setVisible(false);
    }

    public void setName(String userName) {
        allyName = userName;
        headerComponentController.allyName.setText(allyName);
    }
}
