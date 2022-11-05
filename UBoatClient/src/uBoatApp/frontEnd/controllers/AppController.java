package uBoatApp.frontEnd.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

import java.util.List;

public class AppController {
    @FXML
    public ScrollPane login;
    @FXML
    private ScrollPane screen1Component;
    @FXML
    private ScrollPane screen3Component;

    @FXML
    private HeaderController headerComponentController;
    @FXML
    private MachineConfigurationController screen1ComponentController;
    @FXML
    private ContestScreenController screen3ComponentController;

    @FXML
    private LoginController loginController;

    private String uboatName;

    public static void initializeEnigmaCodeManually(String rotors, String startingPositions, String plugBoardPairs, String reflectorID) {
        //set engine in the server

    }

    @FXML
    public void initialize() {
        if (headerComponentController != null && screen1ComponentController != null && screen3ComponentController != null && loginController != null) {
            headerComponentController.setMainController(this);
            screen1ComponentController.setMainController(this);
            screen3ComponentController.setMainController(this);
            loginController.setMainController(this);
        }

        headerComponentController.setLoadXMLButtonDisable(true);
    }

    private void hideScreens() {
        screen1ComponentController.mainScrollPane.setVisible(false);
    }


    public void updateScreens(String currentMachineState) {
        screen3ComponentController.updateMachineState(currentMachineState);
    }

    public void reset() {
        screen1ComponentController.reset();
    }

    public void resetScreens(boolean bool, Object controller) {
    }


    public void updateMachineStats(List<String> choiceBoxItems, String numberOfRotors, String numberOfReflectors) {
        screen1ComponentController.updateScreenOne(choiceBoxItems, numberOfRotors, numberOfReflectors);
    }

    public void initializeMachineStates(String machineStateConsoleString) {
        screen3ComponentController.initializeMachineStates(machineStateConsoleString);
    }

    public void updateScreensDisability(boolean bool) {
        screen3ComponentController.setBruteForceDisability(bool);
    }

    public void updateLabelTextsToEmpty(Object component) {
        headerComponentController.updateLabelTextsToEmpty();
        screen1ComponentController.updateLabelTextsToEmpty();
        screen3ComponentController.updateLabelTextsToEmpty();
    }

    // Swap screens
    public void changeToScreen1() {
        screen1Component.toFront();
    }

    public void changeToScreen3() {
        screen3Component.toFront();
    }

    public void enableContestScreen() {
        headerComponentController.enableContestScreen();
        screen3Component.setDisable(false);
        try {
            screen3ComponentController.getWordsDictionary();

        } catch (Exception e) {
            e.printStackTrace();
        }

        screen3ComponentController.startAlliesRefresher();
    }

    public void setName(String name) {
        uboatName = name;
    }

    public void endLogin() {
        login.setVisible(false);
        headerComponentController.setLoadXMLButtonDisable(false);
        headerComponentController.updateName(uboatName);
    }

    public String getName() {
        return uboatName;
    }
}
