package enigmaEngine;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.impl.EnigmaEngine;
import enigmaEngine.interfaces.InitializeEnigma;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class InitializeEnigmaEngine {
    public enum sourceMode{
        XML,
        DEBUG,
        JSON
    }

    public EnigmaEngine initializeEngine(sourceMode source, String path) throws InvalidRotorException, InvalidABCException, InvalidReflectorException, JAXBException, FileNotFoundException {
        InitializeEnigma enigmaEngineInitializer = null;

        switch (source){
            case XML:
                enigmaEngineInitializer = new EnigmaMachineFromXML();
                break;
            case DEBUG:
                enigmaEngineInitializer = new EnigmaMachineToDebug();
                break;
        }
        
        return enigmaEngineInitializer.getEnigmaEngineFromSource(path);
    }
}
