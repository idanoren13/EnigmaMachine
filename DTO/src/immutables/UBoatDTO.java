package immutables;

public class UBoatDTO {
    private final BattlefieldDTO battlefield;
    private final EngineDTO engine;

    public UBoatDTO(BattlefieldDTO battlefield, EngineDTO engine) {
        this.battlefield = battlefield;
        this.engine = engine;
    }

    public BattlefieldDTO getBattlefield() {
        return battlefield;
    }

    public EngineDTO getEngine() {
        return engine;
    }
}
