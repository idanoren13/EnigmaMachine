package frontEnd.controllers;

import frontEnd.refreshers.AgentDataRefresher;
import frontEnd.refreshers.CandidateRefresher;
import immutables.AgentDataDTO;
import immutables.CandidateDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

import static utils.Constants.READY;
import static utils.Constants.REFRESH_RATE;

public class ContestScreenController implements Initializable {
    @FXML
    private TableView<AgentDataDTO> agentsProgress;
    @FXML
    private TableColumn<AgentDataDTO, String> agentName;
    @FXML
    private TableColumn<AgentDataDTO, Long> progress;
    @FXML
    private TableColumn<AgentDataDTO, Integer> candidatesDelivered;

    @FXML
    private TableView<CandidateDTO> teamStringCandidates;
    @FXML
    private TableColumn<CandidateDTO, String> agentsCandidate;
    @FXML
    private TableColumn<CandidateDTO, String> candidateString;
    private AllyAppController mainController;
    private AgentDataRefresher agentDataRefresher;
    Timer timer1;
    Timer timer2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeContestsTable();
        initializeTeamStringCandidatesTable();

    }

    private void initializeTeamStringCandidatesTable() {
        agentsCandidate.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        candidateString.setCellValueFactory(new PropertyValueFactory<>("candidateString"));
    }

    public void startTeamStringCandidatesRefersher() {
        CandidateRefresher candidateRefresher = new CandidateRefresher(this::updateTeamStringCandidatesTable);
        candidateRefresher.setName(mainController.getAllyName());
        timer2 = new Timer();
        timer2.schedule(candidateRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateTeamStringCandidatesTable(CandidateDTO[] candidateDTOS) {

    }

    private void initializeContestsTable() {
        agentName.setCellValueFactory(new PropertyValueFactory<AgentDataDTO, String>("agentName"));
        progress.setCellValueFactory(new PropertyValueFactory<AgentDataDTO, Long>("progress"));
        candidatesDelivered.setCellValueFactory(new PropertyValueFactory<AgentDataDTO, Integer>("candidatesDelivered"));
    }

    public void startAgentDataRefresher(){
        agentDataRefresher = new AgentDataRefresher(this::updateAgentsProgressTable);
        agentDataRefresher.setName(mainController.getAllyName());
        timer1 = new Timer();
        timer1.schedule(agentDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateAgentsProgressTable(AgentDataDTO[] agentsDataDTO) {
        Platform.runLater(() -> {
            agentsProgress.getItems().clear();
            agentsProgress.getItems().addAll(agentsDataDTO);
        });
    }

    public void setMainController(AllyAppController appController) {
        this.mainController = appController;
    }

    public void onReadyButton(ActionEvent actionEvent) {
        String url = HttpUrl.parse(READY).newBuilder()
                .addQueryParameter("name", mainController.getAllyName())
                .addQueryParameter("entity", "ally")
                .addQueryParameter("uboatName", mainController.getSelectedContest().getUBoatName())
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, "Failed to start contest.").show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.INFORMATION, "Contest started.").show();
                        startTeamStringCandidatesRefersher();
                    });
                } else {
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.ERROR, "Failed to start contest.").show();
                    });
                }
            }
        });
    }
}
