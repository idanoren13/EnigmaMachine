package servlets;

import Entities.UBoatEntity;
import com.google.gson.Gson;
import immutables.AllyDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.UBoatManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetSignedUpAlliesServlet", value = "/allies")
public class GetSignedUpAlliesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        UBoatEntity uBoatEntity = uBoatManager.getUBoat(request.getParameter("name"));
        List<AllyDTO> allyDTOListList = uBoatEntity.getAllies();

        Gson gson = new Gson();
        String json = gson.toJson(allyDTOListList);
        response.setContentType("application/json");
        response.getWriter().println(json);
    }
}
