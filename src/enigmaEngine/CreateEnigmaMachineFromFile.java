package enigmaEngine;

import enigmaEngine.impl.EnigmaEngine;
import enigmaEngine.interfaces.InitializeEnigmaComponents;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;
//import enigmaEngine.schemaBinding.*;  //TODO: implement this class , Guy Becken
import enigmaEngine.xmlReader.schemaBinding.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
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
                InputStream xmlFile = new FileInputStream(path);
                JAXBContext jaxbContext = JAXBContext.newInstance("enigmaEngine.schemaBinding");
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                xmlOutput = (CTEEnigma)jaxbUnmarshaller.unmarshal(xmlFile);
            } catch (JAXBException | FileNotFoundException e) {
                e.printStackTrace();
            }
            CTEMachine machine = xmlOutput.getCTEMachine();
            String cteMachineABC = machine.getABC();
            int cteRotorsCount = machine.getRotorsCount();
            CTERotors cteRotors = machine.getCTERotors();
            List<CTEReflector> cteReflectors = new ArrayList<>(machine.getCTEReflectors().getCTEReflector());

            List<Rotor> rotors = new ArrayList<>();
            for (CTERotor rotor : cteRotors.getCTERotor()) {
                int id = rotor.getId(), notch = rotor.getNotch();
                // TODO: check if all notches in rotors are between 1 to the ABC length
                // TODO: check if one of the numbers is bigger than the length of the ABC or non-positive or appears twice
                List<Character> right = new ArrayList<>();
                List<Character> left = new ArrayList<>();
                for (CTEPositioning pair : rotor.getCTEPositioning()) {
                    right.add(pair.getRight().charAt(0));
                    left.add(pair.getLeft().charAt(0));
                }
                rotors.add(new enigmaEngine.impl.RotorImpl(id, notch, right, left));
            }

            // TODO: check if the 'reflectors' list size is maximum 5
            List<Reflector> reflectors = new ArrayList<>();
            for (CTEReflector reflector : cteReflectors) {
                Reflector.ReflectorID id = Reflector.ReflectorID.valueOf(reflector.getId().toUpperCase());
                // TODO: check if the id is: I, II, III, VI or V
                // TODO: check if one of the numbers is bigger than the length of the ABC or non-positive or appears twice
                List<Integer> input = new ArrayList<>();
                List<Integer> output = new ArrayList<>();
                for (CTEReflect pair : reflector.getCTEReflect()) {
                    input.add(pair.getInput());
                    output.add(pair.getOutput());
                }
                reflectors.add(new enigmaEngine.impl.ReflectorImpl(input, output, id));
            }
            if (cteMachineABC.trim().length() % 2 == 1) {
                // TODO: show message
                // ERROR, odd number of letters.
            }
            return null;
        }
    },
    JSON {
        @Override
        public EnigmaEngine getEnigmaEngineFromSource(String path) {
            return null;
        }

    };

    abstract public EnigmaEngine getEnigmaEngineFromSource(String path);

    public static void main(String[] args) throws JAXBException {
        // WHAT I DID IN CMD: xjc -d . -p enigmaEngine.schemaBinding Enigma-Ex1.xsd
        ((InitializeEnigmaComponents) CreateEnigmaMachineFromFile.XML).getEnigmaEngineFromSource("ex1-sanity-small.xml");
    }
}
