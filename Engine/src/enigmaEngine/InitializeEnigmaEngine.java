package enigmaEngine;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.UnknownSourceException;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.InitializeEnigma;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class InitializeEnigmaEngine {
    public enum SourceMode{
        XML,
        DEBUG,
        JSON
    }

    private String lastFilePath = null;

    public EnigmaEngine initializeEngine(SourceMode source, String path) throws InvalidRotorException, InvalidABCException, InvalidReflectorException, JAXBException, FileNotFoundException, UnknownSourceException {
        InitializeEnigma enigmaEngineInitializer = null;

        switch (source){
            case XML:
                enigmaEngineInitializer = new EnigmaMachineFromXML();
                break;
            case DEBUG:
                enigmaEngineInitializer = new EnigmaMachineToDebug();
                break;
            default:
                throw new UnknownSourceException("Unknown file extension source is given.");
        }
        if (lastFilePath != null && lastFilePath.equals(path) == true) {
            // TODO: add exception and get out
            System.out.println("This file already exists in the machine.");
            return null;
        }
        else {
            return enigmaEngineInitializer.getEnigmaEngineFromSource(path);
        }
    }
}
