package servlets;

import com.google.gson.Gson;
import enigmaEngine.EnigmaMachineFromXML;
import enigmaEngine.interfaces.EnigmaEngine;
import immutables.engine.EngineDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet(name = "servlets.FileUploadServlet", urlPatterns = {"/upload-file"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Part part = request.getPart("file");


        try {
            EnigmaEngine engine = new EnigmaMachineFromXML().getEnigmaEngineFromInputStream(part.getInputStream());
            EngineDTO engineDTO = engine.getEngineDTO();

            StringBuilder fileContent = new StringBuilder();

//            printPart(part, out);

            //to write the content of the file to an actual file in the system (will be created at c:\samplefile)
//        part.write("samplefile");

            //to write the content of the file to a string
//            fileContent.append(readFromInputStream(part.getInputStream()));

            Gson gson = new Gson();
            String json = gson.toJson(engineDTO);

            printFileContent(json, out);
        } catch (JAXBException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void printPart(Part part, PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Parameter Name: ").append(part.getName()).append("\n")
                .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
                .append("Size (of the file): ").append(part.getSize()).append("\n")
                .append("Part Headers:").append("\n");

        for (String header : part.getHeaderNames()) {
            sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
        }

        out.println(sb.toString());
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void printFileContent(String content, PrintWriter out) {
        out.println(content);
    }
}
