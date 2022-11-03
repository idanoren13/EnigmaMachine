package servlets;

import Entities.UBoatEntity;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.UBoatManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "SetMachineConfig", urlPatterns = {"/set-machine-config"})
public class SetMachineConfigAuto extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        UBoatEntity uBoat = uBoatManager.getUBoat(name);

        uBoat.setRandomConfig();
//        ServletUtils.getEnigmaEngine(getServletContext()).getEnigmaEngine().randomSelectedComponents();

        response.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new Gson();
        String json = gson.toJson(uBoat.getEnigmaEngine().getEngineDTO());
        response.getWriter().print(json);
        response.getWriter().flush();
    }
}
