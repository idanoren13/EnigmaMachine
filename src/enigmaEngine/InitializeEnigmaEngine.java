package enigmaEngine;

import enigmaEngine.impl.EnigmaEngine;

public class InitializeEnigmaEngine {
    public enum sourceMode{
        XML,

    }

    public EnigmaEngine initializeEngine(sourceMode source){
        InitializeEnigmaComponents enigmaEngineInitializer = null;

        switch (source){
            case XML:
                enigmaEngineInitializer = new CreateEnigmaMachineFromXML();
                break;
        }
        return enigmaEngineInitializer.getEnigmaEngineFromSource();
    }
}
