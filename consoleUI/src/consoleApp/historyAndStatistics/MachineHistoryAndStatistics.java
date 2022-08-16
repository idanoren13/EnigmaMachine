package consoleApp.historyAndStatistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MachineHistoryAndStatistics implements Iterable<MachineCodeData> {
    private final List<MachineCodeData> machineCodeData;

    public MachineHistoryAndStatistics() {
        this.machineCodeData = new ArrayList<>();
    }

    public void add(MachineCodeData machineCodeData) {
        this.machineCodeData.add(machineCodeData);
    }

    public void addActivateDataToCurrentMachineCode(String rawData, String processedData, int timeElapsed) {
       machineCodeData.get(machineCodeData.size() - 1).addActivateData(rawData, processedData, timeElapsed);
    }



    public String getFirstMachineCode() {
        return machineCodeData.get(0).getMachineCode();
    }

    public String getCurrentMachineCode() {
        return machineCodeData.get(this.machineCodeData.size() - 1).getMachineCode();
    }

    public boolean isEmpty() {
        return this.machineCodeData.isEmpty();
    }

    @Override
    public Iterator<MachineCodeData> iterator() {
        return this.machineCodeData.iterator();
    }

    @Override
    public void forEach(Consumer<? super MachineCodeData> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<MachineCodeData> spliterator() {
        return Iterable.super.spliterator();
    }
}
