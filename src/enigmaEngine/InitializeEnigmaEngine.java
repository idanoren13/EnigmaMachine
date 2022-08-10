package enigmaEngine;

import enigmaEngine.impl.EnigmaEngine;
import enigmaEngine.interfaces.InitializeEnigma;

public class InitializeEnigmaEngine {
    public enum sourceMode{
        XML,
        DEBUG,
        JSON
    }

    public EnigmaEngine initializeEngine(sourceMode source, String path) {
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
