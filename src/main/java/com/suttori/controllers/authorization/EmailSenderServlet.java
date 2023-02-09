package com.suttori.controllers.authorization;

import com.suttori.entity.User;
import com.suttori.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * the servlet handles the email activation
 * request and calls the appropriate method
 */
@WebServlet(name = "activate")
public class EmailSenderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        boolean isActivated = userService.activateEmail(req.getParameter("code"));
        if (isActivated) {
            req.setAttribute("message", "emailActivateSuccess");
        } else {
            req.setAttribute("error", userService.error);
        }
        resp.sendRedirect("/views/registration/activateSuccessful.jsp");
    }
}
