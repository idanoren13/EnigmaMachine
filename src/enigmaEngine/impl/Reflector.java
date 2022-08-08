package enigmaEngine.impl;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

import enigmaEngine.Factory.ReflectorID;

public class Reflector implements enigmaEngine.Reflector {
    private final HashMap<Integer, Integer> indexPairs;
    private final ReflectorID id;

    public Reflector(List<Integer> input, List<Integer> output, ReflectorID id) {
        this.id = id;
        this.indexPairs = new HashMap<Integer, Integer>();

        for (int i = 0; i < input.size(); i++) {
            indexPairs.put(input.get(i), output.get(i));
            indexPairs.put(output.get(i), input.get(i));
        }
    }

    public Reflector(HashMap<Integer, Integer> indexPairs, ReflectorID id) {
        this.indexPairs = indexPairs;
        this.id = id;
    }

    public HashMap<Integer, Integer> getIndexPairs() {
        return indexPairs;
    }

    @Override
    public int findPairByIndex(int idx) {
        // returns ReflectorDictionary[index], returns pairIndex
        return this.getIndexPairs().get(idx);
    }
}