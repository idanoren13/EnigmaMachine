package uBoatApp.frontEnd;

import java.util.List;

// TODO: move to engine
// TODO: may make this DTO
public interface XMLLoader {
    List<Integer> getRotorsFromXML(); // All rotors IDs in XML
    List<String> getReflectorsFromXML(); // All reflectors IDs in XML
    List<Character> getABCFromXML(); // All ABC characters in XML
    List<String> getDictionaryWordsFromXML(); // All fixed and valid words from dictionary in XML
    int getTotalAgents(); // Amount of agents in XML
}
