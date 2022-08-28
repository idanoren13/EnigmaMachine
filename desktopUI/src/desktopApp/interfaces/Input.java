package desktopApp.interfaces;

import desktopApp.exceptions.NoMachineGeneratedException;
import desktopApp.historyAndStatistics.MachineHistoryAndStatistics;
import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.EnigmaEngine;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface Input {

    // Only full path is given, not just file

    MachineHistoryAndStatistics getMachineHistoryStates();
    EnigmaEngine getEngine();
    Boolean readMachineFromXMLFile(String path) throws JAXBException, InvalidRotorException, IOException, InvalidABCException, UnknownSourceException, InvalidReflectorException, InvalidMachineException;
    void getMachineSpecs() throws NoMachineGeneratedException;
    void initializeEnigmaCodeManually(String rotors, String startingPositions, String plugBoardPairs, String reflectorID) throws InvalidRotorException, InvalidReflectorException, InvalidPlugBoardException, InvalidCharactersException; // Changed to boolean. false - if player exits this option in the middle, true if he added all input
    void initializeEnigmaCodeAutomatically();
    void getMessageAndProcessIt(String messageInput);
    void resetMachine();
    void getMachineStatisticsAndHistory() throws NoMachineGeneratedException;
    void exitMachine();
/*    void saveGame();
    void loadGame();*/
}