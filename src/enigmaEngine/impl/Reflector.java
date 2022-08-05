package enigmaEngine.impl;

import java.util.HashMap;
import enigmaEngine.Factory.ReflectorID;

public class Reflector implements enigmaEngine.Reflector {
    private final HashMap<Integer, Integer> indexPairs;
    private final ReflectorID id;

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