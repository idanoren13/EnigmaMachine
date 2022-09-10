package desktopApp.frontEnd;

import desktopApp.impl.models.MachineStateConsole;
import enigmaEngine.exceptions.InvalidCharactersException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

public class Screen2Controller implements Initializable {
    // TODO: for idan: talk with snir about mouse input->output ("Mouse Input"->"Mouse Output")
    // Main component
    private AppController mainController;
    @FXML private MachineStateController firstMachineStateComponentController;
    @FXML private MachineStateController currentMachineStateComponentController;
    // Models
    private MachineStateConsole machineStatesConsole = new MachineStateConsole();
    //
    @FXML private BorderPane borderPaneContainer;
    // Machine states
/*    @FXML private Label firstMachineStateLabel;
    @FXML private Label currentMachineStateLabel;*/
    @FXML private Label currentMachineInitialStateLabel;
    @FXML private Label setCodeLabel;
    @FXML private TextField inputToEncryptDecryptInput;
    @FXML private Button encryptionDecryptionInputButton;
    @FXML private Button resetMachineStateButton;

    @FXML private Button enterCurrentKeyboardInputButton;
    @FXML private GridPane keyboardInputGridPane; // Only for binding the ENTER key to the input text field

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (firstMachineStateComponentController != null && currentMachineStateComponentController != null) {
            firstMachineStateComponentController.setScreen2Controller(this);
            currentMachineStateComponentController.setScreen2Controller(this);

            // Only for binding the ENTER key to the input text field
            enterCurrentKeyboardInputButton.setOnAction(this::enterCurrentKeyboardInputButtonActionListener);
            keyboardInputGridPane.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
                if (ev.getCode() == KeyCode.ENTER) {
                    enterCurrentKeyboardInputButton.fire();
                    ev.consume();
                }
            });
            // Adding change property
            inputToEncryptDecryptInput.textProperty().addListener(new ClearStatusListener());
            // Initialization
            setEnigmaDecryptionInputDisability(true);
            encryptionDecryptionInputButton.getStyleClass().add("chosen-button");
            // Model
            machineStatesConsole = new MachineStateConsole();
/*            firstMachineStateLabel.textProperty().bind(machineStatesConsole.firstMachineStateProperty());
            currentMachineStateLabel.textProperty().bind(machineStatesConsole.currentMachineStateProperty());*/
        }
    }

    public void setEnigmaDecryptionInputDisability(boolean bool) {
        borderPaneContainer.setDisable(bool);
    }

    @FXML
    void enterCurrentKeyboardInputButtonActionListener(ActionEvent event) {
        try {
            String messageInput = inputToEncryptDecryptInput.getText().toUpperCase();
            if (messageInput.equals("")) {
                throw new InputMismatchException("No encryption message was written.");
            }
            setCodeLabel.setText("Processed message: " + messageInput + " -> " + AppController.getConsoleApp().getMessageAndProcessIt(messageInput));
            mainController.updateScreens(AppController.getConsoleApp().getCurrentMachineState());
            mainController.updateLabelTextsToEmpty();
        } catch (InvalidCharactersException | InputMismatchException e) {
            setCodeLabel.setText(e.getLocalizedMessage());
        }
    }

    public void initializeMachineStates() {
        // Machine States
        firstMachineStateComponentController.setInitializedControllerComponents(AppController.getConsoleApp().getEngine().getEngineDTO());
        currentMachineStateComponentController.setInitializedControllerComponents(AppController.getConsoleApp().getEngine().getEngineDTO());

        // Enigma code input
        currentMachineInitialStateLabel.setText(AppController.getConsoleApp().getMachineStatisticsAndHistory());
        updateButtonsCSS();
        setCodeLabel.setText("");
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void resetMachineStateButtonActionListener() {
        AppController.getConsoleApp().resetMachine();
        mainController.resetScreens(true, this);

        encryptionDecryptionInputButton.getStyleClass().remove("chosen-button");
        resetMachineStateButton.getStyleClass().add("chosen-button");
    }


    @FXML
    void inputToEncryptDecryptOnKeyPressed() {
        updateButtonsCSS();
    }

    @FXML
    void inputToEncryptDecryptOnMouseClicked() {
        updateButtonsCSS();
    }

    @FXML
    void inputToEncryptDecryptOnMousePressed() {
        updateButtonsCSS();
    }

    private void updateButtonsCSS() {
        encryptionDecryptionInputButton.getStyleClass().add("chosen-button");
        resetMachineStateButton.getStyleClass().remove("chosen-button");
    }

    public void updateMachineStateAndStatistics(String currentMachineState) {
        machineStatesConsole.setCurrentMachineState(currentMachineState);
        currentMachineInitialStateLabel.setText(AppController.getConsoleApp().getMachineStatisticsAndHistory());
        currentMachineStateComponentController.setInitializedControllerComponents(AppController.getConsoleApp().getEngine().getEngineDTO());
    }

    public void resetMachineStateAndStatistics(boolean bool) {
        if (bool) {
            setCodeLabel.setText("Machine state has been successfully reset");
        }
        machineStatesConsole.setCurrentMachineState(machineStatesConsole.getFirstMachineState());
        currentMachineStateComponentController.resetMachineStateComponentComponent(AppController.getConsoleApp().getEngine().getEngineDTO());
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