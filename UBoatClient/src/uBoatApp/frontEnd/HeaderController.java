package uBoatApp.frontEnd;


import com.google.gson.Gson;
import immutables.engine.EngineDTO;
import immutables.engine.ReflectorID;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import okhttp3.*;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static uBoatApp.util.HttpClientUtil.HTTP_CLIENT;
import static utils.Constants.UPLOAD_FILE;


public class HeaderController implements Initializable {
    public Button contestButton;
    private AppController mainController;
    @FXML
    private HBox headerHBox;
    private String currXMLFilePath = "";

    // private final IntegerProperty chosenButton = new SimpleIntegerProperty();
    @FXML
    private TextField xmlFilePathTextField;

    @FXML
    private Label loadXMLErrorLabel;


    @FXML
    private Button machineButton;


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
        contestButton.getStyleClass().remove("chosen-button");
        mainController.changeToScreen1();
    }

    @FXML
    void contestButtonActionListener() {
        contestButton.getStyleClass().add("chosen-button");
        machineButton.getStyleClass().remove("chosen-button");
        mainController.changeToScreen3();
    }


    @FXML
    void loadXML() {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Pick your XML file for Ex2.");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

            File newXMLFile = fc.showOpenDialog(null);
            String filePath = newXMLFile.getAbsolutePath();

            if (newXMLFile == null) {
                System.out.println("No file was chosen.");
            }

//            MediaType mediaType = MediaType.parse("application/xml");
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", newXMLFile.getName(), RequestBody.create(MediaType.parse("application/xml"), newXMLFile))
                    .build();

            Request request = new Request.Builder()
                    .url(UPLOAD_FILE)
                    .post(body)
                    .build();

//            Call call = HTTP_CLIENT.newCall(request);

            Response response = HTTP_CLIENT.newCall(request).execute();

            String responseString = response.body().string();

            System.out.println(responseString);

            currXMLFilePath = filePath;

            Gson gson = new Gson();
            EngineDTO engineDTO = gson.fromJson(responseString, EngineDTO.class);


            //create a list of all reflectors in size of the number of reflectors in the engine DTO
            List<ReflectorID> reflectors = new ArrayList<>(Arrays.asList(ReflectorID.values()).subList(0, engineDTO.getTotalReflectors()));

            Collections.sort(reflectors);
            mainController.updateMachineStats(
                    reflectors.stream().map(String::valueOf).collect(Collectors.toList()),
                    Integer.toString(engineDTO.getTotalNumberOfRotors()),
                    Integer.toString(engineDTO.getTotalReflectors())
            );

            mainController.initializeMachineStates("NaN");
            mainController.updateScreensDisability(true);
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
}
