package frontEnd.controllers;

import enigmaEngine.EnigmaMachineFromXML;
import enigmaEngine.MachineCode;
import immutables.AllyDTO;
import immutables.ContestDataDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.util.Pair;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static utils.Constants.GET_ENIGMA_ENGINE;

public class AgentAppController {
    @FXML
    public ScrollPane login;
    @FXML
    private ScrollPane contestComponent;
    @FXML
    private AgentContestScreenController contestComponentController;
    @FXML
    private AgentLoginController loginController;

    private String agentName;

    private AllyDTO selectedAlly;
    private int threadsNumber;
    private int missionSize;
    TaskController taskController;


    @FXML
    public void initialize() {
        if (contestComponentController != null && loginController != null) {
            contestComponentController.setMainController(this);
            loginController.setMainController(this);
        }
    }

    public void endLogin() {
        login.setVisible(false);
        taskController = new TaskController(threadsNumber,this);

    }

    public void setName(String userName) {
        agentName = userName;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAllyName() {
        return selectedAlly.getAllyName();
    }


    public void setAllyTeam(AllyDTO selectedAlly) {
        this.selectedAlly = selectedAlly;
    }

    public void setThreadsNumber(Integer value) {
        threadsNumber = value;
    }

    public void setMissionSize(int parseInt) {
        missionSize = parseInt;
    }

    public int getMissionSize() {
        return missionSize;
    }

    public void showCandidates(List<Pair<List<String>, MachineCode>> outputQueue) {
        contestComponentController.showCandidates(outputQueue);
    }

    public synchronized void startContest(ContestDataDTO contestDataDTO) {
        getXmlEnigma();

        taskController.initialize(contestDataDTO.getDifficulty(),contestDataDTO.getEncryptedText());
        taskController.start();
    }

    private void getXmlEnigma() {
        String url = HttpUrl.parse(GET_ENIGMA_ENGINE).newBuilder()
                .addQueryParameter("allyName", selectedAlly.getAllyName())
                .build()
                .toString();


        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to join contest").showAndWait();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
//                        System.out.println("response.body().ByteStream() = " + responseString);
                    InputStream inputStream = new ByteArrayInputStream(responseString.getBytes(StandardCharsets.UTF_8));
                    try {
                        taskController.setEnigmaEngine(new EnigmaMachineFromXML().getEnigmaEngineFromInputStream(inputStream));
                    } catch (JAXBException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void stopContest() {
        try {
            taskController.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startContestDataRefresher() {
        contestComponentController.startContestRefresher();
    }
}
