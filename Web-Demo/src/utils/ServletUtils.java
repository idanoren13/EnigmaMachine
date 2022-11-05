package utils;

import jakarta.servlet.ServletContext;
import managers.AgentManager;
import managers.AlliesManager;
import managers.UBoatManager;
import managers.UserManager;

public class ServletUtils {
    public static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engineManager";
    public static final String UBOATS_MANAGER_ATTRIBUTE_NAME = "uboatsManager";
    public static final String ALLIES_MANAGER_ATTRIBUTE_NAME = "alliesManager";
    public static final String AGENT_MANAGER_ATTRIBUTE_NAME = "agentManager";


    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    public static final String GAME_ATTRIBUTE_NAME = "game";

    private static final Object engineLock = new Object();
    private static final Object userManagerLock = new Object();
    private static final Object uBoatManagerLock = new Object();
    private static final Object alliesManagerLock = new Object();
    private static  final Object agentsManagerLock = new Object();


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

    public static AlliesManager getAlliesManager(ServletContext i_ServletContext) {
        synchronized (alliesManagerLock) {
            AlliesManager alliesManager = (AlliesManager) i_ServletContext.getAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME);
            if (alliesManager == null) {
                alliesManager = new AlliesManager();
                i_ServletContext.setAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME, alliesManager);
            }
            return alliesManager;
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

    public static AgentManager getAgentManager(ServletContext i_ServletContext) {
        synchronized (agentsManagerLock) {
            AgentManager agentManager = (AgentManager) i_ServletContext.getAttribute(AGENT_MANAGER_ATTRIBUTE_NAME);
            if (agentManager == null) {
                agentManager = new AgentManager();
                i_ServletContext.setAttribute(AGENT_MANAGER_ATTRIBUTE_NAME, agentManager);
            }

            return agentManager;
        }
    }

}
