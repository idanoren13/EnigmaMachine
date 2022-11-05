package frontEnd.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;

import java.io.IOException;

import static utils.Constants.READY;

public class ContestScreenController {
    private AppController mainController;
    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void onReadyButton(ActionEvent actionEvent) {
        String url = HttpUrl.parse(READY).newBuilder()
                .addQueryParameter("name", mainController.getAllyName())
                .addQueryParameter("entity", "ally")
                .addQueryParameter("uboatName", mainController.getSelectedContest().getUBoatName())
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, "Failed to start contest.").show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.INFORMATION, "Contest started.").show();
                    });
                } else {
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.ERROR, "Failed to start contest.").show();
                    });
                }
            }
        });
    }
}
