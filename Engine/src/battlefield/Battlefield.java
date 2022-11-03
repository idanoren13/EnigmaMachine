package battlefield;

import Entities.AllyEntity;
import enigmaEngine.schemaBinding.CTEBattlefield;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Battlefield implements Serializable {
    private final BattlefieldInformation battlefieldInformation;
    private final List<AllyEntity> Allies;

//    public Battlefield(InputStream xmlInputStream) {
//        CTEBattlefield battlefield = getCTEBattlefield(xmlInputStream);
//
//        battlefieldInformation = BattlefieldInformation.loadBattlefieldFromXML(battlefield);
//
//        Allies = new ArrayList<>();
//    }
//
//    private CTEBattlefield getCTEBattlefield(InputStream xmlInputStream) {
//        try {
//            JAXBContext jaxbContext = JAXBContext.newInstance("enigmaEngine.schemaBinding");
//            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            return  (CTEBattlefield) jaxbUnmarshaller.unmarshal(xmlInputStream);
//        } catch (JAXBException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public Battlefield(CTEBattlefield cteBattlefield) {
        this.battlefieldInformation = BattlefieldInformation.loadBattlefieldFromXML(cteBattlefield);
        this.Allies = new ArrayList<>();

    }

    public BattlefieldInformation getBattlefieldInformation() {
        return battlefieldInformation;
    }
}
