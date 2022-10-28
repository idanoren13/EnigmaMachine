package servlets;

import desktopApp.impl.Console;
import enigmaEngine.exceptions.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.xml.bind.JAXBException;

@WebServlet("/upload-file")
public class UploadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
        resp.setContentType("text/plain");
//        PrintWriter out = resp.getWriter();

        Console console = new Console();
        try {
            console.readMachineFromXMLFile(req.getPart("file").getInputStream());
        } catch (InvalidMachineException | JAXBException | InvalidRotorException | InvalidABCException |
                 UnknownSourceException | InvalidReflectorException e) {
            throw new RuntimeException(e);
        }
    }

}
