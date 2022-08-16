package consoleApp.interfaces;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.UnknownSourceException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface Input {

    // Only full path is given, not just file
    Boolean readMachineFromXMLFile() throws JAXBException, InvalidRotorException, FileNotFoundException, InvalidABCException, UnknownSourceException, InvalidReflectorException;
    void getMachineSpecs();
    void initializeEnigmaCodeManually();
    void initializeEnigmaCodeAutomatically();
    void encryptInput();
    void resetMachine();
    void getMachineStatisticsAndHistory();
    void exitMachine();
}
