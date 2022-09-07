package desktopApp.frontEnd;

import enigmaEngine.exceptions.InvalidCharactersException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

public class Screen3Controller implements Initializable {
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
    @FXML private Label setCodeLabel;
    @FXML private Slider agentsSliderInput;
    @FXML private ChoiceBox<String> difficultyLevelInput;
    @FXML private TextField missionSizeInput;
    @FXML private Label totalMissionsLabel;
    @FXML private ListView<String> finalCandidatesListView;
    @FXML private GridPane keyboardInputGridPane; // Only for binding the ENTER key to the input text field
    @FXML private Button enterCurrentKeyboardInputButton;

    private int dmTotalAgents;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Only for binding the ENTER key to the input text field
        enterCurrentKeyboardInputButton.setOnAction(event -> enterCurrentKeyboardInputButtonActionListener(event));
        keyboardInputGridPane.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                enterCurrentKeyboardInputButton.fire();
                ev.consume();
            }
        });
        // Adding change property
        inputToEncryptDecryptInput.textProperty().addListener(new ClearStatusListener());
        // Updates total agents
        setBruteForceDisability(true);
        agentsSliderInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            dmTotalAgents = (int)agentsSliderInput.getValue();
            totalAgentsLabel.setText(Integer.toString(dmTotalAgents));
        });
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
            messageOutput = AppController.getConsoleApp().getMessageAndProcessIt(messageInput);
            setCodeLabel.setText("Processed message: " + messageInput + " -> " + messageOutput);
            enigmaOutputTextField.setText(messageOutput);
            mainController.updateScreens(AppController.getConsoleApp().getCurrentMachineState());
            mainController.updateLabelTextsToEmpty();
        } catch (InvalidCharactersException | InputMismatchException e) {
            setCodeLabel.setText(e.getLocalizedMessage());
        }
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
        mainController.resetScreens(true);
    }

    public void updateMachineState(String currentMachineState) {
        currentMachineStateLabel.setText(currentMachineState);
    }
    public void resetMachineStateAndEnigmaOutput(boolean bool) {
        if (bool) {
            setCodeLabel.setText("Machine state has been successfully reset");
        }
        currentMachineStateLabel.setText(firstMachineStateLabel.getText());
        enigmaOutputTextField.setText("NaN");
    }

    class ClearStatusListener implements ChangeListener<String> {
        @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            setCodeLabel.setText("");
        }
    }

    public void updateLabelTextsToEmpty() {
        setCodeLabel.setText("");
        inputToEncryptDecryptInput.setText("");
    }
}
