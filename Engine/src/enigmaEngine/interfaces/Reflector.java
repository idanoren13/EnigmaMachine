package enigmaEngine.interfaces;

public interface Reflector {

    enum ReflectorID {
        I,
        II,
        III,
        IV,
        V
    }
    // TO DO may call this ReflectIndex
    int findPairByIndex(int idx); // returns ReflectorDictionary[index], returns pairIndex
}
