package enigmaEngine;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.impl.EnigmaEngineImpl;
import enigmaEngine.impl.PlugBoardImpl;
import enigmaEngine.interfaces.InitializeEnigma;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;
import enigmaEngine.schemaBinding.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EnigmaMachineFromXML implements InitializeEnigma {
    private CreateAndValidateEnigmaComponentsImpl createAndValidateEnigmaComponents;

    @Override
    public EnigmaEngineImpl getEnigmaEngineFromSource(String path) throws FileNotFoundException, JAXBException, RuntimeException, InvalidABCException, InvalidReflectorException, InvalidRotorException {
        CTEEnigma xmlOutput = null;

        if (!path.contains(".xml")) {
            throw new FileNotFoundException("File given is not of XML type.");
        }
        InputStream xmlFile = new FileInputStream(path);
        JAXBContext jaxbContext = JAXBContext.newInstance("enigmaEngine.schemaBinding");
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        xmlOutput = (CTEEnigma) jaxbUnmarshaller.unmarshal(xmlFile);

        return getEnigmaEngine(path, xmlOutput);
    }

    private EnigmaEngineImpl getEnigmaEngine(String path, CTEEnigma xmlOutput) throws RuntimeException, InvalidABCException, InvalidReflectorException, InvalidRotorException {

        assert xmlOutput != null;
        String cteMachineABC;
        int cteRotorsCount;
        CTERotors cteRotors;
        CTEMachine machine;
        List<CTEReflector> cteReflectors;
        HashMap<Integer, Rotor> rotors = new HashMap<>();
        HashMap<Reflector.ReflectorID, Reflector> reflectors = new HashMap<>();

        machine = xmlOutput.getCTEMachine();
        if (machine == null) {
            throw new RuntimeException("The XML that is given does not contain any machine.");
        }

        cteMachineABC = machine.getABC().trim();
        createAndValidateEnigmaComponents = new CreateAndValidateEnigmaComponentsImpl(cteMachineABC);
        createAndValidateEnigmaComponents.ValidateABC(cteMachineABC);

        cteRotorsCount = machine.getRotorsCount();
        cteRotors = machine.getCTERotors();
        if (cteRotorsCount > cteRotors.getCTERotor().size()) {
            throw new RuntimeException("The XML that is given, contains in its settings more needed rotors than actual rotors.");
        }

        cteReflectors = new ArrayList<>(machine.getCTEReflectors().getCTEReflector());
        for (CTERotor rotor : cteRotors.getCTERotor()) {
            int id = rotor.getId(), notch = rotor.getNotch() - 1;
            List<Character> right = new ArrayList<>();
            List<Character> left = new ArrayList<>();
            for (CTEPositioning pair : rotor.getCTEPositioning()) {
                right.add(pair.getRight().charAt(0));
                left.add(pair.getLeft().charAt(0));
            }

            if (rotors.containsKey(id)) {
                throw new InvalidRotorException("two rotors have the same ID.");
            } else
                rotors.put(id, createAndValidateEnigmaComponents.createRotor(id, notch, right, left));
        }

        createAndValidateEnigmaComponents.validateRotorsIDs(rotors);

        for (CTEReflector reflector : cteReflectors) {
            enigmaEngine.interfaces.Reflector.ReflectorID id;
            try {
                id = enigmaEngine.interfaces.Reflector.ReflectorID.valueOf(reflector.getId().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid ID for enum "
                        + enigmaEngine.interfaces.Reflector.ReflectorID.class.getSimpleName()
                        + " of a given reflector: " + reflector.getId()
                        + ". Valid IDs are only: " + Arrays.toString(Reflector.ReflectorID.values()));
            }

            List<Integer> input = new ArrayList<>();
            List<Integer> output = new ArrayList<>();
            for (CTEReflect pair : reflector.getCTEReflect()) {
                if (pair.getInput() == pair.getOutput()) {
                    throw new RuntimeException("The XML that is given contains a reflector that maps a letter to itself.");
                }

                input.add(pair.getInput());
                output.add(pair.getOutput());
            }
            if (reflectors.containsKey(id)) {
                throw new InvalidReflectorException("two reflectors have the same ID.");
            } else
                reflectors.put(id, createAndValidateEnigmaComponents.createReflector(input, output, id));
        }

        createAndValidateEnigmaComponents.validateReflectorsIDs(reflectors);

        return new EnigmaEngineImpl(rotors, reflectors, new PlugBoardImpl(), cteMachineABC);
    }
}
