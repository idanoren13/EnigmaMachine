package enigmaEngine.interfaces;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.impl.EnigmaEngine;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface InitializeEnigma {
    EnigmaEngine getEnigmaEngineFromSource(String source) throws InvalidABCException, InvalidReflectorException, InvalidRotorException, FileNotFoundException, JAXBException;
}
