package uBoatApp;

import static utils.Constants.FULL_SERVER_PATH;

public class UBoatServletsPaths {
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    public final static String CONTEXT_PATH = "/enigma_web_exploded";
//    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public static final String U_BOAT_LOGIN_SERVLET = FULL_SERVER_PATH + "/users/Login";
    public static final String U_BOAT_LOGOUT_SERVLET = FULL_SERVER_PATH + "/users/logout";
    public static final String FILE_UPLOADED_SERVLET = FULL_SERVER_PATH + "/fileUploaded";
    public static final String ALLIES_LIST_SERVLET = FULL_SERVER_PATH + "/AlliesList";
    public static final String AGENTS_LIST_SERVLET = FULL_SERVER_PATH + "/AgentsList";
    public static final String U_BOATS_LIST_SERVLET = FULL_SERVER_PATH + "/UBoatsList";
    public static final String GET_MACHINE_CONFIG_SERVLET = FULL_SERVER_PATH + "/machine/GetMachineConfig";
    public static final String SET_MACHINE_CONFIG_SERVLET = FULL_SERVER_PATH + "/machine/SetMachineConfig";
    public static final String PROCESS_WORD_SERVLET = FULL_SERVER_PATH + "/machine/ProcessWord";
    public static final String DICTIONARY_SERVLET = FULL_SERVER_PATH + "/Engine/GetDictionary";
    public static final String GET_USER_NAME_SERVLET = FULL_SERVER_PATH + "/users/getUserName";
}
