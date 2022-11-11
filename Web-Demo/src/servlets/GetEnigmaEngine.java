package servlets;

import Entities.AllyEntity;
import Entities.UBoatEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AlliesManager;
import managers.UBoatManager;
import utils.ServletUtils;

import java.io.InputStream;

@WebServlet(name = "GetEnigmaEngine", urlPatterns = {"/get-enigma-engine"})
public class GetEnigmaEngine extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
        synchronized (this) {
            String name = req.getParameter("allyName");
            AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
            AllyEntity ally = alliesManager.getAlly(name);

            UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
            UBoatEntity uBoat = uBoatManager.getUBoat(ally.getUBoatName());

//            EnginePartsDTO enginePartsDTO = uBoat.getEnigmaEngine().getEnginePartsDTO();
//            Gson gson = new Gson();
//            String json = gson.toJson(enginePartsDTO);
//            resp.setContentType("application/json");
//            resp.getWriter().println(json);

            resp.setContentType("text/plain");
            InputStream xmlFile = uBoat.getXmlFile();
            xmlFile.reset();

            ServletOutputStream outputStream = resp.getOutputStream();
            byte[] buffer = new byte[50240];
            int bytesRead = xmlFile.read(buffer);
            while (bytesRead >= 0) {
                outputStream.write(buffer, 0, bytesRead);
                bytesRead = xmlFile.read(buffer);
            }

            xmlFile.reset();
        }
    }
}
