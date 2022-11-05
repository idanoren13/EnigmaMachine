package frontEnd.controllers;

import frontEnd.refreshers.AlliesRefresher;
import immutables.AllyDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
import java.util.Timer;

import static utils.Constants.REFTESH_RATE;

public class LoginController implements Initializable {

    @FXML
    public TextField userNameTextField;
    @FXML
    public Label errorMessageLabel;
    @FXML
    private TableView<AllyDTO> alliesTable;
    @FXML
    private TableColumn<AllyDTO, String> allyCol;
    @FXML
    private TableColumn<AllyDTO, Boolean> isAvialableCol;

    private AppController mainController;
    private Timer timer;
    private AlliesRefresher alliesRefresher;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        initAlliesTable();
        startAlliesRefresher();
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

    public void selectAllyTeam(MouseEvent mouseEvent) {

    }

    public void startAlliesRefresher() {
        alliesRefresher = new AlliesRefresher(this::updateAlliesTable);
        timer = new Timer();
        timer.schedule(alliesRefresher, REFTESH_RATE, REFTESH_RATE);
    }

    private void updateAlliesTable(AllyDTO[] allies) {
        Platform.runLater(() -> {
            alliesTable.getItems().clear();
            for (AllyDTO ally : allies) {
                ObservableList<AllyDTO> items = alliesTable.getItems();
                items.add(ally);
                alliesTable.setItems(items);
            }
        });
    }

    private void initAlliesTable() {
        allyCol.setCellValueFactory(new PropertyValueFactory<AllyDTO,String>("allyName"));
        isAvialableCol.setCellValueFactory(new PropertyValueFactory<AllyDTO,Boolean>("isAvailable"));
    }
}
