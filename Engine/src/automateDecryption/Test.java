package automateDecryption;

import enigmaEngine.InitializeEnigmaEngine;
import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.EnigmaEngine;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Test {

    public static void main(String[] args) {
        try {
            String message = "Hello World";
            String encryptedMessage ;
            InitializeEnigmaEngine initializeEnigmaEngine = new InitializeEnigmaEngine();
            EnigmaEngine engine = initializeEnigmaEngine.initializeEngine(InitializeEnigmaEngine.SourceMode.XML, "E:\\Degree\\Java\\EnigmaMachine\\Engine\\src\\Resources\\ex2-basic.xml");

            engine.randomSelectedComponents();
            encryptedMessage = engine.processMessage(message);
            System.out.println(encryptedMessage);
            engine.reset();
            System.out.println(engine.processMessage(encryptedMessage));


        } catch (InvalidRotorException | InvalidABCException | InvalidReflectorException | JAXBException | IOException |
                 UnknownSourceException | InvalidMachineException | InvalidCharactersException e) {
            throw new RuntimeException(e);
        }
    }
}
