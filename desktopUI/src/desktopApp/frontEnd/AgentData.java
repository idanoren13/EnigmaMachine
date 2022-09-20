package desktopApp.frontEnd;

import java.util.List;

public interface AgentData {
    List<String> getWordsDictionary();
    void setWordsDictionary(List<String> dictionaryWords);

    String getMachineCode();

    void setMachineCode(String machineCode);
}
