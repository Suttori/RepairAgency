package com.suttori.controllers.authorization;

import com.suttori.entity.User;
import org.apache.log4j.Logger;
import com.suttori.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Enumeration;

/**
 * servlet records session and retrieves cookies
 */
@WebServlet(name = "/")
public class MainServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(MainServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            int flag = isUserRemembered(req);
            if (flag != -1) {
                HttpSession session = req.getSession();
                UserService userService = new UserService();
                User user = userService.getUserById(flag); // cookie == user id
                session.setAttribute("user", user);
                log.info("User logged by remember me");
                resp.sendRedirect("/profile/orders");
                return;
            }
        }
        req.getRequestDispatcher("/views/start-page.jsp").forward(req, resp);
    }

    private int isUserRemembered(HttpServletRequest req) {
        if (req.getCookies() == null) {
            return -1;
        }
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("RepairAgencyCookie")) {
                return Integer.parseInt(cookie.getValue());
            }
        }
        return -1;
    }
}
