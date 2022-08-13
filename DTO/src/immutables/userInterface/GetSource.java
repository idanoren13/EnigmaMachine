package immutables.userInterface;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.exceptions.UnknownSourceException;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.Rotor;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// UI->DTO->Engine
// A DTO for sections 1,
public class GetSource {

    private EnigmaEngine engine;

    public GetSource(String path) throws JAXBException, InvalidRotorException, FileNotFoundException, InvalidABCException, UnknownSourceException, InvalidReflectorException {
        this.engine = new enigmaEngine.InitializeEnigmaEngine().initializeEngine(getFilePathExtension(path), path);
        // TODO: if this raised some exception, no engine overwriting should happen
    }
    private enigmaEngine.InitializeEnigmaEngine.SourceMode getFilePathExtension(String path) {
        if (path.matches("[a-zA-Z]+") == false) { // TODO: add exception handling
            System.out.println("Exception! Only English letters can be given.");
        }
        int substringLength = path.lastIndexOf(".") + 1;
        String extension = path.substring(substringLength);
        return enigmaEngine.InitializeEnigmaEngine.SourceMode.valueOf(extension);
    }

    public EnigmaEngine getEngine() {
        return this.engine;
    }

    public List<Integer> getNotchIndexes() {
        HashMap<Integer, Rotor> allRotors = this.engine.getRotors();
        List<Integer> allNotches = new ArrayList<>();

        for (int i = 0; i < allRotors.size(); i++) {
            allNotches.add(allRotors.get(i).getNotch());
        }

        if (allRotors.size() == 0) {
            return null;
        }
        else {
            return allNotches;
        }
    }
}
