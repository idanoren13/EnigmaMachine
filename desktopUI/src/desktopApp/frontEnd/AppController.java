package desktopApp.frontEnd;

import desktopApp.impl.Console;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

import java.util.List;

public class AppController {
    static private Console consoleApp;
    @FXML private ScrollPane screen1Component;
    @FXML private ScrollPane screen2Component;
    @FXML private ScrollPane screen3Component;
    @FXML private HeaderController headerComponentController;
    @FXML private Screen1Controller screen1ComponentController;
    @FXML private Screen2Controller screen2ComponentController;
    @FXML private Screen3Controller screen3ComponentController;

    @FXML
    public void initialize() {
        if (headerComponentController != null && screen1ComponentController != null &&
                screen2ComponentController != null && screen3ComponentController != null) {
            consoleApp = new Console();
            headerComponentController.setMainController(this);
            screen1ComponentController.setMainController(this);
            screen2ComponentController.setMainController(this);
            screen3ComponentController.setMainController(this);
        }
    }

    public static Console getConsoleApp() {
        return consoleApp;
    }

    public void updateScreens(String currentMachineState) {
        screen1ComponentController.updateMachineStateAndStatus(currentMachineState);
        screen2ComponentController.updateMachineStateAndStatistics(currentMachineState);
        screen3ComponentController.updateMachineState(currentMachineState);
    }

    public void reset() {
        screen1ComponentController.reset();
    }
    public void resetScreens(boolean bool, Object controller) {
        screen1ComponentController.resetMachineStateAndStatus();
        screen2ComponentController.resetMachineStateAndStatistics(bool);
        screen3ComponentController.resetMachineStateAndEnigmaOutput(bool, controller);
    }
    public void updateScreenOne(List<String> choiceBoxItems, String numberOfRotors, String numberOfReflectors) {
        screen1ComponentController.updateScreenOne(choiceBoxItems, numberOfRotors, numberOfReflectors);
    }
    public void initializeMachineStates(String machineStateConsoleString) {
        screen2ComponentController.initializeMachineStates();
        screen3ComponentController.initializeMachineStates(machineStateConsoleString);
    }

    public void updateScreensDisability(boolean bool) {
        screen2ComponentController.setEnigmaDecryptionInputDisability(bool);
        screen3ComponentController.setBruteForceDisability(bool);
    }

    public void updateLabelTextsToEmpty() {
        headerComponentController.updateLabelTextsToEmpty();
        screen1ComponentController.updateLabelTextsToEmpty();
        screen2ComponentController.updateLabelTextsToEmpty();
        screen3ComponentController.updateLabelTextsToEmpty();
    }

    public void updateAmountAgents(int amountAgents) {
        screen3ComponentController.updateAmountAgents(amountAgents);
    }

    // Swap screens
    public void changeToScreen1() {
        screen1Component.toFront();
    }
    public void changeToScreen2() {
        screen2Component.toFront();
    }
    public void changeToScreen3() {
        screen3Component.toFront();
    }
}
