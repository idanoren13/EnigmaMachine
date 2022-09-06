package desktopApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class Program extends Application {

    private static String APP_FXML_INCLUDE_RESOURCE = "/desktopApp/frontEnd/fxml/app.fxml";
    private static String APP_ICON_FOR_AVIAD = "/desktopApp/frontEnd/images/kifli.jpg";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Settings
        primaryStage.setMinHeight(200);
        primaryStage.setMaxHeight(800);
        primaryStage.setMinWidth(300);
        primaryStage.setMaxWidth(1200);
        primaryStage.setTitle("C.T.E Exercise 2");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(APP_ICON_FOR_AVIAD)));

        try {
            // Load master app and controller from FXML
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(APP_FXML_INCLUDE_RESOURCE);
            fxmlLoader.setLocation(url);
            BorderPane root = fxmlLoader.load(url.openStream());

            // Set scene
            Scene scene = new Scene(root, 900, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (LoadException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("Exception cause: Resource not found.");
        }
        // runMachine();
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        Application.launch(args);
    }

    /*private static void runMachine() {
        Console consoleApp = new Console();
        Choice userChoice;
        Boolean machineIsLoaded = false;

        // Loads the machine from the XML file
        while (!machineIsLoaded) {
            machineIsLoaded = consoleApp.readMachineFromXMLFile("");
        }

        do {
            userChoice = getChoice();

            switch(userChoice) {
                case CHOICE_ONE:
                    Console tmpConsole = new Console();
                    if (tmpConsole.readMachineFromXMLFile("")) {
                        consoleApp = tmpConsole;
                    }
                    break;
                case CHOICE_TWO:
                    consoleApp.getMachineSpecs();
                    break;
                case CHOICE_THREE:
                    consoleApp.initializeEnigmaCodeManually("", "", "", "");
                    break;
                case CHOICE_FOUR:
                    consoleApp.initializeEnigmaCodeAutomatically();
                    break;
                case CHOICE_FIVE:
                    consoleApp.getMessageAndProcessIt("");
                    break;
                case CHOICE_SIX:
                    consoleApp.resetMachine();
                    break;
                case CHOICE_SEVEN:
                    consoleApp.getMachineStatisticsAndHistory();
                    break;
                case CHOICE_EIGHT:
                    consoleApp.exitMachine();
                    break;
*//*                case CHOICE_NINE:
                    consoleApp.saveGame();
                    break;
                case CHOICE_TEN:
                    consoleApp.loadGame();
                    break;*//*
            }
        } while (userChoice != Choice.CHOICE_EIGHT);
    }

    private static Choice getChoice() {
        Choice userChoice = null;
        boolean validInput = false;
        do {
            try {
                Scanner scanner = new Scanner(System.in);
                int input = Integer.parseInt(scanner.nextLine());
                if (input < 1 || 11 <= input) {
                    throw new IllegalArgumentException();
                }

                userChoice = Choice.values()[input - 1];
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter numeric value.");
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a numeric choice from one of the above.");
            }
        } while (!validInput);

        return userChoice;
    }*/
}