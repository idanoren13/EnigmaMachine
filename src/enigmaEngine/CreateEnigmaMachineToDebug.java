package enigmaEngine;

import enigmaEngine.impl.EnigmaEngine;
import enigmaEngine.impl.PlugBoardImpl;
import enigmaEngine.impl.ReflectorImpl;
import enigmaEngine.impl.RotorImpl;
import enigmaEngine.interfaces.InitializeEnigmaComponents;
import enigmaEngine.interfaces.PlugBoard;
import enigmaEngine.interfaces.Reflector;
import enigmaEngine.interfaces.Rotor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Stream;

public class CreateEnigmaMachineToDebug implements InitializeEnigmaComponents {
    @Override
    public EnigmaEngine getEnigmaEngineFromSource() {
        ArrayList<Character> abc = new ArrayList<Character>();
        HashMap<Integer, Rotor> rotors = new HashMap<Integer, Rotor>();
        HashMap<Integer, Reflector> reflectors = new HashMap<Integer, Reflector>();
        PlugBoard plugBoard = new PlugBoardImpl();

        abc.add('A');
        abc.add('B');
        abc.add('C');
        abc.add('D');
        abc.add('E');
        abc.add('F');

        rotors.put(1, new RotorImpl(1, 1, stringToArrayList("ABCDEF"), stringToArrayList("FEDCBA")));
        rotors.put(2, new RotorImpl(2, 1, stringToArrayList("ABCDEF"), stringToArrayList("FEDCBA")));
        rotors.put(3, new RotorImpl(3, 1, stringToArrayList("ABCDEF"), stringToArrayList("FEDCBA")));

        int [] right = {1,2,3};
        int [] left = {4,5,6};
        HashMap<Integer,Integer> Pairs = new HashMap<Integer,Integer>();

        Pairs.put(1,4);
        Pairs.put(2,5);
        Pairs.put(3,6);
        Pairs.put(4,1);
        Pairs.put(5,2);
        Pairs.put(6,3);

        reflectors.put(1, new ReflectorImpl(Pairs,
                                            Reflector.ReflectorID.I));
        return  new EnigmaEngine(rotors, reflectors, plugBoard, abc);
    }

    private ArrayList<Character> stringToArrayList(String input) {
        ArrayList<Character> output = new ArrayList<Character>();
        for (int i = 0; i < input.length(); i++) {
            output.add(input.charAt(i));
        }
        return output;
    }
}
