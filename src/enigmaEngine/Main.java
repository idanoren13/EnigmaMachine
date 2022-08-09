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
        EnigmaEngine enigmaEngine = enigmaEngineInitializer.initializeEngine(InitializeEnigmaEngine.sourceMode.DEBUG, "");

        List<Integer> selectedRotors = Arrays.asList(2,1);
        ArrayList<Character> startingCharacters = new ArrayList<>();

        startingCharacters.add('C');
        startingCharacters.add('C');

        enigmaEngine.setSelectedReflector(Reflector.ReflectorID.I.ordinal());
        enigmaEngine.setSelectedRotors( selectedRotors, startingCharacters);

        String secretMessage = "ABCDEFGHIJKL";
        String encryptedMessage = "KFBLICCCLFIB";
//        String secretMessage = "ABCDEF";
//        String encryptedMessage = "CCEEFB";

        for (int i = 0; i < secretMessage.length(); i++) {
            System.out.print(enigmaEngine.activate(encryptedMessage.charAt(i)));
        }
    }
}
