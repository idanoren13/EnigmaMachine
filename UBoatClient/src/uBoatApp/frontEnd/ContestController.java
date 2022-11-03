package uBoatApp.frontEnd;


import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import uBoatApp.frontEnd.machineState.MachineStateConsole;
import uBoatApp.util.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;
import java.util.Set;

import static utils.Constants.GET_WORDS_DICTIONARY;

public class ContestController implements Initializable {
    @FXML
    public TableView candidatesTable;

    @FXML
    public TableView alliesDetailsTable;
    public ScrollPane contest;
    // Main component
    private AppController mainController;
    // Models
    private MachineStateConsole machineStatesConsole;
    //
    @FXML
    private VBox bruteForceVBox;
    // Machine states
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Init machine states console
        machineStatesConsole = new MachineStateConsole(candidatesTable, alliesDetailsTable);
        // Init search dictionary words list view
        searchDictionaryWordsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        searchDictionaryWordsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                inputToEncryptDecryptInput.setText(newValue);
            }
        });
        // Init input to encrypt / decrypt
        inputToEncryptDecryptInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                enigmaOutputTextField.setText(newValue);
            }
        });
        // Init enter current keyboard input button
        enterCurrentKeyboardInputButton.setOnAction(event -> {
            if (inputToEncryptDecryptInput.getText() != null) {
                enigmaOutputTextField.setText(inputToEncryptDecryptInput.getText());
            }
        });

    }

//    private void initializeDictionary(){
//        searchInputTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            searchDictionaryWordsListView.getItems().clear();
//            searchDictionaryWordsListView.getItems().addAll( // Sorted dictionary list view
//                    mainController.getDictionary().stream().map((word) -> word.trim())
//                            .sorted().collect(Collectors.toList()));
//        });
//
//
//
//        searchDictionaryWordsListView.onMousePressedProperty().addListener((observable, oldValue, newValue) -> {
//            String selectedWord = searchDictionaryWordsListView.getSelectionModel().getSelectedItem();
//            if (selectedWord != null) {
//                inputToEncryptDecryptInput.setText(selectedWord);
//            }
//        });
//    }

    public void setBruteForceDisability(boolean bool) {
        bruteForceVBox.setDisable(bool);
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
//            messageOutput = AppController.getConsoleApp().getMessageAndProcessIt(messageInput, true);
//
//            new Alert(Alert.AlertType.CONFIRMATION, "Processed message: " + messageInput + " -> " + messageOutput).show();
//            enigmaOutputTextField.setText(messageOutput);
//            mainController.updateScreens(AppController.getConsoleApp().getCurrentMachineState());
//            mainController.updateLabelTextsToEmpty(this);
//            existingInput = true;
        } catch ( InputMismatchException e) {
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
    }

    public void getWordsDictionary() {
        String url = HttpUrl.parse(GET_WORDS_DICTIONARY).newBuilder()
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    String json = response.body().string();
                    Set<String> words = gson.fromJson(json, Set.class);
                    System.out.println(words);
                    Platform.runLater(() -> {
                        searchDictionaryWordsListView.getItems().clear();
//                        searchDictionaryWordsListView.getItems().addAll(words.stream().map(String::trim)
//                                .sorted().collect(Collectors.toList()));
                        searchDictionaryWordsListView.getItems().add("test");
                    });
                }
            }
        });
    }
}
