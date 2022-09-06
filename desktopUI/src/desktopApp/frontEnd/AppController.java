package desktopApp.frontEnd;

import desktopApp.impl.Console;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.util.List;

public class AppController {
    static private Console consoleApp;
    @FXML private FlowPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private StackPane stackPaneContainer;
    @FXML private ScrollPane screen1Component;
    @FXML private Screen1Controller screen1ComponentController;
    @FXML private ScrollPane screen2Component;
    @FXML private Screen2Controller screen2ComponentController;
    @FXML private ScrollPane screen3Component;
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

    public void setHeaderComponentController(HeaderController headerComponentController) {
        this.headerComponentController = headerComponentController;
        headerComponentController.setMainController(this);
    }

    public void setScreen1ComponentController(Screen1Controller screen1ComponentController) {
        this.screen1ComponentController = screen1ComponentController;
        screen1ComponentController.setMainController(this);
    }

    public void setScreen2ComponentController(Screen2Controller screen2ComponentController) {
        this.screen2ComponentController = screen2ComponentController;
        screen2ComponentController.setMainController(this);
    }

    public void setScreen3ComponentController(Screen3Controller screen3ComponentController) {
        this.screen3ComponentController = screen3ComponentController;
        screen3ComponentController.setMainController(this);
    }

    public void updateScreens(String currentMachineState) {
        screen1ComponentController.updateMachineStateAndStatus(currentMachineState);
        screen2ComponentController.updateMachineStateAndStatistics(currentMachineState);
        screen3ComponentController.updateMachineState(currentMachineState);
    }

    public void reset() {
        screen1ComponentController.reset();
    }
    public void resetScreens() {
        screen1ComponentController.resetMachineStateAndStatus();
        screen2ComponentController.resetMachineStateAndStatistics();
        screen3ComponentController.resetMachineState();
    }
    public void updateScreenOne(List<String> choiceBoxItems, String numberOfRotors, String numberOfReflectors) {
        screen1ComponentController.updateScreenOne(choiceBoxItems, numberOfRotors, numberOfReflectors);
    }
    public void initializeMachineStates(String machineStateString) {
        screen2ComponentController.initializeMachineStates(machineStateString);
        screen3ComponentController.initializeMachineStates(machineStateString);
    }

    public void updateScreensDisability(boolean bool) {
        screen2ComponentController.setEnigmaDecryptionInputDisability(bool);
        screen3ComponentController.setBruteForceDisability(bool);
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
