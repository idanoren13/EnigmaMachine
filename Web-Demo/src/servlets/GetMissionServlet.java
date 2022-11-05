package servlets;

import Entities.AllyEntity;
import enigmaEngine.MachineCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AlliesManager;
import utils.ServletUtils;

public class GetMissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        AllyEntity ally = alliesManager.getAlly(request.getParameter("allyName"));
        int missionSize = request.getParameter("missionSize") == null ? 0 : Integer.parseInt(request.getParameter("missionSize"));
        MachineCode mission = ally.getMission(missionSize);
    }
}
