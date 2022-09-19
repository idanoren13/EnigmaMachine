package desktopApp.frontEnd;

import desktopApp.impl.models.MachineStateConsole;
import enigmaEngine.exceptions.InvalidCharactersException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ResourceBundle;

public class Screen2Controller implements Initializable {
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
    // Mouse bonus
    @FXML private TextField mouseInputTextField; // Automatic input after each click
    @FXML private TextField mouseOutputTextField; // Automatic output after each click
    @FXML private Button clearInputButton; // Resets mouse input option
    @FXML private Button mouseEndOfInputButton; // Ends mouse input option
    @FXML private FlowPane mouseInputFlowPane; // Dynamic component of XML Enigma buttons (all ABC letters), for input (clickable button-nodes)
    @FXML private FlowPane mouseOutputFlowPane; // Dynamic component of XML Enigma buttons (all ABC letters), for output (non-clickable button-nodes)
    Button lastOutputButton = null;

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
            produceProcessedMessage(messageInput);
        } catch (InvalidCharactersException | InputMismatchException e) {
            setCodeLabel.setText(e.getLocalizedMessage());
        }
    }

    private void produceProcessedMessage(String messageInput) throws InvalidCharactersException {
        setCodeLabel.setText("Processed message: " + messageInput + " -> " + AppController.getConsoleApp().getMessageAndProcessIt(messageInput, true));
        mainController.updateScreens(AppController.getConsoleApp().getCurrentMachineState());
        mainController.updateLabelTextsToEmpty(this);
    }

    public void mouseEndOfInputButtonActionListener() {
        if (mouseInputTextField.getText() == "") {
            new Alert(Alert.AlertType.WARNING, "No encryption message was written.").show();
        } else {
            try {
                produceProcessedMessage(mouseInputTextField.getText());
            } catch (InvalidCharactersException | InputMismatchException e) {
                setCodeLabel.setText(e.getLocalizedMessage());
            }
        }
    }

    public void clearInputButtonActionListener() {
        if (!mouseInputTextField.equals("")) {
            lastOutputButton.getStyleClass().remove("pressed-keyboard-button-output");
        }
        mouseInputTextField.setText("");
        mouseOutputTextField.setText("");
    }

    public void initializeMachineStatesAndMouseInputKeyboard() {
        // Machine States
        firstMachineStateComponentController.setInitializedControllerComponents(AppController.getConsoleApp().getEngine().getEngineDTO());
        currentMachineStateComponentController.setInitializedControllerComponents(AppController.getConsoleApp().getEngine().getEngineDTO());

        // Enigma code input
        currentMachineInitialStateLabel.setText(AppController.getConsoleApp().getMachineStatisticsAndHistory());
        updateButtonsCSS();
        setCodeLabel.setText("");

    }

    public void updateDynamicKeyboards() {
        createDynamicInputKeyboardFromABC();
        createDynamicOutputKeyboardFromABC();
    }

    private void createDynamicInputKeyboardFromABC() {
        List<Character> xmlABC = AppController.getConsoleApp().getXmlLoader().getABCFromXML();
        EventHandler<MouseEvent> mouseClickHandler = event -> {
            if (MouseButton.PRIMARY.equals(event.getButton()) || MouseButton.SECONDARY.equals(event.getButton())) {
                mouseInputTextField.setText(mouseInputTextField.getText() + ((Button)event.getSource()).getText()); // Updates input text
                try {
                    // TODO: handle with 'clear input'
                    String outputLetter = AppController.getConsoleApp().getMessageAndProcessIt(((Button)event.getSource()).getText(), true); // Creates output
                    mouseOutputTextField.setText(mouseOutputTextField.getText() + outputLetter); // Updates output text
                    for (Node node : mouseOutputFlowPane.getChildren()) {
                        if (node instanceof Button) {
                            Button button = (Button)node;
                            if (button.getText().equalsIgnoreCase(outputLetter)) {
                                node.getStyleClass().add("pressed-keyboard-button-output");
                                if (lastOutputButton != null) {
                                    lastOutputButton.getStyleClass().remove("pressed-keyboard-button-output");
                                }
                                lastOutputButton = button;
                            }
                        }
                    }
                } catch (InvalidCharactersException e) {
                    new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage()).show();
                }
            }
        };
        // Remove old keyboard buttons
        for (int i = 0; i < mouseInputFlowPane.getChildren().size(); i++) {
            mouseInputFlowPane.getChildren().remove(0);
        }
        // Add new
        for (Character ch : xmlABC) {
            Button currButton = new Button(ch.toString());
            currButton.setOnMouseClicked(mouseClickHandler);
            currButton.getStyleClass().add("enigma-keyboard-button");
            mouseInputFlowPane.getChildren().add(currButton);
        }
    }

    private void createDynamicOutputKeyboardFromABC() {
        List<Character> xmlABC = AppController.getConsoleApp().getXmlLoader().getABCFromXML();
        // Remove old keyboard buttons
        for (int i = 0; i < mouseOutputFlowPane.getChildren().size(); i++) {
            mouseOutputFlowPane.getChildren().remove(0);
        }
        // Add new
        for (Character ch : xmlABC) {
            Button currButton = new Button(ch.toString());
            currButton.getStyleClass().add("enigma-keyboard-button");
            mouseOutputFlowPane.getChildren().add(currButton);
        }
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
    public void updateLabelTextsToEmpty(Object component) {
        setCodeLabel.setText("");
        inputToEncryptDecryptInput.setText("");
        if (!component.getClass().getSimpleName().equals("ClearStatusListener") && !component.getClass().getSimpleName().equals("Screen3Controller")) {
            lastOutputButton.getStyleClass().remove("pressed-keyboard-button-output");
        }
        mouseInputTextField.setText("");
        mouseOutputTextField.setText("");
    }
}