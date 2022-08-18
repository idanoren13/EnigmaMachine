package enigmaEngine.interfaces;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidMachineException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.impl.EnigmaEngineImpl;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface InitializeEnigma {
    EnigmaEngineImpl getEnigmaEngineFromSource(String source) throws InvalidABCException, InvalidReflectorException, InvalidRotorException, FileNotFoundException, JAXBException, InvalidMachineException;
}