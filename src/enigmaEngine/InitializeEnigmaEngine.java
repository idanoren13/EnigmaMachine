package enigmaEngine;

import enigmaEngine.impl.EnigmaEngine;
import enigmaEngine.interfaces.InitializeEnigmaComponents;

public class InitializeEnigmaEngine {
    public enum sourceMode{
        XML,
        DEBUG,
        JSON
    }

    public EnigmaEngine initializeEngine(sourceMode source, String path) {
        InitializeEnigmaComponents enigmaEngineInitializer = null;

        switch (source){
            case XML:
                enigmaEngineInitializer = new CreateEnigmaMachineFromXML();
                break;
            case DEBUG:
                enigmaEngineInitializer = new CreateEnigmaMachineToDebug();
                break;
        }
        return enigmaEngineInitializer.getEnigmaEngineFromSource(path);
    }
}
