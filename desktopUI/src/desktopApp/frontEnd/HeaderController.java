package desktopApp.frontEnd;

import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.Reflector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HeaderController implements Initializable {
    private AppController mainController;
    private String currXMLFilePath = "";
    @FXML private TextField xmlFilePathTextField;

    @FXML private Label loadXMLErrorLabel;

    @FXML private ChoiceBox<String> styleChoiceBox;

    @FXML private ChoiceBox<String> animationChoiceBox;

    @FXML private Button machineDetailsButton;

    @FXML private Button decryptInputWithEnigmaButton;

    @FXML private Button bruteForceButton;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Default values are added in certain screen places. This is called after constructor and after FXML variables are created.
        machineDetailsButton.getStyleClass().add("chosen-button");

        styleChoiceBox.getItems().addAll("Style #1", "Style #2", "Style #3");
        styleChoiceBox.setValue("Style #1");
        animationChoiceBox.getItems().addAll("No Animation", "Animation");
        animationChoiceBox.setValue("No Animation");
    }

    @FXML
    void machineDetailsButtonActionListener(ActionEvent event) {
        machineDetailsButton.getStyleClass().add("chosen-button");
        decryptInputWithEnigmaButton.getStyleClass().remove("chosen-button");
        bruteForceButton.getStyleClass().remove("chosen-button");
        mainController.changeToScreen1();
    }

    @FXML
    void decryptInputWithEnigmaButtonActionListener(ActionEvent event) {
        machineDetailsButton.getStyleClass().remove("chosen-button");
        decryptInputWithEnigmaButton.getStyleClass().add("chosen-button");
        bruteForceButton.getStyleClass().remove("chosen-button");
        mainController.changeToScreen2();
    }

    @FXML
    void bruteForceButtonActionListener(ActionEvent event) {
        machineDetailsButton.getStyleClass().remove("chosen-button");
        decryptInputWithEnigmaButton.getStyleClass().remove("chosen-button");
        bruteForceButton.getStyleClass().add("chosen-button");
        mainController.changeToScreen3();
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

                AppController.getConsoleApp().readMachineFromXMLFile(filePath);
                xmlFilePathTextField.setText(filePath);
                currXMLFilePath = filePath;

                // Update reflector choice box options
                List<Reflector.ReflectorID> unsortedReflectors = AppController.getConsoleApp().getEngine().getReflectors();
                Collections.sort(unsortedReflectors);
                mainController.updateScreenOne(
                        unsortedReflectors.stream().map(String::valueOf).collect(Collectors.toList()),
                        Integer.toString(AppController.getConsoleApp().getEngine().getEngineDTO().getTotalNumberOfRotors()),
                        Integer.toString(AppController.getConsoleApp().getEngine().getEngineDTO().getTotalReflectors())
                );
                mainController.initializeMachineStates("NaN");
                mainController.updateScreensDisability(true);

                loadXMLErrorLabel.setText("Machine XML file successfully loaded.");
            }
        } catch (NullPointerException e) { // If a user exits XML file search
            // Continue...
        } catch (FileAlreadyExistsException e) {
            loadXMLErrorLabel.setText(e.getLocalizedMessage());
        } catch (InvalidMachineException | JAXBException | InvalidRotorException | IOException
                 | InvalidABCException | UnknownSourceException | InvalidReflectorException e) {
            loadXMLErrorLabel.setText(e.getLocalizedMessage());
            mainController.reset();
            mainController.initializeMachineStates("NaN");
            mainController.updateScreensDisability(true);
            currXMLFilePath = "";
        }
    }

    // TODO: Implement bonus method 1 (change CSS of screen) and bonus method 2 (add animations)
}
