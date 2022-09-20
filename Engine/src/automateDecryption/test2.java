package automateDecryption;

import enigmaEngine.InitializeEnigmaEngine;
import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.EnigmaEngine;
import enigmaEngine.interfaces.Reflector;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class test2 {
    public static Reflector.ReflectorID _selectedReflectorID = Reflector.ReflectorID.II;
    static char _firstRotorStartingPosition = 'B';
    static char _secondRotorStartingPosition = 'U';
    static char _thirdRotorStartingPosition = 'G';

    public static void main(String[] args) {
        try {
            String message = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
            String encryptedMessage;
            InitializeEnigmaEngine initializeEnigmaEngine = new InitializeEnigmaEngine();
            EnigmaEngine engine = initializeEnigmaEngine.initializeEngine(InitializeEnigmaEngine.SourceMode.XML, "C:\\Users\\idano\\IdeaProjects\\EnigmaMachine\\Engine\\src\\Resources\\ex2-basic.xml");
            List<Integer> rotors = new ArrayList<>();
            rotors.add(1);
            rotors.add(2);
            rotors.add(3);
            List<Character> startingPositions = new ArrayList<>();
            startingPositions.add(_firstRotorStartingPosition);
            startingPositions.add(_secondRotorStartingPosition);
            startingPositions.add(_thirdRotorStartingPosition);
            engine.setSelectedRotors(rotors, startingPositions);
            engine.setSelectedReflector(_selectedReflectorID);

            for(int i = 0 ;i < 1000;i++) {
                engine.reset();
                encryptedMessage = engine.processMessage(message);
                System.out.println(encryptedMessage);
                engine.reset();
                System.out.println(engine.processMessage(encryptedMessage));
            }
            //
//            TasksManager tasksManager = new TasksManager(1, encryptedMessage);
//            tasksManager.initialize(engine, Difficulty.EASY);
//            new Thread(tasksManager).start();
//
//            tasksManager.test();
//
        } catch (InvalidRotorException | InvalidABCException | InvalidReflectorException | JAXBException | IOException |
                 UnknownSourceException | InvalidMachineException | InvalidCharactersException e) {
            throw new RuntimeException(e);
        }
    }
}