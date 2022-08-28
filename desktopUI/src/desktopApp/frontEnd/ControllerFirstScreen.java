package desktopApp.frontEnd;

import desktopApp.impl.Console;
import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.Reflector;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class ControllerFirstScreen implements Initializable {
    Console consoleApp;
    private String currXMLFilePath = "";

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TextField xmlFilePathTextField;

    @FXML
    private Button machineDetailsButton;

    @FXML
    private Button decryptInputWithEnigmaButton;

    @FXML
    private Button bruteForceButton;

    @FXML
    private ChoiceBox<String> styleChoiceBox;

    @FXML
    private ChoiceBox<String> animationChoiceBox;

    @FXML
    private Label loadXMLErrorLabel;

    @FXML
    private Label setCodeLabel;

    @FXML
    private Label machineConfigurationLabel1;

    @FXML
    private Label machineConfigurationLabel2;

    @FXML
    private Label machineConfigurationLabel3;

    @FXML
    private Label machineConfigurationLabel4;

    @FXML
    private Label machineConfigurationLabel5;

    @FXML
    private TextField rotorsAndOrderTextField;

    @FXML
    private TextField rotorsStartingPosTextField;

    @FXML
    private Label firstMachineStateLabel;

    @FXML
    private TextField plugBoardPairsTextField;

    @FXML
    private ChoiceBox<String> reflectorChoiceBox;

    @FXML
    private Button generateRandomCodeButton;

    @FXML
    private Button setCodeButton;

    @FXML
    private Label currentMachineStateLabel;

    @FXML
    private Button resetMachineStateButton;

    public ControllerFirstScreen() {
        consoleApp = new Console();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Default values are added in certain screen places. This is called after constructor and after FXML variables are created.
        machineDetailsButton.getStyleClass().remove("menu-button");

        initializeUIUXChoiceBoxes();

        setConfigurationDisability(true);
        setMachineStateDisability(true);
        firstMachineStateLabel.setText("NaN");
        currentMachineStateLabel.setText("NaN");
    }

    private void initializeUIUXChoiceBoxes() {
        styleChoiceBox.getItems().addAll("Style #1", "Style #2", "Style #3");
        styleChoiceBox.setValue("Style #1");
        animationChoiceBox.getItems().addAll("No Animation", "Animation");
        animationChoiceBox.setValue("No Animation");
    }

    @FXML
    void loadXML(ActionEvent event) {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Pick your XML file for Ex2.");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

            File newXMLFile = fc.showOpenDialog(null);
            String filePath = newXMLFile.getAbsolutePath();
            if (filePath.equals(currXMLFilePath)) {
                throw new FileAlreadyExistsException("File given is already defined as the Enigma machine.");
            } else if (!filePath.equals("")) {
                xmlFilePathTextField.setText(filePath);
                consoleApp.readMachineFromXMLFile(filePath);
                currXMLFilePath = filePath;

                // Update reflector choice box options
                List<Reflector.ReflectorID> unsortedReflectors = consoleApp.getEngine().getReflectors();
                Collections.sort(unsortedReflectors);
                reflectorChoiceBox.setItems(FXCollections.observableArrayList(unsortedReflectors.stream().map(String::valueOf).collect(Collectors.toList())));
                reflectorChoiceBox.setValue(reflectorChoiceBox.getItems().get(0));

                machineConfigurationLabel1.setText(Integer.toString(consoleApp.getEngine().getEngineDTO().getTotalNumberOfRotors()));
                machineConfigurationLabel3.setText(Integer.toString(consoleApp.getEngine().getEngineDTO().getTotalReflectors()));


                setConfigurationDisability(false);
                loadXMLErrorLabel.setText("Machine successfully initialized");
            }
        } catch (NullPointerException e) { // If a user exits XML file search
            // Continue...
        } catch (FileAlreadyExistsException e) {
            loadXMLErrorLabel.setText(e.getLocalizedMessage());
        } catch (InvalidMachineException | JAXBException | InvalidRotorException | IOException
                 | InvalidABCException | UnknownSourceException | InvalidReflectorException e) {
            loadXMLErrorLabel.setText(e.getLocalizedMessage());
        }
    }
    @FXML
    void getConfigurationFromUser(ActionEvent event) {

        initializeEnigmaCode(true);
        updateConfigurationFieldsAndMachineStateDisability();
    }

    @FXML
    void setConfigurationRandomly(ActionEvent event) {

        initializeEnigmaCode(false);
        updateConfigurationFieldsAndMachineStateDisability();
    }

    private void initializeEnigmaCode(boolean isManual) {
        String rotors, startingPositions, plugBoardPairs, reflectorID;
        if (isManual == true) {
            // Get input from user and generate it to the machine
            if (isValidConfigurationTextFields()) {
                rotors = rotorsAndOrderTextField.getText();
                startingPositions = rotorsStartingPosTextField.getText();
                plugBoardPairs = plugBoardPairsTextField.getText();
                reflectorID = reflectorChoiceBox.getValue();

                try {
                    consoleApp.initializeEnigmaCodeManually(rotors, startingPositions, plugBoardPairs, reflectorID);
                    setCodeLabel.setText("Manually initialized configuration code.");
                } catch (NumberFormatException e) {
                    setCodeLabel.setText("Non-numeric value was inserted in 'Rotors And Order'.");
                } catch (InvalidRotorException | InvalidReflectorException | InvalidPlugBoardException |
                         InvalidCharactersException e) {
                    setCodeLabel.setText(e.getLocalizedMessage());
                } catch (NullPointerException | InputMismatchException | IllegalArgumentException e) {
                    setCodeLabel.setText(e.getLocalizedMessage());
                } catch (Exception e) {
                    setCodeLabel.setText(e.getLocalizedMessage());
                }
            }
        }
        else {
            consoleApp.initializeEnigmaCodeAutomatically();
            setCodeLabel.setText("Automatically initialized configuration code.");
        }
        firstMachineStateLabel.setText(consoleApp.getMachineHistoryStates().getCurrentMachineCode());
        currentMachineStateLabel.setText(consoleApp.getMachineHistoryStates().getCurrentMachineCode());

        machineConfigurationLabel2.setText(Integer.toString(consoleApp.getEngine().getEngineDTO().getSelectedRotors().size()));
        machineConfigurationLabel4.setText(consoleApp.getEngine().getEngineDTO().getSelectedReflector());
        machineConfigurationLabel5.setText(Integer.toString(consoleApp.getEngine().getEngineDTO().getMessagesSentCounter()));
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
        else if (plugBoardPairsTextField.getText().equals("")) {
            setCodeLabel.setText("You did not add your plug board pairs.");
            return false;
        }
        else if (plugBoardPairsTextField.getText().trim().length() % 2 == 1) {
            setCodeLabel.setText("Enter an even number of values.");
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

    private void setConfigurationDisability(boolean bool) {
        rotorsAndOrderTextField.setDisable(bool);
        rotorsStartingPosTextField.setDisable(bool);
        plugBoardPairsTextField.setDisable(bool);
        reflectorChoiceBox.setDisable(bool);
        generateRandomCodeButton.setDisable(bool);
        setCodeButton.setDisable(bool);
    }

    private void setMachineStateDisability(boolean bool) {
        resetMachineStateButton.setDisable(bool);
    }

    @FXML
    void machineDetailsButtonActionListener(ActionEvent event) {
        machineDetailsButton.getStyleClass().remove("menu-button");
    }

    @FXML
    void decryptInputWithEnigmaButtonActionListener(ActionEvent event) {
        decryptInputWithEnigmaButton.getStyleClass().remove("menu-button");
    }

    @FXML
    void bruteForceButtonActionListener(ActionEvent event) {
        bruteForceButton.getStyleClass().remove("menu-button");
    }

    @FXML
    void resetMachineStateButtonActionListener(ActionEvent event) {
        consoleApp.resetMachine();
        setCodeLabel.setText("Machine state has been successfully reset");
    }

    @FXML
    void styleChoiceBoxActionListener(MouseEvent event) { // TODO: Bonus method
        styleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> updateCSS(newValue));
    }

    private void updateCSS(String newValue) { // TODO: Bonus method
        mainBorderPane.getStyleClass().removeAll();
        if (newValue.equals("Style #1")) {
            mainBorderPane.getStyleClass().add("enigmaScreensStyleOne");
        }
        else if (newValue.equals("Style #2")) {
            mainBorderPane.getStyleClass().add("enigmaScreensStyleTwo");
        }
        else if (newValue.equals("Style #3")) {
            styleChoiceBox.getStyleClass().add("enigmaScreensStyleThree");
        }
        else {
            // Exception...
        }
    }

    @FXML
    void animationChoiceBoxActionListener(MouseEvent event) { // TODO: Bonus method

    }
}
