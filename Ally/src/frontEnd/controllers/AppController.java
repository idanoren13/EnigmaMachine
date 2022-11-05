package frontEnd.controllers;

import immutables.ContestDTO;
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

    private ContestDTO selectedContest;

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
        dashBoardComponentController.startBattlefieldsRefresher();
    }

    public void setName(String userName) {
        allyName = userName;
        headerComponentController.allyName.setText(allyName);
    }

    public String getAllyName() {
        return allyName;
    }

    public void setSelectedContest(ContestDTO selectedContest) {
        this.selectedContest = selectedContest;
    }

    public void loadBattlefield() {
        changeToContest();
        dashBoardComponent.setDisable(true);
    }

    public ContestDTO getSelectedContest() {
        return selectedContest;
    }
}
