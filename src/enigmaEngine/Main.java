package enigmaEngine;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.InvalidStartingCharacters;
import enigmaEngine.impl.EnigmaEngine;
import enigmaEngine.interfaces.Reflector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    //TODO: The machine starts with 0 and not 1 as required
    public static void main(String[] args) {
        // ~~~~~~~~~ UI : initialize the machine on start ~~~~~~~~~
        InitializeEnigmaEngine enigmaEngineInitializer = new InitializeEnigmaEngine();
//        EnigmaEngine enigmaEngine = enigmaEngineInitializer.initializeEngine(InitializeEnigmaEngine.sourceMode.DEBUG, "");
        EnigmaEngine enigmaEngine = null;

        // ~~~~~~~~~ UI : option 1 Load from XML ~~~~~~~~~
        try {
            enigmaEngine = enigmaEngineInitializer.initializeEngine(InitializeEnigmaEngine.sourceMode.XML, "src/Resources/ex1-sanity-small.xml");
        } catch (InvalidRotorException | InvalidReflectorException | InvalidABCException e) {   //exception thrown by the enigma engine initialization
            throw new RuntimeException(e);
        }catch (Exception e) {  //we want to catch all exceptions and not just the ones that are thrown by the enigma engine
            e.printStackTrace();
        }

        // ~~~~~~~~~ UI : option 3 set machine state manually ~~~~~~~~~
        List<Integer> selectedRotors = Arrays.asList(2, 1);
        ArrayList<Character> startingCharacters = new ArrayList<>();

        startingCharacters.add('C');
        startingCharacters.add('C');

        enigmaEngine.setSelectedReflector(Reflector.ReflectorID.I);
        try {
            enigmaEngine.setSelectedRotors(selectedRotors, startingCharacters);
        } catch (InvalidStartingCharacters e) {
            throw new RuntimeException(e);
        } catch (InvalidRotorException e) {
            throw new RuntimeException(e);
        }

        // ~~~~~~~~~ UI : option 5 encrypt/decrypt message ~~~~~~~~~
        String secretMessage = /*"ABCDEFGHIJKL";*/ "ABCDEF";
        String encryptedMessage = /*"KFBLICCCLFIB";*/ "BCDEFC";

        for (int i = 0; i < secretMessage.length(); i++) {
            System.out.print(enigmaEngine.activate(secretMessage.charAt(i)));
        }

        System.out.println();
        enigmaEngine.reset();   //UI : option 6 reset the machine to its initial state

        for (int i = 0; i < encryptedMessage.length(); i++) {
            System.out.print(enigmaEngine.activate(encryptedMessage.charAt(i)));
        }

        System.out.println();
    }

    //CMD Main
    //    public static void main(String[] args) {
    //        // WHAT I DID IN CMD: xjc -d . -p enigmaEngine.schemaBinding Enigma-Ex1.xsd
    //        ((InitializeEnigmaComponents) CreateEnigmaMachineFromFile.XML).getEnigmaEngineFromSource("ex1-sanity-small.xml");
    //    }
}
