package com.suttori.controllers.user;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "profile")
public class ProfileServlet extends HttpServlet {
    private final Logger logger = Logger.getLogger(ProfileServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("doGet working");
        String uri = req.getRequestURI();
        if (uri.equals("/profile/editData")) {
            RequestDispatcher view = req.getRequestDispatcher("/views/profile/edit.jsp");
            view.forward(req, resp);
            return;
        }
        if (uri.equals("/profile/orders")) {
            RequestDispatcher view = req.getRequestDispatcher("/views/profile/orders.jsp");
            view.forward(req, resp);
            return;
        }
        if (uri.equals("/profile/addRequest")) {
            RequestDispatcher view = req.getRequestDispatcher("/views/profile/addOrder.jsp");
            view.forward(req, resp);
            return;
        }
        RequestDispatcher view = req.getRequestDispatcher("/views/profile.jsp");
        view.forward(req, resp);
    }
}
