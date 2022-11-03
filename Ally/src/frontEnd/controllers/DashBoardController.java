package frontEnd.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class DashBoardController {
    @FXML
    private TableView contestsDataTable ;
    @FXML
    private TableView teamAgentsDataTable;

    private AppController mainController;
    public void setMainController(AppController appController) {
        this.mainController = appController;
    }
}
