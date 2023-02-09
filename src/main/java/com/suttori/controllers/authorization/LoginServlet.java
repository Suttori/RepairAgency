package com.suttori.controllers.authorization;

import com.suttori.ProjectProperties;
import com.suttori.entity.User;
import org.apache.log4j.Logger;
import com.suttori.service.EmailSenderService;
import com.suttori.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * the servlet processes the input data and, based on it,
 * logs the user into the system, also writes cookies
 */
@WebServlet(name = "main")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/views/start-page.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        UserService userService = new UserService();

        boolean remember = req.getParameter("rememberMe") != null;
        if (userService.userLogin(email, password)) {
            User user = userService.getUserByEmail(email);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setAttribute("lang", user.getLocale());

            if (remember) {
                Cookie cookie = new Cookie("RepairAgencyCookie", String.valueOf(user.getId()));
                cookie.setMaxAge(60 * 60 * 24 * 30); //30 days
                resp.addCookie(cookie);
            }
            resp.sendRedirect("/profile/orders");
        } else {
            req.setAttribute("error", userService.error);
            doGet(req, resp);
        }
    }
}
