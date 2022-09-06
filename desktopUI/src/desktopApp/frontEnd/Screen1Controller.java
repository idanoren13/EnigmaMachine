package desktopApp.frontEnd;

import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.exceptions.InvalidPlugBoardException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ResourceBundle;

public class Screen1Controller implements Initializable {


    private AppController mainController;
    @FXML private ScrollPane scrollPaneContainer;

    @FXML private Label machineConfigurationLabel1;

    @FXML private Label machineConfigurationLabel2;

    @FXML private Label machineConfigurationLabel3;

    @FXML private Label machineConfigurationLabel4;

    @FXML private Label machineConfigurationMessageCounterLabel;

    @FXML private TextField rotorsAndOrderTextField;

    @FXML private TextField rotorsStartingPosTextField;

    @FXML private Label firstMachineStateLabel;

    @FXML private TextField plugBoardPairsTextField;

    @FXML private VBox rotorsConfigurationVBox;

    @FXML private VBox plugBoardReflectorConfigurationVBox;

    @FXML private VBox configurationButtonsVBox;

    @FXML private ChoiceBox<String> reflectorChoiceBox;

    @FXML private Label setCodeLabel;

    @FXML private Label currentMachineStateLabel;

    @FXML private Button resetMachineStateButton;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Default values are added in certain screen places. This is called after constructor and after FXML variables are created.
        reset();
    }

    private void setConfigurationDisability(boolean bool) {
        rotorsConfigurationVBox.setDisable(bool);
        plugBoardReflectorConfigurationVBox.setDisable(bool);
        configurationButtonsVBox.setDisable(bool);
    }

    private void initializeMachineState() {
        firstMachineStateLabel.setText("NaN");
        currentMachineStateLabel.setText("NaN");
        setMachineStateDisability(true);
    }

    private void setMachineStateDisability(boolean bool) {
        resetMachineStateButton.setDisable(bool);
    }

    @FXML
    void getConfigurationFromUser(ActionEvent event) {
        if (initializeEnigmaCode(true)) {
            updateConfigurationFieldsAndMachineStateDisability();
            mainController.resetScreens();
        }
    }

    @FXML
    void setConfigurationRandomly(ActionEvent event) {
        if (initializeEnigmaCode(false)) {
            updateConfigurationFieldsAndMachineStateDisability();
            mainController.resetScreens();
        }
    }
    private boolean initializeEnigmaCode(boolean isManual) {
        String rotors, startingPositions, plugBoardPairs, reflectorID;
        if (isManual == true) {
            // Get input from user and generate it to the machine
            if (isValidConfigurationTextFields()) {
                rotors = rotorsAndOrderTextField.getText();
                startingPositions = rotorsStartingPosTextField.getText();
                plugBoardPairs = plugBoardPairsTextField.getText();
                reflectorID = reflectorChoiceBox.getValue();

                try {
                    AppController.getConsoleApp().initializeEnigmaCodeManually(rotors, startingPositions, plugBoardPairs, reflectorID);
                    setCodeLabel.setText("Manually initialized configuration code.");
                } catch (NumberFormatException e) {
                    setCodeLabel.setText("Non-numeric value was inserted in 'Rotors And Order'.");
                    return false;
                } catch (InvalidRotorException | InvalidReflectorException | InvalidPlugBoardException |
                         InvalidCharactersException e) {
                    setCodeLabel.setText(e.getLocalizedMessage());
                    return false;
                } catch (NullPointerException | InputMismatchException | IllegalArgumentException e) {
                    setCodeLabel.setText(e.getLocalizedMessage());
                    return false;
                } catch (Exception e) {
                    setCodeLabel.setText(e.getLocalizedMessage());
                    return false;
                }
            }
        }
        else {
            AppController.getConsoleApp().initializeEnigmaCodeAutomatically();
            setCodeLabel.setText("Automatically initialized configuration code.");
        }
        String machineCode = AppController.getConsoleApp().getMachineHistoryStates().getCurrentMachineCode();
        updateMachineStatesAndDisability(machineCode, false);

        machineConfigurationLabel2.setText(Integer.toString(AppController.getConsoleApp().getEngine().getEngineDTO().getSelectedRotors().size()));
        machineConfigurationLabel4.setText(AppController.getConsoleApp().getEngine().getEngineDTO().getSelectedReflector());
        machineConfigurationMessageCounterLabel.setText(Integer.toString(AppController.getConsoleApp().getEngine().getEngineDTO().getMessagesSentCounter()));
        return true;
    }

    public void updateMachineStatesAndDisability(String machineStateString, boolean bool) {
        mainController.updateScreensDisability(bool);
        mainController.initializeMachineStates(machineStateString);
        firstMachineStateLabel.setText(machineStateString);
        currentMachineStateLabel.setText(machineStateString);
        resetMachineStateButton.setDisable(bool);
    }

    private boolean isValidConfigurationTextFields() {
        if (rotorsAndOrderTextField.getText().equals("")) {
            setCodeLabel.setText("You did not add your rotors' IDs and their order.");
            return false;
        }
        else if (rotorsStartingPosTextField.getText().equals("")) {
            setCodeLabel.setText("You did not add your rotors' starting positions.");
            return false;
        }
        else if (plugBoardPairsTextField.getText().trim().length() % 2 == 1) {
            setCodeLabel.setText("Enter an even number of plug board pairs values.");
            return false;
        }
        return true;
    }

    private void updateConfigurationFieldsAndMachineStateDisability() {
        rotorsAndOrderTextField.setText("");
        rotorsStartingPosTextField.setText("");
        plugBoardPairsTextField.setText("");
        reflectorChoiceBox.setValue(reflectorChoiceBox.getItems().get(0));
        setMachineStateDisability(false);
    }

    @FXML
    void resetMachineStateButtonActionListener(ActionEvent event) {
        AppController.getConsoleApp().resetMachine();
        mainController.resetScreens();
    }

    public void updateScreenOne(List<String> choiceBoxItems, String numberOfRotors, String numberOfReflectors) {
        reflectorChoiceBox.setItems(FXCollections.observableArrayList(choiceBoxItems));
        reflectorChoiceBox.setValue(reflectorChoiceBox.getItems().get(0));

        machineConfigurationLabel1.setText(numberOfRotors);
        machineConfigurationLabel3.setText(numberOfReflectors);

        setConfigurationDisability(false);
    }
    public void reset() {
        setConfigurationDisability(true);
        initializeMachineState();

        machineConfigurationLabel1.setText("NaN");
        machineConfigurationLabel2.setText("NaN");
        machineConfigurationLabel3.setText("NaN");
        machineConfigurationLabel4.setText("NaN");
        machineConfigurationMessageCounterLabel.setText("NaN");
    }

    public void updateMachineStateAndStatus(String currentMachineState) {
        machineConfigurationMessageCounterLabel.setText(Integer.toString(AppController.getConsoleApp().getMessageCounter()));
        currentMachineStateLabel.setText(currentMachineState);
        // TODO: update Enigma code current status section
    }

    public void resetMachineStateAndStatus() {
        setCodeLabel.setText("Machine state has been successfully reset");
        currentMachineStateLabel.setText(firstMachineStateLabel.getText());
        // TODO: update Enigma code current status section
    }
}

// TODO: add this framework: https://stackoverflow.com/posts/12862613/revisions
// TODO: make labels selectable and clickable: https://stackoverflow.com/a/44182371/3598990
// TODO: check back-end response to reset