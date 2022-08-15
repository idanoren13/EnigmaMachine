package consoleApp;

import consoleApp.impl.Console;

import java.util.Scanner;

public class Program {
    public enum Choice {
        CHOICE_ONE,
        CHOICE_TWO,
        CHOICE_THREE,
        CHOICE_FOUR,
        CHOICE_FIVE,
        CHOICE_SIX,
        CHOICE_SEVEN,
        CHOICE_EIGHT
    }

    //TODO: NOT GOOD MAIN LOOP INSIDE THE MAIN FUNCTION IS A BAD PRACTICE! EXTRACT IT TO A NEW CLASS!
    public static void main(String[] args)  {
        Console consoleApp = new Console();
        Choice userChoice = null;

        greetUser();
        do {
            showMenu();
            boolean validInput = false;
            do {
                try {
                    Scanner scanner = new Scanner(System.in);
                    int input = scanner.nextInt(); //TODO: not good, it should be parsed from string to int
                    if (input < 1 || 9 <= input) {
                        throw new IllegalArgumentException();
                    }
                    userChoice = Choice.values()[input - 1];
                    validInput = true;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter numeric value.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Please enter a single-digit from one of the numbers above.");
                }
            } while (!validInput);

            switch(userChoice) {
                case CHOICE_ONE:
                    consoleApp.readMachineFromXMLFile();
                    break;
                case CHOICE_TWO:
                    consoleApp.getMachineSpecs();
                    break;
                case CHOICE_THREE:
                    consoleApp.initializeEnigmaCodeManually();
                    break;
                case CHOICE_FOUR:
                    //TODO: implement this method, Guy
                    break;
                case CHOICE_FIVE:
                    consoleApp.encryptInput();
                    break;
                case CHOICE_SIX:
                    consoleApp.resetMachine();
                    break;
                case CHOICE_SEVEN:
                    //TODO: implement
                    break;
                case CHOICE_EIGHT:
                    System.out.println("Goodbye!");
                    break;
            }
        } while (userChoice != Choice.CHOICE_EIGHT);
    }

    public static void greetUser() {
        System.out.println("Hello dear user!");
        System.out.println("Welcome to the C.T.E game.");
        System.out.println("From now on, showtime.");
        System.out.println("Here are all your options during this game:");

    }
    public static void showMenu() {
        System.out.println("1. Load a XML file by giving a full file path.");
        System.out.println("2. Get your full Enigma machine engine specifications.");
        System.out.println("3. Choose Enigma engine code.");
        System.out.println("4. Generate your Enigma engine code automatically.");
        System.out.println("5. Decrypt input by your Enigma engine.");
        System.out.println("6. Reset current Enigma engine code.");
        System.out.println("7. Reveal descriptive statistics.");
        System.out.println("8. Exit the C.T.E game.");
    }
}
