package utils;

public class Constants {

    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/enigma_web_exploded";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String UPLOAD_FILE = FULL_SERVER_PATH + "/upload-file";
    public final static String JOIN_BATTLE = FULL_SERVER_PATH + "/join";

    public final static String GET_ALLIES = FULL_SERVER_PATH + "/allies";

    public final static String GET_ENTITIES = FULL_SERVER_PATH + "/entities";

    public final static String UNJOIN_BATTLE = FULL_SERVER_PATH + "/unjoin";
    public final static String ADD_AGENT = FULL_SERVER_PATH + "/agent";
    public final static String GET_AGENTS = FULL_SERVER_PATH + "/agents";
    public final static String READY = FULL_SERVER_PATH + "/ready";

}
