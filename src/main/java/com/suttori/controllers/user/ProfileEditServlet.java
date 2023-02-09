package com.suttori.controllers.user;

import com.suttori.entity.User;
import org.apache.log4j.Logger;
import com.suttori.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * the servlet accepts parameters and calls the appropriate service method to change the user's password or data
 */
@WebServlet(name = "edit")
public class ProfileEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/profile/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        UserService userService = new UserService();
        String uri = req.getRequestURI();
        if (uri.equals("/profile/edit/password")) {
            String oldPassword = req.getParameter("oldPassword");
            String newPassword = req.getParameter("newPassword");
            String newPasswordRepeat = req.getParameter("newPasswordRepeat");
            if (userService.changePassword(user, oldPassword, newPassword, newPasswordRepeat)) {
                resp.sendRedirect("/profile/orders");
                return;
            }
        }

        if (uri.equals("/profile/edit/data")) {
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String phone = req.getParameter("phoneNumber");
            if (userService.editProfile(user, firstName, lastName, phone)) {
                resp.sendRedirect(req.getContextPath() + "/profile/orders");
                return;
            }
        }
        req.setAttribute("error", userService.error);
        doGet(req, resp);
    }
}
