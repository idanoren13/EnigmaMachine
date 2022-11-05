package immutables;

import enigmaEngine.MachineCode;

public class CandidateDTO {
    private final String candidateString;
    private final MachineCode machineCode;

    public CandidateDTO(String candidateString, MachineCode machineCode) {
        this.candidateString = candidateString;
        this.machineCode = machineCode;
    }

    public String getCandidateString() {
        return candidateString;
    }

    public MachineCode getMachineCode() {
        return machineCode;
    }
}
