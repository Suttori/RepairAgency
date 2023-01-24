package com.suttori.controllers;

import com.suttori.entity.Order;
import com.suttori.entity.User;
import com.suttori.entity.enams.OrderStatus;
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
        if (changeStatus(req, resp)) {
            return;
        }

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

        List<Order> orders = orderService.getSortedOrders(false, 0, masterId, status, sort, startPosition, ordersOnPage);
        //количество страниц с записями
        int nOfPages = (int) Math.ceil(orderService.getNumberOfRows() * 1.0 / ordersOnPage);

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
        int masterId = Integer.parseInt(req.getParameter("selectedMaster"));
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int price = Integer.parseInt(req.getParameter("price"));

        OrderService orderService = new OrderService();
        if (orderService.saveManagerAnswer(price, masterId, orderId)) {
            resp.sendRedirect(req.getContextPath() + "/views/managerPage");
        } else {
            req.setAttribute("error", orderService.error);
            doGet(req, resp);
        }
    }

    private boolean changeStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getRequestURI().equals("/views/managerPage/cancel")) {
            OrderService orderService = new OrderService();
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.CANCELED);
            resp.sendRedirect(req.getContextPath() + "/views/managerPage");
            return true;
        } else if (req.getRequestURI().equals("/views/managerPage/isPendingPayment")) {
            OrderService orderService = new OrderService();
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.PENDING_PAYMENT);
            resp.sendRedirect(req.getContextPath() + "/views/managerPage");
            return true;
        } else if (req.getRequestURI().equals("/views/managerPage/isPaid")) {
            OrderService orderService = new OrderService();
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.PAID);
            resp.sendRedirect(req.getContextPath() + "/views/managerPage");
            return true;
        }
        return false;
    }
}
