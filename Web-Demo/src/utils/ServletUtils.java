package utils;

import jakarta.servlet.ServletContext;
import managers.UBoatManager;
import managers.UserManager;

public class ServletUtils {
    public static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engineManager";
    public static final String UBOATS_MANAGER_ATTRIBUTE_NAME = "uboatsManager";
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    public static final String GAME_ATTRIBUTE_NAME = "game";

    private static final Object engineLock = new Object();
    private static final Object userManagerLock = new Object();
    private static final Object uBoatManagerLock = new Object();
    private static final Object alliesManagerLock = new Object();
    private static  final Object agentsManagerLock = new Object();

//    public static UBoatEntity getEnigmaEngine(ServletContext i_ServletContext) {
//        synchronized (engineLock) {
//            UBoatEntity engineManager = (UBoatEntity) i_ServletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME);
//            if (engineManager == null) {
//                engineManager = new UBoatEntity();
//                i_ServletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, engineManager);
//            }
//            return engineManager;
//        }
//    }

    public static UBoatManager getUBoatManager(ServletContext i_ServletContext) {
        synchronized (uBoatManagerLock) {
            UBoatManager uBoatManager = (UBoatManager) i_ServletContext.getAttribute(UBOATS_MANAGER_ATTRIBUTE_NAME);
            if (uBoatManager == null) {
                uBoatManager = new UBoatManager();
                i_ServletContext.setAttribute(UBOATS_MANAGER_ATTRIBUTE_NAME, uBoatManager);
            }
            return uBoatManager;
        }
    }

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }



}
