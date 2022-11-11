package utils;

public class Constants {
public static final int REFRESH_RATE = 1500;
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server_Exploded";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";

    public final static String UPLOAD_FILE = FULL_SERVER_PATH + "/upload-file";
    public final static String SET_MACHINE_CONFIG_AUTO = FULL_SERVER_PATH + "/set-machine-config";
    public final static String GET_WORDS_DICTIONARY = FULL_SERVER_PATH + "/get-words-dictionary";
    public final static String PROCESS_MESSAGE = FULL_SERVER_PATH + "/process-message";
    public final static String RESET_ENIGMA = FULL_SERVER_PATH + "/reset-enigma";
    public final static String GET_BATTLEFIELDS = FULL_SERVER_PATH + "/battlefields";
    public final static String JOIN_CONTEST = FULL_SERVER_PATH + "/join";
    public final static String ADD_ALLY = FULL_SERVER_PATH + "/ally";
    public final static String GET_SIGNED_UP_ALLIES = FULL_SERVER_PATH + "/uboat-allies";
    public final static String GET_ALLIES = FULL_SERVER_PATH + "/allies";
    public final static String GET_CONTEST_STATUS = FULL_SERVER_PATH + "/contest-status";
    public final static String GET_ENIGMA_ENGINE = FULL_SERVER_PATH + "/get-enigma-engine";
    public final static String GET_MISSIONS = FULL_SERVER_PATH + "/get-missions";
    public final static String SEND_CANDIDATES = FULL_SERVER_PATH + "/send-candidates";
    public final static String ADD_AGENT = FULL_SERVER_PATH + "/add-agent";
    public final static String GET_AGENTS = FULL_SERVER_PATH + "/agents";
    public final static String GET_CANDIDATES = FULL_SERVER_PATH + "/candidates";
    public final static String READY = FULL_SERVER_PATH + "/ready";

}
