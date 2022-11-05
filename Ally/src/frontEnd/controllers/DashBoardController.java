package frontEnd.controllers;

import frontEnd.refreshers.BattlefieldsRefresher;
import immutables.ContestDTO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

import static utils.Constants.JOIN_CONTEST;
import static utils.Constants.REFTESH_RATE;

public class DashBoardController implements Initializable {
    @FXML
    private TableColumn<ContestDTO, String> statusCol;
    @FXML
    private TableColumn<ContestDTO, String> uboatNameCol;
    @FXML
    private TableColumn<ContestDTO, String> difficultyCol;
    @FXML
    private TableColumn<ContestDTO, String> alliesCol;
    @FXML
    private TableColumn<ContestDTO, String> battleCol;

    @FXML
    private TableView<ContestDTO> contestsDataTable;
    @FXML
    private TableView teamAgentsDataTable;

    private AppController mainController;
    private BattlefieldsRefresher battlefieldsRefresher;
    private ContestDTO contest;
    private Timer timer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeContestsDataTable();

    }

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    private void initializeContestsDataTable() {
        statusCol.setCellValueFactory(new PropertyValueFactory<ContestDTO, String>("status"));
        uboatNameCol.setCellValueFactory(new PropertyValueFactory<ContestDTO, String>("UBoatName"));
        difficultyCol.setCellValueFactory(new PropertyValueFactory<ContestDTO, String>("difficulty"));
        alliesCol.setCellValueFactory(new PropertyValueFactory<ContestDTO, String>("alliesFraction"));
        battleCol.setCellValueFactory(new PropertyValueFactory<ContestDTO, String>("BattleName"));
    }

    public void startBattlefieldsRefresher() {
        battlefieldsRefresher = new BattlefieldsRefresher(this::updateContestTable);
        timer = new Timer();
        timer.schedule(battlefieldsRefresher, REFTESH_RATE, REFTESH_RATE);
    }

    private void updateContestTable(ContestDTO[] contestDetails) {
        Platform.runLater(() -> {
            contestsDataTable.getItems().clear();
            for (ContestDTO contest : contestDetails) {
                ObservableList<ContestDTO> items = contestsDataTable.getItems();
                items.add(contest);
                contestsDataTable.setItems(items);
            }
        });
    }

    public void selcetContest(MouseEvent mouseEvent) {

        contest = contestsDataTable.getSelectionModel().getSelectedItem();
//        if (contest.getStatus().equals("open")) {
//            if (!contest.isFull()) {
////                joinContest(contest);
//            } else {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Error");
//                alert.setHeaderText("Contest is full");
//                alert.setContentText("Contest is full, please try again later");
//                alert.showAndWait();
//            }
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Contest is closed");
//            alert.setContentText("Contest is closed, please try again later");
//            alert.showAndWait();
//        }

    }

    private void joinContest(ContestDTO selectedContest) {
        synchronized (this) {
            mainController.setSelectedContest(selectedContest);

            String url = HttpUrl.parse(JOIN_CONTEST).newBuilder()
                    .addQueryParameter("uboatName", selectedContest.getUBoatName())
                    .addQueryParameter("allyName", mainController.getAllyName())
                    .build().toString();

            HttpClientUtil.runAsync(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    new Alert(Alert.AlertType.ERROR, "Failed to join contest").showAndWait();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Platform.runLater(() -> {
                            mainController.loadBattlefield();
                        });
                    }
                }
            });
        }
    }

    public void onJoinContest(ActionEvent actionEvent) {
        joinContest(contest);
    }
}

