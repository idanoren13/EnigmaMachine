package desktopApp.interfaces;

import desktopApp.exceptions.NoMachineGeneratedException;
import desktopApp.historyAndStatistics.MachineHistoryAndStatistics;
import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.EnigmaEngine;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface Input {

    // Only full path is given, not just file

    String getCurrentMachineState();
    int getMessageCounter();
    MachineHistoryAndStatistics getMachineHistoryStates();
    EnigmaEngine getEngine();
    Boolean readMachineFromXMLFile(String path) throws JAXBException, InvalidRotorException, IOException, InvalidABCException, UnknownSourceException, InvalidReflectorException, InvalidMachineException;
    void getMachineSpecs() throws NoMachineGeneratedException;
    void initializeEnigmaCodeManually(String rotors, String startingPositions, String plugBoardPairs, String reflectorID) throws InvalidRotorException, InvalidReflectorException, InvalidPlugBoardException, InvalidCharactersException; // Changed to boolean. false - if player exits this option in the middle, true if he added all input
    void initializeEnigmaCodeAutomatically();
    String getMessageAndProcessIt(String messageInput) throws InvalidCharactersException;
    void resetMachine();
    String getMachineStatisticsAndHistory() throws NoMachineGeneratedException;
    void exitMachine();
/*    void saveGame();
    void loadGame();*/
}