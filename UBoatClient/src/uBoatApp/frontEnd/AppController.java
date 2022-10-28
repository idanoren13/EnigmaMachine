package uBoatApp.frontEnd;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

import java.util.List;

public class AppController {
    @FXML private ScrollPane screen1Component;
    @FXML private ScrollPane screen3Component;
    @FXML private HeaderController headerComponentController;
    @FXML private MachineConfigurationController screen1ComponentController;
    @FXML private ConstestController constestController;
    private ObservableValue<? extends Number> progressProperty;

    public static void initializeEnigmaCodeManually(String rotors, String startingPositions, String plugBoardPairs, String reflectorID) {
        //set engine in the server

    }

    @FXML
    public void initialize() {
        if (headerComponentController != null && screen1ComponentController != null){
            headerComponentController.setMainController(this);
            screen1ComponentController.setMainController(this);

            constestController.setMainController(this);
//            setTasksManagerLogic(consoleApp.getBruteForceTaskManager());
        }
    }


    public void updateScreens(String currentMachineState) {
//        screen1ComponentController.updateMachineStateAndStatus(currentMachineState);
        constestController.updateMachineState(currentMachineState);
    }

    public void reset() {
        screen1ComponentController.reset();
    }
    public void resetScreens(boolean bool, Object controller) {
//        screen1ComponentController.resetMachineStateAndStatus();
        constestController.resetMachineStateAndEnigmaOutput(bool, controller);
    }
    public void updateScreenOne(List<String> choiceBoxItems, String numberOfRotors, String numberOfReflectors) {
        screen1ComponentController.updateScreenOne(choiceBoxItems, numberOfRotors, numberOfReflectors);
    }
    public void initializeMachineStates(String machineStateConsoleString) {
        constestController.initializeMachineStates(machineStateConsoleString);
    }

    public void updateScreensDisability(boolean bool) {
        constestController.setBruteForceDisability(bool);
    }

    public void updateLabelTextsToEmpty(Object component) {
        headerComponentController.updateLabelTextsToEmpty();
        screen1ComponentController.updateLabelTextsToEmpty();
        constestController.updateLabelTextsToEmpty();
    }

    public void updateDynamicKeyboardsAndAmountAgents(int amountAgents) {

        constestController.updateAmountAgents(amountAgents);
    }

    // Swap screens
    public void changeToScreen1() {
        screen1Component.toFront();
    }
    public void changeToScreen3() {
        screen3Component.toFront();
    }


//    public Set<String> getDictionary() {
//        return consoleApp.getWordsDictionary();
//    }

//    public void setDMProperties(int agents, int missionSize, Difficulty difficulty) {
//        consoleApp.setDMProperties(agents, missionSize, difficulty);
//    }
//
//    public void startResumeDM() {
//        consoleApp.startResumeDM();
////        progressProperty = consoleApp.getProgressProperty();
//
//    }
//
//    private void setProgressProperty(ObservableValue<String> progressProperty) {
//
//    }
//
//    public void setEncryptedText(String text) {
//        consoleApp.setEncryptedText(text);
////        consoleApp.getBruteForceTaskManager().setOnProgressChanged((progress) -> {
//////            screen3ComponentController.updateProgress(progress);
////        });
//    }
//
//    public void setTasksManagerLogic(TasksManager decryptionManagerLogic) {
//        constestController.setTasksManagerLogic(decryptionManagerLogic);
//        consoleApp.getBruteForceTaskManager().setOnCandidateWordsFound(constestController::updateCandidateWords);
//    }
//
//    public void stopDM() {
//        consoleApp.stopDM();
//    }
//
//    public ObservableValue<? extends Number> getProgressProperty() {
//        return progressProperty;
//    }
//
//    public void startDM() {
//        consoleApp.startDM();
//    }

//    public void setProgressProperty(ObservableValue<String> progressProperty) {
//        this.progressProperty = progressProperty;
//    }
}
