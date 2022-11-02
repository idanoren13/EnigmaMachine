package enigmaEngine.interfaces;

import immutables.engine.ReflectorID;

import java.io.Serializable;

public interface Reflector extends Serializable {

    int findPairByIndex(int idx); // returns ReflectorDictionary[index], returns pairIndex
    ReflectorID getReflectorID();
}