package consoleApp.impl;

import java.util.ArrayList;
import java.util.List;

public class MachineHistoryAndStatistics {
    private final List<MachineCodeData> machineCodeData;

    public MachineHistoryAndStatistics() {
        this.machineCodeData = new ArrayList<>();
    }

    public void addMachineCodeData(MachineCodeData machineCodeData) {
        this.machineCodeData.add(machineCodeData);
    }

    public String getFirstMachineCode() {
        return machineCodeData.get(0).getMachineCode();
    }

    public String getCurrentMachineCode() {
        return machineCodeData.get(this.machineCodeData.size() - 1).getMachineCode();
    }

}
