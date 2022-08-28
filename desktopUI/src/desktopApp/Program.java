package desktopApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Program extends Application {

    /*public enum Choice {
        CHOICE_ONE,
        CHOICE_TWO,
        CHOICE_THREE,
        CHOICE_FOUR,
        CHOICE_FIVE,
        CHOICE_SIX,
        CHOICE_SEVEN,
        CHOICE_EIGHT,
        CHOICE_NINE,
        CHOICE_TEN
    }*/

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("C.T.E Exercise 2");

        Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("frontEnd/enigma-screen-one.fxml")));
        Scene scene = new Scene(load, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

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