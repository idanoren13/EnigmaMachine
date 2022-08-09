package enigmaEngine;

import enigmaEngine.impl.EnigmaEngine;

public class Main {
    public static void main(String[] args) {
        InitializeEnigmaEngine enigmaEngineInitializer = new InitializeEnigmaEngine();
        EnigmaEngine enigmaEngine = enigmaEngineInitializer.initializeEngine(InitializeEnigmaEngine.sourceMode.DEBUG);


    }
}
