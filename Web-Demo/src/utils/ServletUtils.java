package utils;

import enigmaEngine.EnigmaEngineManager;
import jakarta.servlet.ServletContext;

public class ServletUtils {
    public static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engineManager";
    public static final String GAME_MANAGER_ATTRIBUTE_NAME = "gameManager";
    public static final String GAME_ATTRIBUTE_NAME = "game";

    private static final Object engineLock = new Object();
    private static final Object userManagerLock = new Object();
    private static final Object uBoatManagerLock = new Object();
    private static final Object alliesManagerLock = new Object();
    private static  final Object agentsManagerLock = new Object();

    public static EnigmaEngineManager getEnigmaEngine(ServletContext i_ServletContext) {
        synchronized (engineLock) {
            EnigmaEngineManager engineManager = (EnigmaEngineManager) i_ServletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME);
            if (engineManager == null) {
                engineManager = new EnigmaEngineManager();
                i_ServletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, engineManager);
            }
            return engineManager;
        }
    }


}
