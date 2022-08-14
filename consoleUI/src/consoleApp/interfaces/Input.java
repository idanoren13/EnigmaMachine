package consoleApp.interfaces;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.UnknownSourceException;
import immutables.userInterface.InitCode;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface Input {

    // Only full path is given, not just file
    void readMachineFromXMLFile() throws JAXBException, InvalidRotorException, FileNotFoundException, InvalidABCException, UnknownSourceException, InvalidReflectorException; // TODO: test - only English letters are allowed, given file is actual XML and not shit, etc... Read page 17 on CTE sheet (1)
    void getMachineSpecs(); // TODO: read page 18 on CTE sheet (2)
    InitCode initializePaperEnigmaCodeManually(); // TODO: read page 18 on CTE sheet (3). Can't work before (1) ran.
    void initializePaperEnigmaCodeAutomatically(); // TODO: read page 19 on CTE sheet (4). Can't work before (1) ran.
    void encryptInput(); // TODO: read page 19 on CTE sheet (5). Can't work before (3) or (4) ran.
    void resetMachine(); // TODO: read page 19 on CTE sheet (6). Can't work before (3) or (4) ran.
    void getMachineStatisticsAndHistory(); // TODO: read page 19 on CTE sheet (7).
    void exitMachine(); // TODO: read page 19 on CTE sheet (8).
}
