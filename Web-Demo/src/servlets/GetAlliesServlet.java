package servlets;

import com.google.gson.Gson;
import immutables.AllyDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AlliesManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name="GetAlliesServlet", urlPatterns = {"/allies"})
public class GetAlliesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());

        List<AllyDTO> allyDTOList = alliesManager.getAllies();
        Gson gson = new Gson();
        String json = gson.toJson(allyDTOList);
        response.setContentType("application/json");
        response.getWriter().println(json);

    }
}
