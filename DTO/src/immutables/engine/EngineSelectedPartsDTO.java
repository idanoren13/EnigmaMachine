package immutables.engine;

import java.util.List;

public class EngineSelectedPartsDTO {
    private final int NumberOfRotors;
    private final int totalReflectors;
    private final List<Character> ABC;

    public EngineSelectedPartsDTO(int totalNumberOfRotors, int totalReflectors, List<Character> ABC) {
        this.NumberOfRotors = totalNumberOfRotors;
        this.totalReflectors = totalReflectors;
        this.ABC = ABC;
    }

    public int getNumberOfRotors() {
        return NumberOfRotors;
    }

    public int getNumberOfReflectors() {
        return totalReflectors;
    }

    public List<Character> getABC() {
        return ABC;
    }
}