package servlets;

import Entities.AllyEntity;
import Entities.UBoatEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AlliesManager;
import managers.UBoatManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "JoinContestServlet", urlPatterns = {"/join"})
public class JoinContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String uBoatName = request.getParameter("uboatName");
        String allyName = request.getParameter("allyName");

        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());

        AllyEntity ally = alliesManager.getAlly(allyName);
        UBoatEntity uBoat = uBoatManager.getUBoat(uBoatName);

        ally.setUBoatName(uBoatName);
        ally.setUBoatEntity(uBoat);
        uBoat.addAlly(ally);
    }
}
