package enigmaEngine.impl;

import enigmaEngine.interfaces.Reflector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ReflectorImpl implements Reflector, Serializable {
    private final HashMap<Integer, Integer> indexPairs;
    private final ReflectorID id;

    public ReflectorImpl(HashMap<Integer, Integer> indexPairs, ReflectorID id) {
        this.indexPairs = indexPairs;
        this.id = id;
    }

    public ReflectorImpl(List<Integer> input, List<Integer> output, ReflectorID id) {
        this.id = id;
        this.indexPairs = new HashMap<>();

        for (int i = 0; i < input.size(); i++) {
            indexPairs.put(input.get(i), output.get(i));
            indexPairs.put(output.get(i), input.get(i));
        }
    }

    @Override
    public int findPairByIndex(int idx) {
        return this.getIndexPairs().get(idx);
    }

    @Override
    public ReflectorID getReflectorID() {
        return this.id;
    }

    private HashMap<Integer, Integer> getIndexPairs() {
        return indexPairs;
    }
}