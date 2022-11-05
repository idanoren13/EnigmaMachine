package servlets;

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

@WebServlet(name = "ReadyServlet", urlPatterns = {"/ready"})
public class ReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String entitiy = request.getParameter("entity");
        String name = request.getParameter("name");
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());

        switch (entitiy) {
            case "uboat":
                UBoatEntity uBoatEntity = uBoatManager.getUBoat(name);
                uBoatEntity.setReady(true);
                uBoatEntity.checkIfAllReady();
                break;
            case "ally":
                AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
                alliesManager.getAlly(name).setReady(true);
                uBoatManager.getUBoat(request.getParameter("uboatName")).checkIfAllReady();
                break;
        }
    }
}
