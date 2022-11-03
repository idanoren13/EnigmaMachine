package uBoatApp.frontEnd.controllers;


import com.google.gson.Gson;
import immutables.engine.EngineDTO;
import immutables.engine.ReflectorID;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import okhttp3.*;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static utils.HttpClientUtil.HTTP_CLIENT;
import static utils.Constants.UPLOAD_FILE;


public class HeaderController implements Initializable {
    public Button contestButton;
    public Button loadXMLButton;
    public TextArea name;
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
        machineButton.getStyleClass().add("chosen-button");
        contestButton.setDisable(true);
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

            //            MediaType mediaType = MediaType.parse("application/xml");
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", newXMLFile.getName(), RequestBody.create(MediaType.parse("application/xml"), newXMLFile))
                    .build();

            HttpUrl url = HttpUrl.parse(UPLOAD_FILE).newBuilder()
                    .addQueryParameter("name", name.getText())
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = HTTP_CLIENT.newCall(request).execute();
            String responseString = response.body().string();
            System.out.println(responseString);
            currXMLFilePath = filePath;

            Gson gson = new Gson();
            EngineDTO engineDTO = gson.fromJson(responseString, EngineDTO.class);

            //create a list of all reflectors in size of the number of reflectors in the engine DTO
            List<ReflectorID> reflectors = new ArrayList<>(Arrays.asList(ReflectorID.values()).subList(0, engineDTO.getTotalReflectors()));
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

    public void enableContestScreen() {
        contestButton.setDisable(false);
    }

    public void updateName(String i_name) {
        name.setText(i_name);
    }


    public void setLoadXMLButtonDisable(boolean b) {
        loadXMLButton.setDisable(b);
    }
}
