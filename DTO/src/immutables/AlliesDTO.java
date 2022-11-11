package immutables;
import java.io.Serializable;
import java.util.List;

public class AlliesDTO implements Serializable {
    private List<AllyDTO> listOfAllies;

    public AlliesDTO(List<AllyDTO> listOfAllies) {
        this.listOfAllies = listOfAllies;
    }

    public List<AllyDTO> getListOfAllies() {
        return listOfAllies;
    }
}
