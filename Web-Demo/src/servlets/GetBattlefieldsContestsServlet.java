package servlets;

import com.google.gson.Gson;
import immutables.ContestDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.UBoatManager;
import utils.ServletUtils;

import java.util.List;

@WebServlet(name = "GetBattlefieldsContestsServlet", urlPatterns = "/battlefields")
public class GetBattlefieldsContestsServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {

            UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());

            List<ContestDTO> contestDTOList = uBoatManager.getAllContests();

            Gson gson = new Gson();
            String json = gson.toJson(contestDTOList);
            resp.setContentType("application/json");
            resp.getWriter().println(json);

        }
}
