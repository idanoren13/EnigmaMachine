package uBoatApp.frontEnd;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class BasicAgentData implements AgentData {

    protected ListProperty<String> dictionaryWords;
    protected StringProperty machineCode;

    public BasicAgentData(List<String> dictionaryWords, String machineCode) {
        ObservableList<String> observableList = FXCollections.observableArrayList(dictionaryWords);

        this.dictionaryWords = new SimpleListProperty<>(observableList);
        this.machineCode = new SimpleStringProperty(machineCode);
    }

    @Override
    public List<String> getWordsDictionary() {
        return dictionaryWords.get();
    }

    public ListProperty<String> dictionaryWordsProperty() {
        return dictionaryWords;
    }

    @Override
    public void setWordsDictionary(List<String> dictionaryWords) {
        this.dictionaryWords.set(FXCollections.observableArrayList(dictionaryWords));
    }

    @Override
    public String getMachineCode() {
        return machineCode.get();
    }

    public StringProperty machineCodeProperty() {
        return machineCode;
    }

    @Override
    public void setMachineCode(String machineCode) {
        this.machineCode.set(machineCode);
    }
    @Override
    public String toString() {
        return dictionaryWords + " : " + machineCode;
    }
}
