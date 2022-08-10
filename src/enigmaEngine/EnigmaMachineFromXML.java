package enigmaEngine;

import enigmaEngine.exceptions.InvalidABCException;
import enigmaEngine.exceptions.InvalidReflectorException;
import enigmaEngine.exceptions.InvalidRotorException;
import enigmaEngine.impl.EnigmaEngine;
import enigmaEngine.impl.PlugBoardImpl;
import enigmaEngine.impl.ReflectorImpl;
import enigmaEngine.impl.RotorImpl;
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

//TODO: implement this class , Guy Becken
public class EnigmaMachineFromXML implements InitializeEnigma {
    private CreateAndValidateEnigmaComponentsImpl createAndValidateEnigmaComponents;

    @Override
    public EnigmaEngine getEnigmaEngineFromSource(String path) throws FileNotFoundException, JAXBException, RuntimeException, InvalidABCException, InvalidReflectorException, InvalidRotorException {
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

    private EnigmaEngine getEnigmaEngine(String path, CTEEnigma xmlOutput) throws RuntimeException, InvalidABCException, InvalidReflectorException, InvalidRotorException {

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
            throw new RuntimeException("ERROR! The XML that is given does not contain any machine.");
        }

        cteMachineABC = machine.getABC().trim();
        createAndValidateEnigmaComponents = new CreateAndValidateEnigmaComponentsImpl(cteMachineABC);
        createAndValidateEnigmaComponents.ValidateABC(cteMachineABC);

        cteRotorsCount = machine.getRotorsCount();
        cteRotors = machine.getCTERotors();
        if (cteRotorsCount > cteRotors.getCTERotor().size()) { //TODO: ??
            throw new RuntimeException("ERROR! The XML that is given, contains in its settings more needed rotors than actual rotors.");
        }

        cteReflectors = new ArrayList<>(machine.getCTEReflectors().getCTEReflector());
        // TODO: check that all rotors IDs are given from 1 to n without empty IDs
        for (CTERotor rotor : cteRotors.getCTERotor()) {
            int id = rotor.getId(), notch = rotor.getNotch() - 1;
            List<Character> right = new ArrayList<>();
            List<Character> left = new ArrayList<>();
            for (CTEPositioning pair : rotor.getCTEPositioning()) {
                right.add(pair.getRight().charAt(0));
                left.add(pair.getLeft().charAt(0));
            }

            if (rotors.containsKey(id)) {
                throw new InvalidRotorException("Error! two rotors have the same ID.");
            } else
                rotors.put(id, createAndValidateEnigmaComponents.createRotor(id, notch, right, left));
        }

        createAndValidateEnigmaComponents.validateRotorsIDs(rotors);

        // TODO: check if the 'reflectors' list size is maximum 5
        for (CTEReflector reflector : cteReflectors) {
            enigmaEngine.interfaces.Reflector.ReflectorID id;
            try {
                id = enigmaEngine.interfaces.Reflector.ReflectorID.valueOf(reflector.getId().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("ERROR! Invalid ID for enum "
                        + enigmaEngine.interfaces.Reflector.ReflectorID.class.getSimpleName() // TODO: check if this line works
                        + " of a given reflector: " + reflector.getId()
                        + ". Valid IDs are only: " + Arrays.toString(Reflector.ReflectorID.values()));
            }

            List<Integer> input = new ArrayList<>();
            List<Integer> output = new ArrayList<>();
            for (CTEReflect pair : reflector.getCTEReflect()) {
                if (pair.getInput() == pair.getOutput()) {
                    throw new RuntimeException("ERROR! The XML that is given contains a reflector that maps a letter to itself.");
                }

                input.add(pair.getInput());
                output.add(pair.getOutput());
            }
            if (reflectors.containsKey(id)) {
                throw new InvalidReflectorException("Error! two reflectors have the same ID.");
            } else
                reflectors.put(id, createAndValidateEnigmaComponents.createReflector(input, output, id));
        }

        createAndValidateEnigmaComponents.validateReflectorsIDs(reflectors);

        return new EnigmaEngine(rotors, reflectors, new PlugBoardImpl(), cteMachineABC);
    }
}
