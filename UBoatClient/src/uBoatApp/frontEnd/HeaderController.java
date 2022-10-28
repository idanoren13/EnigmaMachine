package uBoatApp.frontEnd;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import okhttp3.*;
import uBoatApp.util.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static utils.Constants.UPLOAD_FILE;


public class HeaderController implements Initializable {
    private AppController mainController;
    @FXML private HBox headerHBox;
    private String currXMLFilePath = "";

    // private final IntegerProperty chosenButton = new SimpleIntegerProperty();
    @FXML private TextField xmlFilePathTextField;

    @FXML private Label loadXMLErrorLabel;

    @FXML private ChoiceBox<String> styleChoiceBox;

    @FXML private ChoiceBox<String> animationChoiceBox;

    @FXML private Button machineButton;

    @FXML private Button decryptInputWithEnigmaButton;

    @FXML private Button constestButton;




    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Default values are added in certain screen places. This is called after constructor and after FXML variables are created.
        machineButton.getStyleClass().add("chosen-button");

//        styleChoiceBox.getItems().addAll("Style #1", "Style #2", "Style #3");
//        styleChoiceBox.setValue("Style #1");
//        animationChoiceBox.getItems().addAll("No Animation", "Animation");
//        animationChoiceBox.setValue("No Animation");

//        styleChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
//        });
//
//        animationChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
//        });
    }

    @FXML
    void machineDetailsButtonActionListener() {
        machineButton.getStyleClass().add("chosen-button");
        decryptInputWithEnigmaButton.getStyleClass().remove("chosen-button");
        constestButton.getStyleClass().remove("chosen-button");
        mainController.changeToScreen1();
    }


    @FXML
    void loadXML() {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Pick your XML file for Ex2.");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

            File newXMLFile = fc.showOpenDialog(null);
            String filePath = newXMLFile.getAbsolutePath();

            MediaType mediaType = MediaType.parse("application/xml");
//            RequestBody body = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("file", filePath, RequestBody.create(mediaType, new File(filePath)))
//                    .build();

            HttpClientUtil.runAsync(UPLOAD_FILE, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Platform.runLater(() -> {
                        loadXMLErrorLabel.setText("Error: " + e.getMessage());
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            loadXMLErrorLabel.setText("XML file loaded successfully.");
                            currXMLFilePath = filePath;
                            xmlFilePathTextField.setText(currXMLFilePath);
                        } else {
                            loadXMLErrorLabel.setText("Error: " + response.message());
                        }
                    });
                }
            });

//            Request request =new Request.Builder()
//                    .url(UPLOAD_FILE)
//                    .post(body)
//                    .build();
//
//            Callback callback = new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        Platform.runLater(() -> {
//
//                        });
//                    }
//                }
//            };

        } catch (Exception e) {
            loadXMLErrorLabel.setText("Error loading XML file.");
        }
    }
    public void updateLabelTextsToEmpty() {
        loadXMLErrorLabel.setText("");
    }



    public void updateStylesheet(Number num) {
        headerHBox.getStylesheets().remove(0, 2);
        if (num.equals(0)) {
            headerHBox.getStylesheets().add("/uBoatApp/frontEnd/css/headerStyleOne.css");
            headerHBox.getStylesheets().add("/uBoatApp/frontEnd/css/generalStyleOne.css");
        } else if (num.equals(1)) {
            headerHBox.getStylesheets().add("/uBoatApp/frontEnd/css/headerStyleTwo.css");
            headerHBox.getStylesheets().add("/uBoatApp/frontEnd/css/generalStyleOne.css");
        } else {
            headerHBox.getStylesheets().add("/uBoatApp/frontEnd/css/headerStyleThree.css");
            headerHBox.getStylesheets().add("/uBoatApp/frontEnd/css/generalStyleOne.css");
        }
    }

    public void contestButtonActionListener(ActionEvent actionEvent) {

    }
}
