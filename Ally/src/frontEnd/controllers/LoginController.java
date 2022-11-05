package frontEnd.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    public TextField userNameTextField;
    @FXML
    public Label errorMessageLabel;

    private AppController mainController;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }

    public void userNameKeyTyped(KeyEvent keyEvent) {
        errorMessageProperty.set("");
    }

    public void loginButtonClicked(ActionEvent actionEvent) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        addUser(userName);
        signUpAlly(userName);
    }

    private void addUser(String userName) {
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        mainController.setName(userName);
                        mainController.endLogin();
                    });
                }
            }
        });
    }

    private void signUpAlly(String userName) {
        String finalUrl = HttpUrl
                .parse(Constants.ADD_ALLY)
                .newBuilder()
                .addQueryParameter("allyName", userName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                }
            }
        });
    }

    public void quitButtonClicked(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void setMainController(AppController mainScene) {
        this.mainController = mainScene;
    }

}
