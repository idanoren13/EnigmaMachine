package enigmaEngine.impl;

import enigmaEngine.interfaces.PlugBoard;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlugBoardImpl implements PlugBoard, Serializable {
    private final HashMap<Character, Character> abcPairs;
    private final List<Pair<Character, Character>> pairList;

    public PlugBoardImpl() {
        this.abcPairs = new HashMap<>();
        this.pairList = new ArrayList<>();
    }

    public PlugBoardImpl(List<Pair<Character, Character>> pairList) {
        this.pairList = pairList;
        this.abcPairs = generateInputIntoPairs(pairList);
    }


    private HashMap<Character, Character> generateInputIntoPairs(List<Pair<Character, Character>> pairList) {
        HashMap<Character, Character> abcPairs = new HashMap<>();
        for (Pair<Character, Character> pair : pairList) {
            abcPairs.put(pair.getKey(), pair.getValue());
            abcPairs.put(pair.getValue(), pair.getKey());
        }
        return abcPairs;
    }

    @Override
    public char returnCharacterPair(char input) {
        char res = input;

        if (abcPairs.containsKey(input))
            res = abcPairs.get(input);

        return res;
    }

    @Override
    public void addPair(char a, char b) {
        this.abcPairs.put(a, b);
        this.abcPairs.put(b, a);
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap<Character, Character> getPairs() {
        return (HashMap<Character, Character>) this.abcPairs.clone();
    }

    @Override
    public boolean containsPair(Pair<Character, Character> pair) {
        return this.abcPairs.containsKey(pair.getKey()) && this.abcPairs.containsKey(pair.getValue());
    }

    @Override
    public List<Pair<Character, Character>> getPairList() {
        return this.pairList;
    }

    @Override
    public PlugBoard clonePlugBoard() {
        return new PlugBoardImpl(new ArrayList<>(this.pairList));
    }

    @Override
    public PlugBoard clone() {
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
            return (PlugBoard) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}