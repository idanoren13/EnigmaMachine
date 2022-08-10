package enigmaEngine;

import enigmaEngine.impl.EnigmaEngine;
import enigmaEngine.impl.PlugBoard;
import enigmaEngine.impl.Reflector;
import enigmaEngine.impl.Rotor;
import enigmaEngine.interfaces.InitializeEnigmaComponents;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateEnigmaMachineToDebug implements InitializeEnigmaComponents {
    @Override
    public EnigmaEngine getEnigmaEngineFromSource(String source) {
        ArrayList<Character> abc = new ArrayList<Character>();
        HashMap<Integer, enigmaEngine.interfaces.Rotor> rotors = new HashMap<Integer, enigmaEngine.interfaces.Rotor>();
        HashMap<Integer, enigmaEngine.interfaces.Reflector> reflectors = new HashMap<Integer, enigmaEngine.interfaces.Reflector>();
        abc.add('A');
        abc.add('B');
        abc.add('C');
        abc.add('D');
        abc.add('E');
        abc.add('F');
        abc.add('G');
        abc.add('H');
        abc.add('I');
        abc.add('J');
        abc.add('K');
        abc.add('L');

        enigmaEngine.interfaces.PlugBoard plugBoard = new PlugBoard();
        plugBoard.addPair('C', 'B');

//        rotors.put(1, new RotorImpl(1, 3, stringToArrayList("ABCDEF"), stringToArrayList("FEDCBA")));
//        rotors.put(3, new RotorImpl(3, 5, stringToArrayList("ABCDEF"), stringToArrayList("BADCFE")));
//        rotors.put(2, new RotorImpl(2, 0, stringToArrayList("ABCDEF"), stringToArrayList("EBDFCA")));
        rotors.put(1, new Rotor(1, 3, stringToArrayList("ABCDEFGHIJKL"), stringToArrayList("HAIJCDEGLKBF")));
        rotors.put(3, new Rotor(3, 5, stringToArrayList("ABCDEFGHIJKL"), stringToArrayList("GHICDEFJKLAB")));
        rotors.put(2, new Rotor(2, 0, stringToArrayList("ABCDEFGHIJKL"), stringToArrayList("CFLBHIGKDEGA")));

        HashMap<Integer, Integer> Pairs1 = new HashMap<Integer, Integer>();
        //decrementing the index by 1 to match the index of the abc arraylist
        Pairs1.put(0, 6);
        Pairs1.put(1, 7);
        Pairs1.put(2, 8);
        Pairs1.put(3, 9);
        Pairs1.put(4, 10);
        Pairs1.put(5, 11);
        Pairs1.put(6, 0);
        Pairs1.put(7, 1);
        Pairs1.put(8, 2);
        Pairs1.put(9, 3);
        Pairs1.put(10, 4);
        Pairs1.put(11, 5);

        HashMap<Integer, Integer> Pairs2 = new HashMap<Integer, Integer>();
        Pairs2.put(0, 1);
        Pairs2.put(1, 0);
        Pairs2.put(2, 3);
        Pairs2.put(3, 2);
        Pairs2.put(4, 5);
        Pairs2.put(5, 4);
        Pairs2.put(6, 7);
        Pairs2.put(7, 6);
        Pairs2.put(8, 9);
        Pairs2.put(9, 8);
        Pairs2.put(10, 11);
        Pairs2.put(11, 10);

        reflectors.put(1, new Reflector(Pairs1, enigmaEngine.interfaces.Reflector.ReflectorID.I));
        reflectors.put(2, new Reflector(Pairs2, enigmaEngine.interfaces.Reflector.ReflectorID.II));

        return new EnigmaEngine(rotors, reflectors, plugBoard, abc);
    }

    private ArrayList<Character> stringToArrayList(String input) {
        ArrayList<Character> output = new ArrayList<Character>();
        for (int i = 0; i < input.length(); i++) {
            output.add(input.charAt(i));
        }
        return output;
    }
}
