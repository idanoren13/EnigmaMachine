package enigmaEngine;

import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.InitializeEnigma;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Serializable;

public class InitializeEnigmaEngine implements Serializable {
    public enum SourceMode{
        XML,
        DEBUG,
        JSON
    }

    public EnigmaEngine initializeEngine(SourceMode source, String path) throws InvalidRotorException, InvalidABCException, InvalidReflectorException, JAXBException, IOException, UnknownSourceException, InvalidMachineException {
        InitializeEnigma enigmaEngineInitializer;

        switch (source) {
            case XML:
                enigmaEngineInitializer = new EnigmaMachineFromXML();
                break;
            case DEBUG:
                enigmaEngineInitializer = new EnigmaMachineToDebug();
                break;
            default:
                throw new UnknownSourceException("Unknown file extension source is given.");
        }
        return enigmaEngineInitializer.getEnigmaEngineFromSource(path);
    }
}