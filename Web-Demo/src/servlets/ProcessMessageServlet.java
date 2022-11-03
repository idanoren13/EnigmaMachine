package servlets;

import Entities.UBoatEntity;
import com.google.gson.Gson;
import enigmaEngine.exceptions.InvalidCharactersException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.UBoatManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "ProcessMessageServlet", urlPatterns = {"/process-message"})
public class ProcessMessageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("name");
        String message = request.getParameter("message");

        String name = request.getParameter("name");
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        UBoatEntity uBoat = uBoatManager.getUBoat(name);

        try {
            String processedMessage = uBoat.getEnigmaEngine().processMessage(message);
            response.setStatus(HttpServletResponse.SC_OK);
            Gson gson = new Gson();
            String json = gson.toJson(processedMessage);
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (InvalidCharactersException e) {
            e.printStackTrace();
        }
    }

}
