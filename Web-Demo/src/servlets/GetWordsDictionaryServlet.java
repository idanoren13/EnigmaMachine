package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "GetWordsDictionaryServlet", urlPatterns = {"/get-words-dictionary"})
public class GetWordsDictionaryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(ServletUtils.getUBoatManager(getServletContext())
                .getUBoat(request.getParameter("name"))
                .getEnigmaEngine().getWordsDictionary().getWords());
        response.setContentType("application/json");
        response.getWriter().println(json);
    }
}
