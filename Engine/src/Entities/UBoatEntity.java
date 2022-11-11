package Entities;

import battlefield.Battlefield;
import enigmaEngine.EnigmaMachineFromXML;
import enigmaEngine.exceptions.InvalidCharactersException;
import enigmaEngine.interfaces.EnigmaEngine;
import immutables.AllyDTO;
import immutables.CandidateDTO;
import immutables.ContestDTO;
import immutables.ContestDataDTO;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class UBoatEntity implements Serializable {

    private String name;
    private EnigmaEngine enigmaEngine;
    private Battlefield battlefield;
    private String encryptedMessage;
    private String OriginalMessage;
    private EnigmaEngine dummyEngine;
    private String status = "open";
    transient private InputStream xmlFile;
    private List<CandidateDTO> candidateDTOList;
    private Boolean isReady = false;

    private Boolean isContestStarted = false;

    public EnigmaEngine getEnigmaEngine() {
        return enigmaEngine;
    }

    public void setEnigmaEngineFromInputStream(InputStream inputStream) throws IOException, ClassNotFoundException {
        try {
            copyInputStream(inputStream);
            inputStream.reset();
            this.enigmaEngine = new EnigmaMachineFromXML().getEnigmaEngineFromInputStream(xmlFile);
//            battlefield = new Battlefield(inputStream);


        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        battlefield = enigmaEngine.getBattlefield();
    }

    private void copyInputStream(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[50000];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1 ) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            xmlFile = new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    public void setUBoatName(String name) {
        this.name = name;
    }

    public String EncryptMessage(String message) {
        OriginalMessage = message;
//        dummyEngine = enigmaEngine.deepClone();
        try {
            encryptedMessage = enigmaEngine.processMessage(message);
        } catch (InvalidCharactersException e) {
            throw new RuntimeException(e);
        }
        enigmaEngine.reset();

        return encryptedMessage;
    }

    public void StartBattle() {
        status = "closed, in progress";
    }

    public void setRandomConfig() {
        enigmaEngine.randomSelectedComponents();
    }

    public void setBattlefield(Battlefield battlefield) {
        this.battlefield = battlefield;
    }

    public ContestDTO getContest() {
        return new ContestDTO(enigmaEngine.getBattlefieldDTO(),name, status);
    }

    public void addAlly(AllyEntity ally) {
        battlefield.addAlly(ally);
    }

    public List<AllyDTO> getAllies() {
        return battlefield.getAllies();
    }

    public InputStream getXmlFile() {
        return xmlFile;
    }

    public void sendCandidates(CandidateDTO[] candidates) {
        Collections.addAll(candidateDTOList, candidates);
        for(CandidateDTO candidate : candidateDTOList){
            if(candidate.getCandidateString().equals(OriginalMessage)){
                status = "open";
                stopContest();
            }
        }
    }

    public void checkIfAllReady() {
        if(isReady && battlefield.isAlliesReady()){
            startContest();
        }
    }

    private void startContest() {
        isContestStarted = true;
        status = "closed, in progress";
        battlefield.startContest(enigmaEngine, encryptedMessage);
    }

    public void setReady(Boolean isReady){
        this.isReady = isReady;
    }

    private void stopContest() {
        isContestStarted = false;

    }

    public ContestDataDTO getContestDataDTO() {
        return new ContestDataDTO(encryptedMessage,battlefield.getDifficulty(),isContestStarted);
    }

    public boolean isContestEnded() {
        return !isContestStarted;
    }
}
