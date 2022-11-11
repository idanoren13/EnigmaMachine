import enigmaEngine.EnigmaMachineFromXML;
import frontEnd.controllers.TaskController;
import immutables.AllyDTO;
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

import static utils.Constants.GET_ENIGMA_ENGINE;

public class test {
    private TaskController taskController = new TaskController();
    private AllyDTO selectedAlly;

    private void getXmlEnigma() {
        String url = HttpUrl.parse(GET_ENIGMA_ENGINE).newBuilder()
                .addQueryParameter("allyName", "dfbd")
                .build()
                .toString();

        InputStream res = null;

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
//                    String responseString = response.body().ByteStream();
//                    Gson gson = new Gson();
//                    EnginePartsDTO enginePartsDTO = gson.fromJson(responseString, EnginePartsDTO.class);
//                    taskController.setEnigmaEngine(new EnigmaEngineImpl(enginePartsDTO));

                    try {
                        String responseString = response.body().string();
//                        System.out.println("response.body().ByteStream() = " + responseString);
                        InputStream inputStream = new ByteArrayInputStream(responseString.getBytes(StandardCharsets.UTF_8));
                        taskController.setEnigmaEngine(new EnigmaMachineFromXML().getEnigmaEngineFromInputStream(inputStream));
                    } catch (JAXBException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        test t = new test();
        t.getXmlEnigma();
    }
}
