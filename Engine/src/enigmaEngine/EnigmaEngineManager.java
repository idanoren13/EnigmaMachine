package enigmaEngine;

import enigmaEngine.interfaces.EnigmaEngine;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

public class EnigmaEngineManager {
    private EnigmaEngine enigmaEngine;

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

}
