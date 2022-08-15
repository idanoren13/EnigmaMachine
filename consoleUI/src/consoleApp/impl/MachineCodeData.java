package consoleApp.impl;

import java.util.ArrayList;
import java.util.List;

public class MachineCodeData {
    private final String machineCode;
    private final List<MachineActivateData> machineActivateData;

    public MachineCodeData(String machineCode) {
        this.machineCode = machineCode;
        this.machineActivateData = new ArrayList<>();
    }

    public List<MachineActivateData> getMachineActivateData() {
        return machineActivateData;
    }

    public String getMachineCode() {
        return machineCode;
    }
}
