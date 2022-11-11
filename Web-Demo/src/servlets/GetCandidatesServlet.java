package servlets;

import Entities.AgentEntity;
import com.google.gson.Gson;
import immutables.CandidateDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AlliesManager;
import utils.ServletUtils;

import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetAgentData", urlPatterns = {"/candidates"})
public class GetCandidatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {

        String name = req.getParameter("allyName");
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        List<AgentEntity> agentList = alliesManager.getAlly(name).getAgents();

        List<CandidateDTO> candidateDTOList = new ArrayList<>();

        for (AgentEntity agent : agentList) {
            candidateDTOList.addAll(agent.getCandidates());
        }

        Gson gson = new Gson();
        String json = gson.toJson(candidateDTOList);
        resp.setContentType("application/json");
        resp.getWriter().println(json);
    }
}
