package uBoatApp.frontEnd.controllers;


import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import uBoatApp.MachineStateConsole;
import utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.EventListener;
import java.util.InputMismatchException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import static utils.Constants.*;

public class ContestScreenController implements Initializable {

    private AppController mainController;
    // Models
    //
    @FXML
    private VBox bruteForceVBox;
    // Machine states
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

    boolean existingInput = false;

    EventListener onXMLLoaded;
    private MachineStateConsole machineStatesConsole;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Only for binding the ENTER key to the input text field
        enterCurrentKeyboardInputButton.setOnAction(this::enterCurrentKeyboardInputButtonActionListener);
        keyboardInputVBox.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                enterCurrentKeyboardInputButton.fire();
                ev.consume();
            }
        });

        searchDictionaryWordsListView.onMousePressedProperty().addListener((observable, oldValue, newValue) -> {
            String selectedWord = searchDictionaryWordsListView.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                inputToEncryptDecryptInput.setText(selectedWord);
            }
        });

        searchDictionaryWordsListView.editableProperty().setValue(true);
        searchDictionaryWordsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selectedWord = searchDictionaryWordsListView.getSelectionModel().getSelectedItem();
            if (selectedWord != null && inputToEncryptDecryptInput.getText() != null) {
                inputToEncryptDecryptInput.setText(inputToEncryptDecryptInput.getText() + selectedWord + " "); // Added " "
            }
        });

    }

    public void setBruteForceDisability(boolean bool) {
        bruteForceVBox.setDisable(bool);
    }

    @FXML
    void enterCurrentKeyboardInputButtonActionListener(ActionEvent event) {
        try {
            String messageInput = inputToEncryptDecryptInput.getText().toUpperCase();

            resetEnigmaEngine();

            if (messageInput.equals("")) {
                throw new InputMismatchException("No encryption message was written.");
            }
            if (messageInput.charAt(messageInput.length() - 1) == ' ') {
                messageInput = messageInput.substring(0, messageInput.length() - 1);
            }

            new Alert(Alert.AlertType.CONFIRMATION, "Processed message: " + messageInput + " -> ");
            mainController.updateLabelTextsToEmpty(this);
            existingInput = true;

            encryptInput(messageInput);

        } catch (InputMismatchException e) {
            new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage()).show();
        }
    }

    private void resetEnigmaEngine() {
        String url = HttpUrl.parse(RESET_ENIGMA).newBuilder()
                .addQueryParameter("name", mainController.getName())
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Failed to reset the Enigma engine.").show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Failed to reset the Enigma engine.").show());
                }
            }
        });
    }

    private void encryptInput(String message) {
        String url = HttpUrl.parse(PROCESS_MESSAGE).newBuilder()
                .addQueryParameter("name", mainController.getName())
                .addQueryParameter("message", message)
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, "Error while encrypting message.").show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        Gson gson = new Gson();
                        String responseArray = gson.fromJson(responseString, String.class);
                        enigmaOutputTextField.setText(responseArray);
                    });
                } else {
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.ERROR, "Error while encrypting message.").show();
                    });
                }
            }
        });


    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


    public void updateMachineState(String currentMachineState) {
    }

    public void resetMachineStateAndEnigmaOutput(boolean bool, Object controller) {
        if (bool && controller == null) {
            new Alert(Alert.AlertType.INFORMATION, "Machine state has been successfully reset.").show();
        }
        enigmaOutputTextField.setText("NaN");
    }

    public void updateLabelTextsToEmpty() {
        inputToEncryptDecryptInput.setText("");
    }

    public void initializeMachineStates(String machineStateConsoleString) {
        machineStatesConsole.setFirstMachineState(machineStateConsoleString);
        machineStatesConsole.setCurrentMachineState(machineStateConsoleString);
    }

    public void getWordsDictionary() {
        String url = HttpUrl.parse(GET_WORDS_DICTIONARY).newBuilder()
                .addQueryParameter("name", mainController.getName())
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
                        searchDictionaryWordsListView.getItems().addAll(words.stream().map(String::trim)
                                .sorted().collect(Collectors.toList()));
                    });
                }
            }
        });
    }
}
