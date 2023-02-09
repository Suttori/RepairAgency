package com.suttori.controllers;

import com.suttori.entity.Pagination;
import com.suttori.entity.Sort;
import com.suttori.entity.User;
import com.suttori.service.OrderService;
import com.suttori.service.PaginationService;
import com.suttori.service.PaymentService;
import com.suttori.service.UserService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * the doGet method of the servlet generates a page with sorted or not ordered users depending on the received parameters,
 * doPost calls the appropriate methods to replenish the user's balance
 */
@WebServlet(name = "userList")
public class UserListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User manager = (User) req.getSession().getAttribute("user");
        if (!manager.isManager()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        PaginationService paginationService = new PaginationService();
        Pagination pagination = paginationService.getLimitOffsetPage(req);
        Sort sort = paginationService.setSortParams(req);

        UserService userService = new UserService();

        List<User> users = userService.getSortedUsers(sort, pagination);

        int nOfPages = (int) Math.ceil(userService.getNumberOfRows() * 1.0 / pagination.getOrdersOnPage());
        req.setAttribute("users", users);
        req.setAttribute("page", pagination.getPage());
        req.setAttribute("nOfPages", nOfPages);

        req.getRequestDispatcher("/views/userList.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        int sum = Integer.parseInt(req.getParameter("sum"));
        PaymentService paymentService = new PaymentService();
        UserService userService = new UserService();
        if (!paymentService.topUpBalance(userService.getUserById(userId), sum)) {
            req.setAttribute("error", paymentService.error);
        }
        resp.sendRedirect("/userList");
    }
}
