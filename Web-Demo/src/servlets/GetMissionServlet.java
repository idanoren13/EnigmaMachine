package servlets;

import Entities.AllyEntity;
import com.google.gson.Gson;
import enigmaEngine.MachineCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AlliesManager;
import utils.ServletUtils;

import java.util.List;

@WebServlet(name = "GetMissionServlet", urlPatterns = {"/get-missions"})
public class GetMissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        AllyEntity ally = alliesManager.getAlly(request.getParameter("allyName"));
        int missionSize = request.getParameter("missionSize") == null ? 0 : Integer.parseInt(request.getParameter("missionSize"));
        List<MachineCode> missions = ally.getMissions(missionSize);

        response.setContentType("application/json");
        Gson gson = new Gson();
        String json = gson.toJson(missions);
        response.getWriter().write(json);
    }
}
