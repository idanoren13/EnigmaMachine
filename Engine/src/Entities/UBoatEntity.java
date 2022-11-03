package Entities;

import battlefield.Battlefield;
import enigmaEngine.EnigmaMachineFromXML;
import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.interfaces.EnigmaEngine;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

public class UBoatEntity {

    private String name;
    private EnigmaEngine enigmaEngine;
    private Battlefield battlefield;
    private String encryptedMessage;
    private EnigmaEngine dummyEngine;
    public EnigmaEngine getEnigmaEngine() {
        return enigmaEngine;
    }

    public void setEnigmaEngineFromInputStream(InputStream inputStream) throws IOException, ClassNotFoundException {
        try {
            this.enigmaEngine = new EnigmaMachineFromXML().getEnigmaEngineFromInputStream(inputStream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUBoatName(String name) {
        this.name = name;
    }

    public String EncryptMessage(String message)  {
        try {
            encryptedMessage = enigmaEngine.processMessage(message);
        } catch (InvalidCharactersException e) {
            throw new RuntimeException(e);
        }

        return encryptedMessage;
    }

    public void setRandomConfig() {
        enigmaEngine.randomSelectedComponents();
    }
}
