package servlets;

import Entities.AllyEntity;
import Entities.UBoatEntity;
import com.google.gson.Gson;
import immutables.CandidateDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AlliesManager;
import managers.UBoatManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "SendCandidatesServlet", value = "/send-candidates")
public class SendCandidatesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CandidateDTO[] candidates = new Gson().fromJson(request.getReader(), CandidateDTO[].class);
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());

        AllyEntity ally = alliesManager.getAlly(request.getParameter("allyName"));
        ally.setCandidates(candidates);
        UBoatEntity uBoat = uBoatManager.getUBoat(request.getParameter("uBoatName"));
        uBoat.sendCandidates(candidates);


    }
}
