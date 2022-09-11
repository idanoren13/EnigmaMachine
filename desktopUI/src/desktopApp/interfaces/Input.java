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
    void setEngine(EnigmaEngine engine);
    void readMachineFromXMLFile(String path) throws JAXBException, InvalidRotorException, IOException, InvalidABCException, UnknownSourceException, InvalidReflectorException, InvalidMachineException;
    void getMachineSpecs() throws NoMachineGeneratedException;
    void initializeEnigmaCodeManually(String rotors, String startingPositions, String plugBoardPairs, String reflectorID) throws InvalidRotorException, InvalidReflectorException, InvalidPlugBoardException, InvalidCharactersException; // Changed to boolean. false - if player exits this option in the middle, true if he added all input
    void initializeEnigmaCodeAutomatically();
    String getMessageAndProcessIt(String messageInput, boolean bool) throws InvalidCharactersException;
    void resetMachine();
    String getMachineStatisticsAndHistory() throws NoMachineGeneratedException;
    void exitMachine();
/*    void saveGame();
    void loadGame();*/
}