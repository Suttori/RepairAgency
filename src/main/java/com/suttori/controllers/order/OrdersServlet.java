package com.suttori.controllers.order;

import com.suttori.entity.*;
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
 * the doPost method is responsible for saving a comment to an already completed order
 */
@WebServlet(name = "orders")
public class OrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/profile/orders/cancel")) {
            OrderService orderService = new OrderService();
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.CANCELED);
            resp.sendRedirect(req.getContextPath() + "/profile/orders");
            return;
        }

        PaginationService paginationService = new PaginationService();
        Pagination pagination = paginationService.getLimitOffsetPage(req);
        Sort sort = paginationService.setSortParams(req);
        OrderService orderService = new OrderService();

        List<Order> orders = orderService.getSortedOrders(true, sort, pagination);

        if (orders.isEmpty()) {
            req.setAttribute("message", "orders.noOrders");
        }

        int nOfPages = (int)Math.ceil(orderService.getNumberOfRows() * 1.0 / pagination.getOrdersOnPage());

        req.setAttribute("orders", orders);
        req.setAttribute("nOfPages", nOfPages);
        req.setAttribute("page", pagination.getPage());
        UserService userService = new UserService();

        List<User> masters = userService.getUserMasters(sort.getUser().getId());

        req.setAttribute("masters", masters);
        RequestDispatcher view = req.getRequestDispatcher("/views/profile/orders.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int craftsmanId = Integer.parseInt(req.getParameter("craftsmanId"));
        int userId = Integer.parseInt(req.getParameter("userId"));
        String description = req.getParameter("description");

        Comment comment = new Comment();
        comment.setCraftsmanId(craftsmanId);
        comment.setUserId(userId);
        comment.setDescription(description);

        OrderService orderService = new OrderService();
        if (orderService.saveComment(orderId, comment)) {
            resp.sendRedirect("/profile/orders");
        }else{
            req.setAttribute("error", orderService.error);
            doGet(req, resp);
        }
    }
}
