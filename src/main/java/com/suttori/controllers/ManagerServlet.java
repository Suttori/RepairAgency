package com.suttori.controllers;

import com.suttori.entity.Order;
import com.suttori.entity.User;
import com.suttori.service.OrderService;
import com.suttori.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "managerPage")
public class ManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String masterId = req.getParameter("master");
        String status = req.getParameter("status");
        String sort = req.getParameter("sort");

        int ordersOnPage = 6;
        int page = 1;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }


        //количесвто строк, которое необходимо пропустить
        int startPosition = page * ordersOnPage - ordersOnPage;
        OrderService orderService = new OrderService();
       // List<Order> orders = orderService.getOrdersAll(startPosition, ordersOnPage);
        List<Order> orders = orderService.getSortedOrders(masterId, status, sort, startPosition, ordersOnPage);
        //количество страниц с записями
        int nOfPages = (int)Math.ceil(orderService.getNumberOfRows() * 1.0 / ordersOnPage);

        req.setAttribute("orders", orders);
        req.setAttribute("nOfPages", nOfPages);
        req.setAttribute("page", page);

        UserService userService = new UserService();
        List<User> masters = userService.getAllMasters();
        req.setAttribute("masters", masters);


        RequestDispatcher view = req.getRequestDispatcher("/views/managerPage.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
