package com.suttori.controllers;

import com.suttori.entity.Order;
import com.suttori.entity.Pagination;
import com.suttori.entity.Sort;
import com.suttori.entity.User;
import com.suttori.entity.enams.OrderStatus;
import com.suttori.service.OrderService;
import com.suttori.service.PaginationService;
import com.suttori.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * the doGet method of the servlet generates a page with sorted or not ordered orders depending on the received parameters,
 * doPost calls the appropriate methods to change the order status
 */
@WebServlet(name = "managerPage")
public class ManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User manager = (User) req.getSession().getAttribute("user");
        if (!manager.isManager()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        if (changeStatusManager(req, resp)) {
            return;
        }

        PaginationService paginationService = new PaginationService();
        Pagination pagination = paginationService.getLimitOffsetPage(req);
        Sort sort = paginationService.setSortParams(req);
        OrderService orderService = new OrderService();

        List<Order> orders = orderService.getSortedOrders(false, sort, pagination);

        int nOfPages = (int) Math.ceil(orderService.getNumberOfRows() * 1.0 / pagination.getOrdersOnPage());

        req.setAttribute("orders", orders);
        req.setAttribute("nOfPages", nOfPages);
        req.setAttribute("page", pagination.getPage());

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

    private boolean changeStatusManager(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
