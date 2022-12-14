package consoleApp.interfaces;

import consoleApp.exceptions.NoMachineGeneratedException;
import enigmaEngine.exceptions.*;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface Input {

    // Only full path is given, not just file
    Boolean readMachineFromXMLFile() throws JAXBException, InvalidRotorException, FileNotFoundException, InvalidABCException, UnknownSourceException, InvalidReflectorException;
    void getMachineSpecs() throws NoMachineGeneratedException;
    void initializeEnigmaCodeManually(); // Changed to boolean. false - if player exits this option in the middle, true if he added all input
    void initializeEnigmaCodeAutomatically() throws InvalidPlugBoardException, InvalidRotorException, InvalidReflectorException;
    void getMessageAndProcessIt();
    void resetMachine();
    void getMachineStatisticsAndHistory() throws NoMachineGeneratedException;
    void exitMachine();
    void saveGame();
    void loadGame();
}