package immutables;

import enigmaEngine.impl.PlugBoardImpl;
import enigmaEngine.impl.ReflectorImpl;
import enigmaEngine.impl.RotorImpl;

import java.io.Serializable;
import java.util.HashMap;

public class EnginePartsDTO implements Serializable {
    private final java.util.HashMap<Integer, RotorImpl> rotors;
    private final HashMap<ReflectorID, ReflectorImpl> reflectors;
    private final PlugBoardImpl plugBoard;
    private final String abc;

    public EnginePartsDTO(HashMap<Integer, RotorImpl> rotors, HashMap<ReflectorID, ReflectorImpl> reflectors, PlugBoardImpl plugBoard, String abc) {
        this.rotors = rotors;
        this.reflectors = reflectors;
        this.plugBoard = plugBoard;
        this.abc = abc;
    }

    public HashMap<Integer, RotorImpl> getRotors() {
        return rotors;
    }

    public HashMap<ReflectorID, ReflectorImpl> getReflectors() {
        return reflectors;
    }

    public PlugBoardImpl getPlugBoard() {
        return plugBoard;
    }

    public String getAbc() {
        return abc;
    }
}
