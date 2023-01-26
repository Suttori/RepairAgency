package com.suttori.controllers;

import com.suttori.entity.User;
import com.suttori.service.PaymentService;
import com.suttori.service.UserService;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "userList")
public class UserListServlet extends HttpServlet {
    Logger logger = Logger.getLogger(UserListServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("doGet");

        String email = req.getParameter("email");
        String sort = req.getParameter("sort");

        int usersOnPage = 6;
        int page = 1;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }

        int startPosition = page * usersOnPage - usersOnPage;

        UserService userService = new UserService();
        List<User> users = userService.getSortedUsers(email, sort, startPosition, usersOnPage);

        int nOfPages = (int) Math.ceil(userService.getNumberOfRows() * 1.0 / usersOnPage);
        req.setAttribute("users", users);
        req.setAttribute("page", page);
        req.setAttribute("nOfPages", nOfPages);
        req.getRequestDispatcher("/views/userList.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("doPost");
        System.out.println(req.getParameter("userId"));
        System.out.println(req.getParameter("sum"));
        int userId = Integer.parseInt(req.getParameter("userId"));
        int sum = Integer.parseInt(req.getParameter("sum"));
        PaymentService paymentService = new PaymentService();
        UserService userService = new UserService();
        if (!paymentService.topUpBalance(userService.getUserById(userId), sum)) {
            req.setAttribute("error", paymentService.error);
        }
        doGet(req, resp);
    }
}
