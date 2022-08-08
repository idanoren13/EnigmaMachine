package enigmaEngine.impl;

import enigmaEngine.Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class EnigmaEngine {
    private final HashMap<Integer, RotorImpl> rotors;
    private final HashMap<Integer,Reflector> reflectors;
    private final PlugBoard plugBoard;
    private final ArrayList<Character> machineABC;
    private Stack<RotorImpl> rotorStackRightToLeft;
    private Stack<RotorImpl> rotorStackLeftToRight;
    private List<Integer> selectedRotors;

    private Reflector selectedReflector;

    public EnigmaEngine(HashMap<Integer, RotorImpl> rotors, HashMap<Integer,Reflector> reflector, PlugBoard plugBoard, ArrayList<Character> abc) {
        this.rotors = rotors;
        this.reflectors = reflector;
        this.plugBoard = plugBoard;
        this.machineABC = abc;
    }

    public char activate(char input) {
        char out = '\0';
        int index;
        char temp;

        //rotate the first rotor
        rotorStackRightToLeft.peek().rotate();

        temp = plugBoard.returnCharacterPair(input);
        index = machineABC.indexOf(temp);

        //pipeline to the reflector
        while (!rotorStackRightToLeft.isEmpty()){
            index = rotorStackRightToLeft.peek().getOutputIndex(index, Engine.Direction.LEFT);
            rotorStackLeftToRight.push(rotorStackRightToLeft.pop());
        }

        index = selectedReflector.findPairByIndex(index);

        //pipeline form the reflector
        while (!rotorStackLeftToRight.isEmpty()){
            index = rotorStackLeftToRight.peek().getOutputIndex(index, Engine.Direction.RIGHT);
            rotorStackRightToLeft.push(rotorStackLeftToRight.pop());
        }

        temp = machineABC.get(index);
        out = plugBoard.returnCharacterPair(temp);

        return out;
    }

    //creates the array of the selected rotors in order
    public void setSelectedRotors(List<Integer> rotorsID){
        selectedRotors = rotors.keySet().stream().filter(rotorsID::contains).collect(Collectors.toList()); //TODO: need to check
        connectRotors();
        selectedRotors.forEach(rotorID ->rotorStackRightToLeft.push(rotors.get(rotorID)));
    }

    private void connectRotors() {
        disconnectAllRotors();
        rotors.get(selectedRotors.get(0)).setRotateNextRotor(null);

        for (int i = selectedRotors.size() - 1; i > 0; i--) {
            try {
                rotors.get(selectedRotors.get(i)).setRotateNextRotor(rotors.get(selectedRotors.get(i - 1)).getClass().getMethod("rotate", null));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void disconnectAllRotors(){
        rotors.keySet().forEach(key ->rotors.get(key).setRotateNextRotor(null));
    }

    public void setSelectedReflector(int selectedReflectorID) {
        this.selectedReflector = reflectors.get(selectedReflectorID);
    }
}
