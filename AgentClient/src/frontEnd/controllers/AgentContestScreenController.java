package frontEnd.controllers;

import enigmaEngine.MachineCode;
import frontEnd.refreshers.UpdateContestRefresher;
import immutables.ContestDataDTO;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.stream.Collectors;

import static utils.Constants.REFTESH_RATE;

public class AgentContestScreenController implements Initializable {
    public TableView<Pair<String,String>> candidatesTable;
    public TableColumn<Pair<String,String>, String> candidateStringCol;
    public TableColumn<Pair<String,String>, String> machineCodeCol;
    private AgentAppController mainController;
    private List<Pair<List<String>, MachineCode>> candidatesList;
    private Timer timer;
    ContestDataDTO contestDataDTO;
    private Boolean isContestStarted = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCandidatesTable();

    }

    public void setMainController(AgentAppController appController) {
        this.mainController = appController;
    }

    public void showCandidates(List<Pair<List<String>, MachineCode>> outputQueue) {
        candidatesList.addAll(outputQueue);
        candidatesTable.getItems().clear();
        List<Pair<String,String>> output = candidatesList.stream().map(pair -> new Pair<>(pair.getKey().toString(), pair.getValue().toString())).collect(Collectors.toList());
        candidatesTable.getItems().addAll(output);
    }

    private void initializeCandidatesTable() {
        candidateStringCol.setCellValueFactory(new PropertyValueFactory<Pair<String,String>, String>("key"));
        machineCodeCol.setCellValueFactory(new PropertyValueFactory<Pair<String,String>, String>("value"));
    }

    public void startContestRefresher() {
        UpdateContestRefresher updateContestRefresher = new UpdateContestRefresher(this::getContestDTO);
        updateContestRefresher.setAllyName(mainController.getAllyName());
        timer = new Timer();
        timer.schedule(updateContestRefresher,REFTESH_RATE, REFTESH_RATE);
    }

    private void getContestDTO(ContestDataDTO contestDataDTO) {
        if (contestDataDTO.getIsStarted() && !isContestStarted) {
            mainController.startContest(contestDataDTO);
            isContestStarted = true;
        }
        if (!contestDataDTO.getIsStarted() && isContestStarted) {
            mainController.stopContest();
        }
    }


}
