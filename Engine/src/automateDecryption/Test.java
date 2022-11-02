package automateDecryption;

import enigmaEngine.InitializeEnigmaEngine;
import enigmaEngine.exceptions.*;
import enigmaEngine.interfaces.EnigmaEngine;
import immutables.engine.Difficulty;
import immutables.engine.ReflectorID;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static ReflectorID selectedReflectorID = ReflectorID.II;
    static char firstRotorStartingPosition = 'B';
    static char secondRotorStartingPosition = 'U';
    static char thirdRotorStartingPosition = 'G';

    public static void main(String[] args) {
        try {
            String message = "dolphine then it am quality eye moon system folder light letter";
            String encryptedMessage;
            InitializeEnigmaEngine initializeEnigmaEngine = new InitializeEnigmaEngine();
            EnigmaEngine engine = initializeEnigmaEngine.initializeEngine(InitializeEnigmaEngine.SourceMode.XML, "C:\\Users\\idano\\IdeaProjects\\EnigmaMachine\\Engine\\src\\Resources\\ex2-basic.xml");
            List<Integer> rotors = new ArrayList<>();
            rotors.add(1);
            rotors.add(2);
            rotors.add(3);
            List<Character> startingPositions = new ArrayList<>();
            startingPositions.add(firstRotorStartingPosition);
            startingPositions.add(secondRotorStartingPosition);
            startingPositions.add(thirdRotorStartingPosition);
            engine.setSelectedRotors(rotors, startingPositions);
            engine.setSelectedReflector(selectedReflectorID);

            System.out.println("machine code: " + engine.getMachineCode().toString());
            encryptedMessage = engine.processMessage(message);
            System.out.println("machine code: " + engine.getMachineCode().toString());

            System.out.println(encryptedMessage);
            engine.reset();
            System.out.println("machine code: " + engine.getMachineCode().toString());

            System.out.println(engine.processMessage(encryptedMessage));
            System.out.println("machine code: " + engine.getMachineCode().toString());

            engine.reset();
            System.out.println("machine code: " + engine.getMachineCode().toString());

            TasksManager tasksManager = new TasksManager(10, encryptedMessage);
            tasksManager.initialize(engine, Difficulty.EASY);
//            new Thread(tasksManager).start();

//            tasksManager.test();

        } catch (InvalidRotorException | InvalidABCException | InvalidReflectorException | JAXBException | IOException |
                 UnknownSourceException | InvalidMachineException | InvalidCharactersException e) {
            throw new RuntimeException(e);
        }
    }
}
