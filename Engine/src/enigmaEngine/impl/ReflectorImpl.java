package enigmaEngine.impl;

import enigmaEngine.interfaces.Reflector;
import immutables.ReflectorID;

import java.io.*;
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
        // returns ReflectorDictionary[index], returns pairIndex
        return this.getIndexPairs().get(idx);
    }

    @Override
    public ReflectorID getReflectorID() {
        return this.id;
    }

    private HashMap<Integer, Integer> getIndexPairs() {
        return indexPairs;
    }

    @Override
    public Reflector clone()  {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            return (Reflector) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}