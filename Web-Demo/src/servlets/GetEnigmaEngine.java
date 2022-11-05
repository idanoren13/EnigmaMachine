package servlets;

import Entities.UBoatEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.UBoatManager;
import utils.ServletUtils;

@WebServlet(name = "servlets.GetEnigmaEngine", urlPatterns = {"/get-enigma-engine"})
public class GetEnigmaEngine extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        UBoatEntity uBoat = uBoatManager.getUBoat(req.getParameter("uboatName"));
        resp.setContentType("application/xml");
        resp.getWriter().println(uBoat.getXmlFile());
    }
}
