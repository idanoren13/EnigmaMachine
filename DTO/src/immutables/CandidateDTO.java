package immutables;

import enigmaEngine.MachineCode;

import java.io.Serializable;

public class CandidateDTO implements Serializable {
    private final String candidateString;
    private final MachineCode machineCode;
    private final String agentName;

    public CandidateDTO(String candidateString, MachineCode machineCode, String agentName) {
        this.candidateString = candidateString;
        this.machineCode = machineCode;
        this.agentName = agentName;
    }

    public String getCandidateString() {
        return candidateString;
    }

    public MachineCode getMachineCode() {
        return machineCode;
    }

    public String getAgentName(){return agentName;}
}
