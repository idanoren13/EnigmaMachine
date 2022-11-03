package servlets;

import Entities.UBoatEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.UBoatManager;
import utils.ServletUtils;

@WebServlet(name = "ResetEnigmaServlet", urlPatterns = {"/reset-enigma"})
public class ResetEnigmaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        String name = request.getParameter("name");
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        UBoatEntity uBoat = uBoatManager.getUBoat(name);

        uBoat.getEnigmaEngine().reset();
    }
}
