package enigmaEngine;

import enigmaEngine.impl.EnigmaEngine;
import enigmaEngine.interfaces.Reflector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    //TODO: The machine starts with 0 and not 1 as required
    public static void main(String[] args) {
        InitializeEnigmaEngine enigmaEngineInitializer = new InitializeEnigmaEngine();
//        EnigmaEngine enigmaEngine = enigmaEngineInitializer.initializeEngine(InitializeEnigmaEngine.sourceMode.DEBUG, "");
        EnigmaEngine enigmaEngine = enigmaEngineInitializer.initializeEngine(InitializeEnigmaEngine.sourceMode.XML, "src/Resources/ex1-sanity-small.xml");

        List<Integer> selectedRotors = Arrays.asList(2, 1);
        ArrayList<Character> startingCharacters = new ArrayList<>();

        startingCharacters.add('C');
        startingCharacters.add('C');

        enigmaEngine.setSelectedReflector(Reflector.ReflectorID.I);
        enigmaEngine.setSelectedRotors(selectedRotors, startingCharacters);

        String secretMessage = "ABCDEFGHIJKL"; // = "ABCDEF";
        String encryptedMessage = "KFBLICCCLFIB"; // = "CCEEFB";

        for (int i = 0; i < secretMessage.length(); i++) {
            System.out.print(enigmaEngine.activate(encryptedMessage.charAt(i)));
        }
    }

    //CMD Main
    //    public static void main(String[] args) {
    //        // WHAT I DID IN CMD: xjc -d . -p enigmaEngine.schemaBinding Enigma-Ex1.xsd
    //        ((InitializeEnigmaComponents) CreateEnigmaMachineFromFile.XML).getEnigmaEngineFromSource("ex1-sanity-small.xml");
    //    }
}
