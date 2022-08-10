//package enigmaEngine;
//
//import enigmaEngine.impl.EnigmaEngine;
//import enigmaEngine.impl.ReflectorImpl;
//import enigmaEngine.impl.RotorImpl;
//import enigmaEngine.interfaces.InitializeEnigmaComponents;
//import enigmaEngine.schemaBinding.*;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public enum CreateEnigmaMachineFromFile implements InitializeEnigmaComponents {
//    XML {
//        @Override
////        public EnigmaEngine getEnigmaEngineFromSource(String path) {
////            CTEEnigma xmlOutput = null;
////            try {
////                if (path.contains(".xml") == false) {
////                    throw new FileNotFoundException("File given is not of XML type.");
////                }
////                InputStream xmlFile = new FileInputStream(path);
////                JAXBContext jaxbContext = JAXBContext.newInstance("enigmaEngine.schemaBinding");
////                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
////                xmlOutput = (CTEEnigma)jaxbUnmarshaller.unmarshal(xmlFile);
////            } catch (JAXBException | FileNotFoundException e) {
////                e.printStackTrace();
////            }
////            return getEnigmaEngine(path, xmlOutput);
////        }
//    },
//    JSON {
//        @Override
//        public EnigmaEngine getEnigmaEngineFromSource(String path) {
//            return null;
//        }
//    };
//
//    abstract public EnigmaEngine getEnigmaEngineFromSource(String path);
//
//    public static void main(String[] args) {
//        // WHAT I DID IN CMD: xjc -d . -p enigmaEngine.schemaBinding Enigma-Ex1.xsd
//        ((InitializeEnigmaComponents) CreateEnigmaMachineFromFile.XML).getEnigmaEngineFromSource("ex1-sanity-small.xml");
//    }
//}
