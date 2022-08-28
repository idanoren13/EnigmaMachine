package enigmaEngine.interfaces;

public interface Reflector {

    enum ReflectorID {
        I,
        II,
        III,
        IV,
        V;
    }
    int findPairByIndex(int idx); // returns ReflectorDictionary[index], returns pairIndex
    ReflectorID getReflectorID();
}