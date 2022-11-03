package utils;

public class Constants {

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
    public final static String JOIN_BATTLE = FULL_SERVER_PATH + "/join";

    public final static String GET_ALLIES = FULL_SERVER_PATH + "/allies";

    public final static String GET_ENTITIES = FULL_SERVER_PATH + "/entities";

    public final static String UNJOIN_BATTLE = FULL_SERVER_PATH + "/unjoin";
    public final static String ADD_AGENT = FULL_SERVER_PATH + "/agent";
    public final static String GET_AGENTS = FULL_SERVER_PATH + "/agents";
    public final static String READY = FULL_SERVER_PATH + "/ready";

}
