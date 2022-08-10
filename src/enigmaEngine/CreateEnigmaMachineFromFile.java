package enigmaEngine;

import enigmaEngine.impl.EnigmaEngine;
import enigmaEngine.impl.Reflector;
import enigmaEngine.impl.Rotor;
import enigmaEngine.interfaces.InitializeEnigmaComponents;
import enigmaEngine.schemaBinding.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public enum CreateEnigmaMachineFromFile implements InitializeEnigmaComponents {
    XML {
        @Override
        public EnigmaEngine getEnigmaEngineFromSource(String path) {
            CTEEnigma xmlOutput = null;
            try {
                if (path.contains(".xml") == false) {
                    throw new FileNotFoundException("File given is not of XML type.");
                }
                InputStream xmlFile = new FileInputStream(path);
                JAXBContext jaxbContext = JAXBContext.newInstance("enigmaEngine.schemaBinding");
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                xmlOutput = (CTEEnigma)jaxbUnmarshaller.unmarshal(xmlFile);
            } catch (JAXBException | FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                assert xmlOutput != null;
                CTEMachine machine = xmlOutput.getCTEMachine();
                if (machine == null) {
                    throw new RuntimeException("ERROR! The XML that is given does not contain any machine.");
                }
                String cteMachineABC = machine.getABC().trim();
                if (cteMachineABC.length() % 2 == 1) {
                    throw new RuntimeException("ERROR! The XML that is given contains an odd number of letters.");
                }
                int cteRotorsCount = machine.getRotorsCount();
                CTERotors cteRotors = machine.getCTERotors();
                if (cteRotorsCount > machine.getCTEReflectors().getCTEReflector().size()) {
                    throw new RuntimeException("ERROR! The XML that is given, contains in its settings more needed rotors than actual rotors.");
                }
                List<CTEReflector> cteReflectors = new ArrayList<>(machine.getCTEReflectors().getCTEReflector());

                List<Rotor> rotors = new ArrayList<>();
                // TODO: check that all rotors IDs are given from 1 to n without empty IDs
                for (CTERotor rotor : cteRotors.getCTERotor()) {
                    int id = rotor.getId(), notch = rotor.getNotch();
                    if (0 >= notch || notch >= cteMachineABC.length()) {
                        throw new RuntimeException("ERROR! The XML that is given contains an invalid notch index of a given rotor.");
                    }
                    // TODO: check if one of the numbers is bigger than the length of the ABC or non-positive or appears twice
                    List<Character> right = new ArrayList<>(); // TODO: may change to HashMap in order to check if a given letter is already given
                    List<Character> left = new ArrayList<>();
                    for (CTEPositioning pair : rotor.getCTEPositioning()) {
                        /*if (pair.getRight().equals(pair.getLeft()) == true) {
                            throw new RuntimeException("ERROR! The XML that is given contains a rotor that maps to itself.");
                        }*/
                        right.add(pair.getRight().charAt(0));
                        left.add(pair.getLeft().charAt(0));
                    }
                    rotors.add(new Rotor(id, notch, right, left));
                }
                // TODO: check if the 'reflectors' list size is maximum 5
                List<Reflector> reflectors = new ArrayList<>();
                for (CTEReflector reflector : cteReflectors) {
                    enigmaEngine.interfaces.Reflector.ReflectorID id;
                    try {
                        id = enigmaEngine.interfaces.Reflector.ReflectorID.valueOf(reflector.getId().toUpperCase());
                    } catch(IllegalArgumentException e) {
                        throw new RuntimeException("ERROR! Invalid ID for enum "
                                + enigmaEngine.interfaces.Reflector.ReflectorID.class.getSimpleName() // TODO: check if this line works
                                + " of a given reflector: " + reflector.getId()
                                + ". Valid IDs are only: I, II, III, VI, V.");
                    }
                    // TODO: check if one of the numbers is bigger than the length of the ABC or non-positive or appears twice
                    List<Integer> input = new ArrayList<>();
                    List<Integer> output = new ArrayList<>();
                    for (CTEReflect pair : reflector.getCTEReflect()) {
                        if (pair.getInput() == pair.getOutput()) {
                            throw new RuntimeException("ERROR! The XML that is given contains a reflector that maps a letter to itself.");
                        }
                        input.add(pair.getInput());
                        output.add(pair.getOutput());
                    }
                    reflectors.add(new enigmaEngine.impl.Reflector(input, output, id));
                }
                return null;
            } catch (RuntimeException e) {
                e.printStackTrace(); // TODO: check how to print the error message to screen
                return null;
            } catch (AssertionError e) {
                throw new RuntimeException("ERROR! File " + path + " is not of XML format.");
            }
        }
    },
    JSON {
        @Override
        public EnigmaEngine getEnigmaEngineFromSource(String path) {
            return null;
        }
    };

    abstract public EnigmaEngine getEnigmaEngineFromSource(String path);

    public static void main(String[] args) {
        // WHAT I DID IN CMD: xjc -d . -p enigmaEngine.schemaBinding Enigma-Ex1.xsd
        ((InitializeEnigmaComponents) CreateEnigmaMachineFromFile.XML).getEnigmaEngineFromSource("ex1-sanity-small.xml");
    }
}
