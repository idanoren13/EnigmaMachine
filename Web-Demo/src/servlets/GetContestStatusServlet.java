package servlets;

import Entities.AllyEntity;
import Entities.UBoatEntity;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AlliesManager;
import managers.UBoatManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "GetContestStatusServlet", value = "/contest-status")
public class GetContestStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("allyName");
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        AllyEntity ally = alliesManager.getAlly(name);

        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        UBoatEntity uBoat = uBoatManager.getUBoat(ally.getUBoatName());

        Gson gson = new Gson();
        String json = gson.toJson(uBoat.getCOntestDataDTO());
        response.getWriter().print(json);
        response.getWriter().flush();
    }
}
