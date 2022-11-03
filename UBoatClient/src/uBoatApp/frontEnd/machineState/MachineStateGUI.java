package uBoatApp.frontEnd.machineState;

import immutables.EngineDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MachineStateGUI {
    private List<StringProperty> currentSelectedRotorsPositions;
    private List<Pair<IntegerProperty,IntegerProperty>> selectedRotorsAndNotchesPosition;
    private StringProperty selectedReflector;

    private Map<StringProperty, StringProperty> plugBoard;
    public MachineStateGUI(EngineDTO DTO) {
        selectedReflector = new SimpleStringProperty();
        updateMachineState(DTO);
    }
    public void updateMachineState(EngineDTO DTO) {
        setCurrentSelectedRotorsPositions(DTO.getCurrentSelectedRotorsPositions());
        setSelectedRotorsAndNotchesPosition(DTO.getSelectedRotorsAndNotchesPosition());
        setSelectedReflector(DTO.getSelectedReflector());
        setPlugBoard(DTO.getPlugBoard());
    }

    public List<StringProperty> getCurrentSelectedRotorsPositions() {
        return currentSelectedRotorsPositions;
    }

    public void setCurrentSelectedRotorsPositions(List<Character> currentSelectedRotorsPositions) {;
        /*List<StringProperty> lst = new ArrayList<>();
        for (Character ch : currentSelectedRotorsPositions) {
            lst.add(new SimpleStringProperty(ch.toString()));
        }
        this.currentSelectedRotorsPositions = lst;*/
        this.currentSelectedRotorsPositions = currentSelectedRotorsPositions.stream()
                .map(ch -> new SimpleStringProperty(ch.toString()))
                .collect(Collectors.toList());
    }

    public List<Pair<IntegerProperty, IntegerProperty>> getSelectedRotorsAndNotchesPosition() {
        return selectedRotorsAndNotchesPosition;
    }

    public void setSelectedRotorsAndNotchesPosition(List<Pair<Integer, Integer>> selectedRotorsAndNotchesPosition) {
        List<Pair<IntegerProperty, IntegerProperty>> lst = new ArrayList<>();
        for (Pair<Integer, Integer> p : selectedRotorsAndNotchesPosition) {
            lst.add(new Pair<>(new SimpleIntegerProperty(p.getKey()), new SimpleIntegerProperty(p.getValue())));
        }
        this.selectedRotorsAndNotchesPosition = lst;
    }

    public String getSelectedReflector() {
        return selectedReflector.get();
    }

    public StringProperty selectedReflectorProperty() {
        return selectedReflector;
    }

    public void setSelectedReflector(String selectedReflector) {
        this.selectedReflector.set(selectedReflector);
    }

    public Map<StringProperty, StringProperty> getPlugBoard() {
        return plugBoard;
    }

    public void setPlugBoard(Map<Character, Character> plugBoard) {
/*        List<Character> lst1 = new ArrayList<>(plugBoard.keySet());
        List<Character> lst2 = new ArrayList<>(plugBoard.values());
        Map<StringProperty, StringProperty> lst = new HashMap<>();
        for (int i = 0; i < lst1.size(); i++) {
            StringProperty str1 = new SimpleStringProperty(lst1.get(i).toString());
            StringProperty str2 = new SimpleStringProperty(lst2.get(i).toString());
            lst.put(str1, str2);
        }
        this.plugBoard = lst;*/
        this.plugBoard = new HashMap<>();
        plugBoard.forEach((k, v) -> this.plugBoard.put(new SimpleStringProperty(k.toString()), new SimpleStringProperty(v.toString())));
    }
}
