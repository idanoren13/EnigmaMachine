package frontEnd.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable{

    @FXML
    public Button DashButton;
    public Button contestButton;
    public Label allyName;

    AppController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DashButton.getStyleClass().add("chosen-button");
        allyName.setText("");
    }


    public void dashBoardButtonActionListener(ActionEvent actionEvent) {
        DashButton.getStyleClass().add("chosen-button");
        contestButton.getStyleClass().remove("chosen-button");
        mainController.changeToDashBoard();
    }

    public void contestButtonActionListener(ActionEvent actionEvent) {
        contestButton.getStyleClass().add("chosen-button");
        DashButton.getStyleClass().remove("chosen-button");
        mainController.changeToContest();
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
