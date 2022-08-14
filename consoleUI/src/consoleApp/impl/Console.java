package consoleApp.impl;

import consoleApp.interfaces.Input;
import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.UnknownSourceException;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.Rotor;
import immutables.userInterface.InitCode;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Console implements Input {

    private Scanner scanner;
    private EnigmaEngine machine;
    private int numOfDecryptions;
    private List<String> stringInput;
    private List<String> stringOutput;
    private List<Integer> timeStamps;

    public Console() {
        this.scanner = new Scanner(System.in);
        this.machine = null;
        this.numOfDecryptions = 0;
        this.stringInput = null;
        this.stringOutput = null;
        this.timeStamps = null;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public Console(EnigmaEngine machine) {
        this.machine = machine;
        this.stringInput = null;
        this.stringOutput = null;
        this.timeStamps = null;
    }

    @Override
    public void readMachineFromXMLFile() throws JAXBException, InvalidRotorException, FileNotFoundException, InvalidABCException, UnknownSourceException, InvalidReflectorException {
        System.out.println("Enter a valid full path to your file, including file name and its file extension.");
        try {
            String path = this.scanner.nextLine();
            EnigmaEngine newMachine = new immutables.userInterface.GetSource(path).getEngine();
            this.machine = newMachine;
        } catch(Exception e) {
            // TODO: add error message, etc... Prevent machine for being overwritten
        }
    }

    @Override
    public void getMachineSpecs() {
        // TODO: do this
    }

    @Override
    public InitCode initializePaperEnigmaCodeManually() {
        String machineABC = this.machine.getMachineABC();
        boolean valid1 = false;
        boolean valid2 = false;
        boolean valid3 = false;
        boolean valid4 = false;
        String selectedRotors = null;
        String allStartingPositions = null;
        int reflectorNumber = -1;
        String allPlugBoardPairs = null;

        selectedRotors = this.scanner.nextLine();
        allStartingPositions = this.scanner.nextLine();
        System.out.println("Enter your desired reflector ID.");
        do {
            try {
                reflectorNumber = Integer.parseInt(this.scanner.nextLine());
                valid3 = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a single-digit from one of the numbers above, without other letters.");
                valid3 = false;
            }

        } while (!valid3);

        do {
            allPlugBoardPairs = this.scanner.nextLine().trim();
            valid4 = (allPlugBoardPairs.length() % 2 == 1);
            if (!valid4) {
                System.out.println("Given plug board pairs does not contain even number of ABC characters. Try again please.");
            }

        } while (!valid4);

        return new InitCode(selectedRotors, allStartingPositions, reflectorNumber, allPlugBoardPairs, getAllNotches());
    }

    private List<Integer> getAllNotches() {
        HashMap<Integer, Rotor> allRotors = this.machine.getRotors();
        List<Integer> allNotches = new ArrayList<>();

        for (int i = 0; i < allRotors.size(); i++) {
            allNotches.add(allRotors.get(i).getNotch());
        }

        return allNotches;
    }

    @Override
    public void initializePaperEnigmaCodeAutomatically() {

    }

    @Override
    public void encryptInput() {

    }

    @Override
    // Reset last
    public void resetMachine() {
        // TODO: reset manual or automatic paper enigma code

    }

    @Override
    public void getMachineStatisticsAndHistory() {

    }

    @Override
    public void exitMachine() {
        // Does nothing
    }
}
