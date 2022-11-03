package servlets;

import Entities.UBoatEntity;
import com.google.gson.Gson;
import immutables.engine.EngineDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import managers.UBoatManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "servlets.FileUploadServlet", urlPatterns = {"/upload-file"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        Part part = request.getPart("file");


        try {
            UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
            if(uBoatManager.uBoatIsNotExists(name)){
                UBoatEntity uBoat = new UBoatEntity();
                uBoat.setEnigmaEngineFromInputStream(part.getInputStream());
                uBoatManager.addUBoat(name, uBoat);

                EngineDTO engineDTO = uBoat.getEnigmaEngine().getEngineDTO();

                Gson gson = new Gson();
                String json = gson.toJson(engineDTO);
                printFileContent(json, out);
            }
            else{
                out.println("UBoat already exists!");
            }

//            ServletUtils.getEnigmaEngine(getServletContext()).setEnigmaEngineFromInputStream((part.getInputStream()));
////            EnigmaEngine engine = new EnigmaMachineFromXML().getEnigmaEngineFromInputStream(part.getInputStream());
//            EngineDTO engineDTO = ServletUtils.getEnigmaEngine(getServletContext()).getEnigmaEngine().getEngineDTO();

        } catch (ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void printFileContent(String content, PrintWriter out) {
        out.println(content);
    }
}
