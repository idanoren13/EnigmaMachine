package desktopApp.frontEnd;

import enigmaEngine.exceptions.InvalidCharactersException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;

import java.util.InputMismatchException;

public class Screen2Controller {
    // TODO: for idan: talk with snir about mouse input->output ("Mouse Input"->"Mouse Output")
    private AppController mainController;
    @FXML private ScrollPane scrollPaneContainer;
    @FXML private StackPane keyboardInputStackPane;
    @FXML private Label firstMachineStateLabel;
    @FXML private Label currentMachineStateLabel;
    @FXML private TextFlow machineEntireStatisticsAndHistoryTextFlow;
    @FXML private Label currentMachineInitialStateLabel;
    @FXML private Label setCodeLabel;
    @FXML private TextField inputToEncryptDecryptInput;
    @FXML private GridPane mouseGridPane;
    @FXML private Button encryptionDecryptionInputButton;
    @FXML private Button resetMachineStateButton;

    @FXML
    public void initialize() {
        setEnigmaDecryptionInputDisability(true);
        encryptionDecryptionInputButton.getStyleClass().add("chosen-button");
    }

    public void setEnigmaDecryptionInputDisability(boolean bool) {
        keyboardInputStackPane.setDisable(bool);
        mouseGridPane.setDisable(bool);
    }

    @FXML
    void enterCurrentKeyboardInputButtonActionListener(ActionEvent event) {
        try {
            String messageInput = inputToEncryptDecryptInput.getText().toUpperCase();
            if (messageInput.equals("")) {
                throw new InputMismatchException("No encryption message was written.");
            }
            setCodeLabel.setText(AppController.getConsoleApp().getMessageAndProcessIt(messageInput));
            mainController.updateScreens(AppController.getConsoleApp().getCurrentMachineState());
            inputToEncryptDecryptInput.setText("");
        } catch (InvalidCharactersException | InputMismatchException e) {
            setCodeLabel.setText(e.getLocalizedMessage());
        }
    }

    public void initializeMachineStates(String machineStateString) {
        // Machine States
        firstMachineStateLabel.setText(machineStateString);
        currentMachineStateLabel.setText(machineStateString);

        // Enigma code input
        updateButtonsCSS();
        setCodeLabel.setText("");
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void resetMachineStateButtonActionListener(ActionEvent event) {
        AppController.getConsoleApp().resetMachine();
        mainController.resetScreens();

        encryptionDecryptionInputButton.getStyleClass().remove("chosen-button");
        resetMachineStateButton.getStyleClass().add("chosen-button");
    }


    @FXML
    void inputToEncryptDecryptOnKeyPressed(KeyEvent event) {
        updateButtonsCSS();
    }

    @FXML
    void inputToEncryptDecryptOnMouseClicked(MouseEvent event) {
        updateButtonsCSS();
    }

    @FXML
    void inputToEncryptDecryptOnMousePressed(MouseEvent event) {
        updateButtonsCSS();
    }

    private void updateButtonsCSS() {
        encryptionDecryptionInputButton.getStyleClass().add("chosen-button");
        resetMachineStateButton.getStyleClass().remove("chosen-button");
    }

    public void updateMachineStateAndStatistics(String currentMachineState) {
        currentMachineStateLabel.setText(currentMachineState);
        currentMachineInitialStateLabel.setText(AppController.getConsoleApp().getMachineStatisticsAndHistory());
        // TODO: update statistics and history section
    }

    public void resetMachineStateAndStatistics() {
        setCodeLabel.setText("Machine state has been successfully reset");
        currentMachineStateLabel.setText(firstMachineStateLabel.getText());
        currentMachineInitialStateLabel.setText(AppController.getConsoleApp().getMachineStatisticsAndHistory());
        // TODO: update statistics and history section
    }
}