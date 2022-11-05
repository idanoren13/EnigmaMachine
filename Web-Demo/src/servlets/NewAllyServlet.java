package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AlliesManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "NewAllyServlet", urlPatterns = {"/ally"})
public class NewAllyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String allyName = request.getParameter("allyName");
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        alliesManager.addAlly(allyName);
    }
}
