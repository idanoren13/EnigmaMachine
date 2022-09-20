package desktopApp.frontEnd;

import automateDecryption.Difficulty;
import automateDecryption.TasksManager;
import desktopApp.impl.models.MachineStateConsole;
import enigmaEngine.exceptions.InvalidCharactersException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Screen3Controller implements Initializable {
    public Button startResumeDM;
    public Button pauseDM;
    public Button stopDM;
    public Button spaceButton;
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
    private Label firstMachineStateLabel;
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
    private Label totalAgentsLabel;
    @FXML
    private Slider agentsSliderInput;
    @FXML
    private ChoiceBox<String> difficultyLevelInput;
    @FXML
    private Label difficultyLevelLabel;
    @FXML
    private Label missionSizeLabel;
    @FXML
    private TextField missionSizeInput;
    @FXML
    private Label totalMissionsLabel;

    @FXML
    private Button setDMProperties;

    private int dmTotalAgents;
    private int dmMissionSize;
    private Difficulty dmDifficultyLevel;
    // DM Output
    @FXML private TextArea finalCandidatesTextArea;
    StringProperty finalCandidates;
    TasksManager tasksManagerLogic;
    boolean existingInput = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        finalCandidates = new SimpleStringProperty("");
        finalCandidatesTextArea.textProperty().bind(finalCandidates);
        // Only for binding the ENTER key to the input text field
        enterCurrentKeyboardInputButton.setOnAction(this::enterCurrentKeyboardInputButtonActionListener);
        keyboardInputVBox.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                enterCurrentKeyboardInputButton.fire();
                ev.consume();
            }
        });
//        InitMachine.setOnAction(this::InitMachineActionListener);

        // Updates total agents
        setBruteForceDisability(true);
        agentsSliderInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            dmTotalAgents = (int) agentsSliderInput.getValue();
            totalAgentsLabel.setText(Integer.toString(dmTotalAgents));
        });

        // Updates mission size
        missionSizeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                dmMissionSize = Integer.parseInt(newValue);
                missionSizeLabel.setText(Integer.toString(dmMissionSize));
            } catch (NumberFormatException e) {
                missionSizeLabel.setText("Invalid input");
            }
        });

        // Updates difficulty level
        difficultyLevelInput.getItems().addAll(Arrays.stream(Difficulty.values()).map(Enum::name).toArray(String[]::new));
        difficultyLevelInput.setValue("Easy");
        difficultyLevelInput.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.intValue()) {
                case 0:
                    dmDifficultyLevel = Difficulty.EASY;
                    break;
                case 1:
                    dmDifficultyLevel = Difficulty.MEDIUM;
                    break;
                case 2:
                    dmDifficultyLevel = Difficulty.HARD;
                    break;
                case 3:
                    dmDifficultyLevel = Difficulty.IMPOSSIBLE;
                    break;
            }

            String difficultyLevel = dmDifficultyLevel.toString().toLowerCase();
            difficultyLevelLabel.setText(difficultyLevel.substring(0, 1).toUpperCase() + difficultyLevel.substring(1));
        });

//        setDMProperties.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
//            if (ev.getCode() == KeyCode.ENTER) {
//                setDMProperties.fire();
//                ev.consume();
//            }
//        });

        pauseDM.setOnAction(this::PauseDMActionListener);

        stopDM.setOnAction(this::StopDMActionListener);

        startResumeDM.setDisable(true);
        pauseDM.setDisable(true);
        stopDM.setDisable(true);

        // Updates search dictionary
        searchInputTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDictionaryWordsListView.getItems().clear();
            searchDictionaryWordsListView.getItems().addAll( // Sorted dictionary list view
                    mainController.getDictionary().stream().map((word) -> word.trim())
                            .sorted().collect(Collectors.toList()));
        });

        searchDictionaryWordsListView.onMousePressedProperty().addListener((observable, oldValue, newValue) -> {
            String selectedWord = searchDictionaryWordsListView.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                inputToEncryptDecryptInput.setText(selectedWord);
            }
        });
//        searchDictionaryWordsListView.getItems().addAll(mainController.getDictionary());

        searchDictionaryWordsListView.editableProperty().setValue(false);
        searchDictionaryWordsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selectedWord = searchDictionaryWordsListView.getSelectionModel().getSelectedItem();
            if (selectedWord != null && inputToEncryptDecryptInput.getText() != null) {
                inputToEncryptDecryptInput.setText(inputToEncryptDecryptInput.getText() + selectedWord + " "); // Added " "
            }
        });

        spaceButton.setOnAction((event) -> {
            inputToEncryptDecryptInput.setText(inputToEncryptDecryptInput.getText() + " ");
        });

        // Model
        machineStatesConsole = new MachineStateConsole();
        firstMachineStateLabel.textProperty().bind(machineStatesConsole.firstMachineStateProperty());
        currentMachineStateLabel.textProperty().bind(machineStatesConsole.currentMachineStateProperty());

    }

    private void StopDMActionListener(ActionEvent actionEvent) {
        startResumeDM.setText(startResumeDM.getText().replace("Resume", "Start"));
        stopDM.setDisable(true);
        pauseDM.setDisable(true);
        setDMProperties.setDisable(false);
        startResumeDM.setDisable(false);
        tasksManagerLogic.stop();

    }

    private void PauseDMActionListener(ActionEvent actionEvent) {
//        startResumeDM.setText(startResumeDM.getText().replace("Resume", "Start"));
        setDMProperties.setDisable(false);
        startResumeDM.setDisable(false);
        pauseDM.setDisable(true);
    }

    @FXML
    void StartResumeDMActionListener(ActionEvent actionEvent) {
        if (startResumeDM.getText().contains("Start")) {

            startResumeDM.setText(startResumeDM.getText().replace("Start", "Resume"));
        }
        setDMProperties.setDisable(true);
        startResumeDM.setDisable(true);
        stopDM.setDisable(false);
        pauseDM.setDisable(false);
        if (!enigmaOutputTextField.getText().isEmpty()) {
            mainController.setEncryptedText(enigmaOutputTextField.getText());
            mainController.startResumeDM();
        }
    }

    @FXML
    void setDMPropertiesActionListener(ActionEvent event) {
        try {
            // totalMissionsLabel.setText
            mainController.setDMProperties(dmTotalAgents, dmMissionSize, dmDifficultyLevel);
            setBruteForceDisability(false);
            startResumeDM.setDisable(false);
        } catch (InputMismatchException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid input");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setBruteForceDisability(boolean bool) {
        bruteForceVBox.setDisable(bool);
    }

    public void updateAmountAgents(int amountAgents) {
        agentsSliderInput.setMax(amountAgents);
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
            messageOutput = AppController.getConsoleApp().getMessageAndProcessIt(messageInput, true);

            new Alert(Alert.AlertType.CONFIRMATION, "Processed message: " + messageInput + " -> " + messageOutput).show();
            enigmaOutputTextField.setText(messageOutput);
            mainController.updateScreens(AppController.getConsoleApp().getCurrentMachineState());
            mainController.updateLabelTextsToEmpty(this);
            existingInput = true;
        } catch (InvalidCharactersException | InputMismatchException e) {
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

    @FXML
    void resetMachineStateButtonActionListener() {
        AppController.getConsoleApp().resetMachine();
        mainController.resetScreens(true, null);
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

    public void setTasksManagerLogic(TasksManager decryptionManagerLogic) {
        this.tasksManagerLogic = decryptionManagerLogic;
    }

    public void updateCandidateWords(String s) {
        finalCandidates.setValue(finalCandidatesTextArea.getText() +'\n' + s );
    }
}
