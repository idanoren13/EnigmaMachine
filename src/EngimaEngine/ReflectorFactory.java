package EngimaEngine;

public interface ReflectorFactory {
    public enum ReflectorID {
        I,
        II,
        III,
        IV,
        V;
    }
    Reflector createNewReflector(ReflectorID id, String right, String left);
}
