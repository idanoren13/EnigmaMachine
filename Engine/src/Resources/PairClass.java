package Resources;

public class PairClass<K, V> {
    private final K key;
    private final V value;

    public PairClass(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public static <K, V> PairClass<K, V> of(K first, V second) {
        return new PairClass<>(first, second);
    }
    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
