package enigmaEngine.impl;

import java.util.HashMap;
import java.util.List;

public class ReflectorImpl implements enigmaEngine.interfaces.Reflector {
    private final HashMap<Integer, Integer> indexPairs;
    private final ReflectorID id;

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
        // returns ReflectorDictionary[index], returns pairIndex
        return this.getIndexPairs().get(idx);
    }

    private HashMap<Integer, Integer> getIndexPairs() {
        return indexPairs;
    }
}