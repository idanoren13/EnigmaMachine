package enigmaEngine;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidMachineException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.impl.EnigmaEngineImpl;
import enigmaEngine.impl.PlugBoardImpl;
import enigmaEngine.interfaces.InitializeEnigma;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;
import enigmaEngine.schemaBinding.*;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EnigmaMachineFromXML implements InitializeEnigma {
    private CreateAndValidateEnigmaComponentsImpl createAndValidateEnigmaComponents;
    public String currentEnigmaMachineSource;

    @Override
    public EnigmaEngineImpl getEnigmaEngineFromSource(String path) throws FileNotFoundException, JAXBException, RuntimeException, InvalidABCException, InvalidReflectorException, InvalidRotorException, InvalidMachineException, FileAlreadyExistsException {
        CTEEnigma xmlOutput;

        if (!path.contains(".xml")) {
            throw new FileNotFoundException("File given is not of XML type.");
        }
        else if (this.currentEnigmaMachineSource.equals(path)) {
            throw new FileAlreadyExistsException("File given is already defined as the Enigma machine.");
        }
        InputStream xmlFile = new FileInputStream(path);
        JAXBContext jaxbContext = JAXBContext.newInstance("enigmaEngine.schemaBinding");
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        xmlOutput = (CTEEnigma) jaxbUnmarshaller.unmarshal(xmlFile);

        assert xmlOutput != null;
        return getEnigmaEngine(path, xmlOutput);
    }

    private EnigmaEngineImpl getEnigmaEngine(String path, CTEEnigma xmlOutput) throws RuntimeException, InvalidABCException, InvalidReflectorException, InvalidRotorException, InvalidMachineException {

        String cteMachineABC;
        int cteRotorsCount;
        CTERotors cteRotors;
        CTEMachine machine;
        List<CTEReflector> cteReflectors;
        HashMap<Integer, Rotor> rotors;
        HashMap<Reflector.ReflectorID, Reflector> reflectors;

        // Machine
        machine = xmlOutput.getCTEMachine();
        if (machine == null) {
            throw new InvalidMachineException("In the given XML, no machine is given.");
        }

        // Machine's ABC
        if (machine.getABC() == null) {
            throw new InvalidABCException("In the given XML, there is no ABC language.");
        }
        cteMachineABC = machine.getABC().trim();
        createAndValidateEnigmaComponents = new CreateAndValidateEnigmaComponentsImpl(cteMachineABC);
        createAndValidateEnigmaComponents.ValidateABC(cteMachineABC);

        // Rotors
        cteRotorsCount = machine.getRotorsCount();
        if (cteRotorsCount < 2) {
            throw new InvalidRotorException("In the given XML, rotors count is less than 2.");
        }
        cteRotors = machine.getCTERotors();
        if (cteRotorsCount > cteRotors.getCTERotor().size()) {
            throw new InvalidRotorException("In the given XML, rotors count is larger than actual rotors.");
        }
        rotors = (HashMap<Integer, Rotor>)importCTERotors(cteRotors, new HashMap<>());
        createAndValidateEnigmaComponents.validateRotorsIDs(rotors);

        // Reflectors
        if (machine.getCTEReflectors() == null || machine.getCTEReflectors().getCTEReflector().size() < 1) {
            throw new InvalidReflectorException("In the given XML, there are no reflectors.");
        }
        cteReflectors = new ArrayList<>(machine.getCTEReflectors().getCTEReflector());
        reflectors = (HashMap<Reflector.ReflectorID, Reflector>)importCTEReflectors(cteReflectors, new HashMap<>());
        createAndValidateEnigmaComponents.validateReflectorsIDs(reflectors);

        this.currentEnigmaMachineSource = path;
        return new EnigmaEngineImpl(rotors, reflectors, new PlugBoardImpl(), cteMachineABC);
    }

    private HashMap<?, ?> importCTERotors(CTERotors cteRotors, HashMap<Object, Object> rotors) throws InvalidRotorException {
        for (CTERotor rotor : cteRotors.getCTERotor()) {
            int id = rotor.getId(), notch = rotor.getNotch() - 1;
            Pair<List<Character>, List<Character>> rightAndLeft = getCTERotorRightAndLeftPairs(rotor);

            if (rotors.containsKey(id)) {
                throw new InvalidRotorException("two rotors have the same ID.");
            } else
                rotors.put(id, createAndValidateEnigmaComponents.createRotor(id, notch, rightAndLeft.getKey(), rightAndLeft.getValue()));
        }
        return rotors;
    }

    private Pair<List<Character>, List<Character>> getCTERotorRightAndLeftPairs(CTERotor rotor) {
        List<Character> right = new ArrayList<>();
        List<Character> left = new ArrayList<>();
        for (CTEPositioning pair : rotor.getCTEPositioning()) {
            right.add(pair.getRight().charAt(0));
            left.add(pair.getLeft().charAt(0));
        }
        return new Pair<>(right, left);
    }

    private HashMap<?, ?> importCTEReflectors(List<CTEReflector> cteReflectors, HashMap<Object, Object> reflectors) throws InvalidReflectorException {
        for (CTEReflector reflector : cteReflectors) {
            Reflector.ReflectorID id = getCTEReflectorID(reflector.getId().toUpperCase());
            Pair<List<Integer>, List<Integer>> inputAndOutput = getCTEReflectorInputAndOutputPairs(reflector);

            if (reflectors.containsKey(id)) {
                throw new InvalidReflectorException("two reflectors have the same ID.");
            } else
                reflectors.put(id, createAndValidateEnigmaComponents.createReflector(inputAndOutput.getKey(), inputAndOutput.getValue(), id));
        }
        return reflectors;
    }

    private Reflector.ReflectorID getCTEReflectorID(String stringID) {
        try {
            return Reflector.ReflectorID.valueOf(stringID);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ID for enum "
                    + enigmaEngine.interfaces.Reflector.ReflectorID.class.getSimpleName()
                    + " of a given reflector: " + stringID
                    + ". Valid IDs are only: " + Arrays.toString(Reflector.ReflectorID.values()));
        }
    }

    private Pair<List<Integer>, List<Integer>> getCTEReflectorInputAndOutputPairs(CTEReflector reflector) {
        List<Integer> input = new ArrayList<>();
        List<Integer> output = new ArrayList<>();
        for (CTEReflect pair : reflector.getCTEReflect()) {
            if (pair.getInput() == pair.getOutput()) {
                throw new RuntimeException("The XML that is given contains a reflector that maps a letter to itself.");
            }

            input.add(pair.getInput());
            output.add(pair.getOutput());
        }
        return new Pair<>(input, output);
    }
}