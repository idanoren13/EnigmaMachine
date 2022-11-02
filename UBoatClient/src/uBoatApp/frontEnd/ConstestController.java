package uBoatApp.frontEnd;


import immutables.engine.Difficulty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.EventListener;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

public class ConstestController implements Initializable {
    public Button startResumeDM;
    public Button pauseDM;
    public Button stopDM;
    public Button spaceButton;
    public Label progressLabel;
    public TableView candidatesTable;
    public TableView alliesDetailsTable;
    //    public ProgressBar progressBar;
    // TODO: implement all logic for screen 3 after implementing screens 1 and 2
    // Main component
    private AppController mainController;
    // Models
    private MachineStateConsole machineStatesConsole;
    //
    @FXML
    private VBox bruteForceVBox;
    // Machine states
    @FXML
    private Label currentMachineStateLabel;
    // Search for words
    @FXML
    private TextField searchInputTextField;
    @FXML
    private ListView<String> searchDictionaryWordsListView;
    // Input to encrypt / decrypt
    @FXML
    private VBox keyboardInputVBox; // Only for binding the ENTER key to the input text field
    @FXML
    private TextField inputToEncryptDecryptInput;
    @FXML
    private TextField enigmaOutputTextField;
    @FXML
    private Button enterCurrentKeyboardInputButton;
    //    @FXML private Button InitMachine;
    // DM input
    @FXML


    private int dmTotalAgents;
    private int dmMissionSize;
    private Difficulty dmDifficultyLevel;
    // DM Output
    StringProperty finalCandidates;
    boolean existingInput = false;

    EventListener onXMLLoaded;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void setBruteForceDisability(boolean bool) {
        bruteForceVBox.setDisable(bool);
    }


    @FXML
    void enterCurrentKeyboardInputButtonActionListener(ActionEvent event) {
        try {
            String messageInput = inputToEncryptDecryptInput.getText().toUpperCase(), messageOutput;
            if (messageInput.equals("")) {
                throw new InputMismatchException("No encryption message was written.");
            }
            if (messageInput.charAt(messageInput.length() - 1) == ' ') {
                messageInput = messageInput.substring(0, messageInput.length() - 1);
            }
//            messageOutput = AppController.getConsoleApp().getMessageAndProcessIt(messageInput, true);
//
//            new Alert(Alert.AlertType.CONFIRMATION, "Processed message: " + messageInput + " -> " + messageOutput).show();
//            enigmaOutputTextField.setText(messageOutput);
//            mainController.updateScreens(AppController.getConsoleApp().getCurrentMachineState());
//            mainController.updateLabelTextsToEmpty(this);
//            existingInput = true;
        } catch ( InputMismatchException e) {
            new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage()).show();
        }
    }

    public void initializeMachineStates(String machineStateConsoleString) {
        machineStatesConsole.setFirstMachineState(machineStateConsoleString);
        machineStatesConsole.setCurrentMachineState(machineStateConsoleString);
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


    public void updateMachineState(String currentMachineState) {
        machineStatesConsole.setCurrentMachineState(currentMachineState);
    }

    public void resetMachineStateAndEnigmaOutput(boolean bool, Object controller) {
        if (bool && controller == null) {
            new Alert(Alert.AlertType.INFORMATION, "Machine state has been successfully reset.").show();
        }
        machineStatesConsole.setCurrentMachineState(machineStatesConsole.getFirstMachineState());
        enigmaOutputTextField.setText("NaN");
    }

    public void updateLabelTextsToEmpty() {
        inputToEncryptDecryptInput.setText("");
        finalCandidates.setValue("");
    }

}
