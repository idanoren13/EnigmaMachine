import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class UBoatClientApp extends Application {

    private static String APP_FXML_INCLUDE_RESOURCE = "/frontEnd/fxml/app.fxml";
    private static String APP_ICON_FOR_AVIAD = "/frontEnd/images/kifli.jpg";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Settings
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(200);
        primaryStage.setTitle("C.T.E Exercise 3");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(APP_ICON_FOR_AVIAD))));

        try {
            // Load master app and controller from FXML
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(APP_FXML_INCLUDE_RESOURCE);
            fxmlLoader.setLocation(url);
            ScrollPane root = fxmlLoader.load(url.openStream());


            // Set scene
            Scene scene = new Scene(root, 902, 602);
            primaryStage.setScene(scene);
            primaryStage.show();

//            FXMLLoader fxmlLoader2 = new FXMLLoader();
//            URL url2 = getClass().getResource("/login/LoginWindow.fxml");
//            fxmlLoader2.setLocation(url2);
//            ScrollPane root2 = fxmlLoader2.load(url2.openStream());
//            Scene scene2 = new Scene(root2, 902, 602);
//            primaryStage.setScene(scene2);

        } catch (LoadException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("Exception cause: Resource not found.");
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        Application.launch(args);
    }
}