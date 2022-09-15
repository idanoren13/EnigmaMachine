package desktopApp;

import desktopApp.frontEnd.AppController;
import desktopApp.frontEnd.MachineStateController;
import desktopApp.impl.Console;
import enigmaEngine.interfaces.Reflector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Collections;
import java.util.List;

public class MSCDebug extends Application {
    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        MachineStateController mcs = new MachineStateController();
        Console consoleApp = new Console();
        String rotors = "2,1", startingPositions = "CC", reflectorID = "I", plugBoardPairs = "AF";
        try {
            consoleApp.readMachineFromXMLFile("C:\\Users\\guybe\\IdeaProjects\\EnigmaMachine\\Engine\\src\\Resources\\ex1-sanity-small.xml");
            List<Reflector.ReflectorID> unsortedReflectors = AppController.getConsoleApp().getEngine().getReflectors();
            Collections.sort(unsortedReflectors);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            consoleApp.initializeEnigmaCodeManually(rotors, startingPositions, plugBoardPairs, reflectorID);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApp/frontEnd/fxml/currentMachineState.fxml");
            fxmlLoader.setLocation(url);
            HBox root = fxmlLoader.load(url.openStream());

            Scene scene = new Scene(root, 449, 132);
            primaryStage.setScene(scene);
            primaryStage.show();
            mcs.setInitializedControllerComponents(consoleApp.getEngine().getEngineDTO());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
