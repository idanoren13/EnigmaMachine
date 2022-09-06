package desktopApp.frontEnd;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class Screen3Controller {
    // TODO: implement all logic for screen 3 after implementing screens 1 and 2
    private AppController mainController;
    @FXML private ScrollPane scrollPaneContainer;
    @FXML private VBox bruteForceVBox;
    @FXML private Label firstMachineStateLabel;
    @FXML private Label currentMachineStateLabel;
    @FXML private TextField inputToEncryptDecryptInput;
    @FXML private TextField enigmaOutputTextField;
    @FXML private ListView<String> searchDictionaryWordsListView;
    @FXML private Label totalAgentsLabel;
    @FXML private Label difficultyLevelLabel;
    @FXML private Label missionSizeLabel;
    @FXML private Slider agentsSliderInput;
    @FXML private ChoiceBox<String> difficultyLevelInput;
    @FXML private TextField missionSizeInput;
    @FXML private Label totalMissionsLabel;
    @FXML private ListView<String> finalCandidatesListView;

    private int dmTotalAgents;

    @FXML
    public void initialize() {
        setBruteForceDisability(true);
        agentsSliderInput.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { // Updates total agents
                dmTotalAgents = (int)agentsSliderInput.getValue();
                totalAgentsLabel.setText(Integer.toString(dmTotalAgents));
            }
        });
    }

    public void setBruteForceDisability(boolean bool) {
        bruteForceVBox.setDisable(bool);
    }

    public void initializeMachineStates(String machineStateString) {
        firstMachineStateLabel.setText(machineStateString);
        currentMachineStateLabel.setText(machineStateString);
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void resetMachineStateButtonActionListener(ActionEvent event) {
        AppController.getConsoleApp().resetMachine();
        mainController.resetScreens();
    }

    public void updateMachineState(String currentMachineState) {
        currentMachineStateLabel.setText(currentMachineState);
    }
    public void resetMachineState() {
        currentMachineStateLabel.setText(firstMachineStateLabel.getText());
    }
}
