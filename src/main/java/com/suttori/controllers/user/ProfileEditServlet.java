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

@WebServlet(name = "edit")
public class ProfileEditServlet extends HttpServlet {

    Logger log = Logger.getLogger(ProfileEditServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doPost working");
        User user = (User) req.getSession().getAttribute("user");
        UserService userService = new UserService();
        String uri = req.getRequestURI();
        if (uri.equals("/profile/edit/password")) {
            String oldPassword = req.getParameter("oldPassword");
            String newPassword = req.getParameter("newPassword");
            String newPasswordRepeat = req.getParameter("newPasswordRepeat");
            if (userService.changePassword(user, oldPassword, newPassword, newPasswordRepeat)) {
                resp.sendRedirect("/profile");
            }
        }

        if (uri.equals("/profile/edit/data")) {
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String email = req.getParameter("email");
            String phone = req.getParameter("phoneNumber");
            if (userService.editProfile(user, firstName, lastName, email, phone)) {
                resp.sendRedirect(req.getContextPath() + "/profile");
            }
        }
    }
}
