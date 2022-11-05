package servlets;

import Entities.AgentEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AgentManager;
import managers.AlliesManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AddAgentServlet", urlPatterns = "/add-agent")
public class AddAgentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String agentName = request.getParameter("agentName");
        String allyName = request.getParameter("allyName");
        AgentManager agentManager = ServletUtils.getAgentManager(getServletContext());
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());

        AgentEntity agent = new AgentEntity(agentName, allyName);

        agentManager.addAgent(agent);
        alliesManager.addAgentToAlly(allyName, agent);
    }
}
