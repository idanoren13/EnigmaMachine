package enigmaEngine;

import enigmaEngine.impl.EnigmaEngineImpl;
import enigmaEngine.impl.PlugBoardImpl;
import enigmaEngine.impl.ReflectorImpl;
import enigmaEngine.impl.RotorImpl;
import enigmaEngine.interfaces.InitializeEnigma;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;
import immutables.engine.ReflectorID;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("SpellCheckingInspection")
public class EnigmaMachineToDebug implements InitializeEnigma {
    @Override
    public EnigmaEngineImpl getEnigmaEngineFromSource(String source) {
        String abc = "ABCDEFGHIJKL";
        HashMap<Integer, Rotor> rotors = new HashMap<>();
        HashMap<ReflectorID, Reflector> reflectors = new HashMap<>();

        enigmaEngine.interfaces.PlugBoard plugBoard = new PlugBoardImpl();
        plugBoard.addPair('C', 'B');

        rotors.put(1, new RotorImpl(1, 3, stringToArrayList("ABCDEFGHIJKL"), stringToArrayList("HAIJCDEGLKBF")));
        rotors.put(3, new RotorImpl(3, 5, stringToArrayList("ABCDEFGHIJKL"), stringToArrayList("GHICDEFJKLAB")));
        rotors.put(2, new RotorImpl(2, 0, stringToArrayList("ABCDEFGHIJKL"), stringToArrayList("CFLBHIGKDEGA")));

        HashMap<Integer, Integer> Pairs1 = new HashMap<>();
        // Decrementing the index by 1 to match the index of the abc arraylist
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

        HashMap<Integer, Integer> Pairs2 = new HashMap<>();
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

        reflectors.put(ReflectorID.I, new ReflectorImpl(Pairs1, ReflectorID.I));
        reflectors.put(ReflectorID.II, new ReflectorImpl(Pairs2, ReflectorID.II));

        return new EnigmaEngineImpl(rotors, reflectors, plugBoard, abc);
    }

    private ArrayList<Character> stringToArrayList(String input) {
        ArrayList<Character> output = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            output.add(input.charAt(i));
        }
        return output;
    }
}
