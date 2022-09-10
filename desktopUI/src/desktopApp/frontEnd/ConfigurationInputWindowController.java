package desktopApp.frontEnd;

import desktopApp.interfaces.XMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.ListSelectionView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigurationInputWindowController implements Initializable {
    private final Screen1Controller screen1Controller;
    @FXML private ListSelectionView<Integer> rotorIDs;
    @FXML private ListSelectionView<String> reflectorIDs;
    private List<Character> abcFromXML;

    public ConfigurationInputWindowController(XMLLoader xmlLoader, Screen1Controller screen1Controller) {
        this.screen1Controller = screen1Controller;
        rotorIDs = new ListSelectionView<>();
        reflectorIDs = new ListSelectionView<>();
        abcFromXML = new ArrayList<>(xmlLoader.getABCFromXML());
        ObservableList<Integer> rotorsOList = FXCollections.observableArrayList(xmlLoader.getRotorsFromXML());
        ObservableList<String> reflectorsOList = FXCollections.observableArrayList(xmlLoader.getReflectorsFromXML());
        rotorIDs.getSourceItems().addAll(rotorsOList);
        reflectorIDs.getSourceItems().addAll(reflectorsOList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}