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
    PaginationService paginationService = new PaginationService();
    OrderService orderService = new OrderService();
    UserService userService = new UserService();

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


        Pagination pagination = paginationService.getLimitOffsetPage(req);
        Sort sort = paginationService.setSortParams(req);
        List<Order> orders = orderService.getSortedOrders(false, sort, pagination);

        int nOfPages = (int) Math.ceil(orderService.getNumberOfRows() * 1.0 / pagination.getOrdersOnPage());

        req.setAttribute("orders", orders);
        req.setAttribute("nOfPages", nOfPages);
        req.setAttribute("page", pagination.getPage());

        List<User> masters = userService.getAllMasters();
        req.setAttribute("masters", masters);

        req.getRequestDispatcher("/views/managerPage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int masterId = Integer.parseInt(req.getParameter("selectedMaster"));
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int price = Integer.parseInt(req.getParameter("price"));
        if (orderService.saveManagerAnswer(price, masterId, orderId)) {
            resp.sendRedirect(req.getContextPath() + "/views/managerPage");
        } else {
            req.setAttribute("error", orderService.error);
            doGet(req, resp);
        }
    }

    private boolean changeStatusManager(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getRequestURI().equals("/views/managerPage/cancel")) {
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.CANCELED);
            resp.sendRedirect(req.getContextPath() + "/views/managerPage");
            return true;
        } else if (req.getRequestURI().equals("/views/managerPage/isPendingPayment")) {
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.PENDING_PAYMENT);
            resp.sendRedirect(req.getContextPath() + "/views/managerPage");
            return true;
        } else if (req.getRequestURI().equals("/views/managerPage/isPaid")) {
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.PAID);
            resp.sendRedirect(req.getContextPath() + "/views/managerPage");
            return true;
        }
        return false;
    }
}
