package frontEnd.controllers;

import immutables.ContestDTO;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class AppController {
    @FXML
    public ScrollPane login;
    @FXML
    private ScrollPane contestComponent;
//    @FXML
//    private ScrollPane dashBoardComponent;

    @FXML
    private ContestScreenController contestComponentController;
    @FXML
    private LoginController loginController;

    private String allyName;

    private ContestDTO selectedContest;

    @FXML
    public void initialize() {
        if (contestComponentController != null  && loginController != null) {
            contestComponentController.setMainController(this);
            loginController.setMainController(this);
        }
    }

    public void changeToDashBoard() {
        contestComponent.setVisible(false);
    }

    public void changeToContest() {
        contestComponent.setVisible(true);
    }

    public void endLogin() {
        login.setVisible(false);
    }

    public void setName(String userName) {
        allyName = userName;
    }

    public String getAllyName() {
        return allyName;
    }

    public void setSelectedContest(ContestDTO selectedContest) {
        this.selectedContest = selectedContest;
    }

    public void loadBattlefield() {
        changeToContest();
    }
}
