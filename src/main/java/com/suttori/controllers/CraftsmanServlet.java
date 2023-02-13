package com.suttori.controllers;

import com.suttori.entity.Order;
import com.suttori.entity.Pagination;
import com.suttori.entity.Sort;
import com.suttori.entity.User;
import com.suttori.entity.enams.OrderStatus;
import com.suttori.service.OrderService;
import com.suttori.service.PaginationService;

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
@WebServlet(name = "craftsman")
public class CraftsmanServlet extends HttpServlet {

    PaginationService paginationService = new PaginationService();
    OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User craftsman = (User) req.getSession().getAttribute("user");
        if (!craftsman.isCraftsman()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        if (changeStatusCraftsman(req, resp)) {
            return;
        }

        Pagination pagination = paginationService.getLimitOffsetPage(req);
        Sort sort = paginationService.setSortParams(req);

        List<Order> orders = orderService.getSortedOrders(false, sort, pagination);
        int nOfPages = (int) Math.ceil(orderService.getNumberOfRows() * 1.0 / 6);

        req.setAttribute("orders", orders);
        req.setAttribute("nOfPages", nOfPages);
        req.setAttribute("page", pagination.getPage());

        req.getRequestDispatcher("/views/craftsman.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        if (req.getRequestURI().equals("/craftsman/complete")) {
            orderService.setOrderStatus(orderId, OrderStatus.COMPLETED);
        } else if (req.getRequestURI().equals("/views/craftsman/execute")) {
            orderService.setOrderStatus(orderId, OrderStatus.IN_PROGRESS);
        }
        resp.sendRedirect(req.getContextPath() + "/views/craftsman");
    }

    protected boolean changeStatusCraftsman(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getRequestURI().equals("/views/craftsman/execute")) {
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.IN_PROGRESS);
            resp.sendRedirect(req.getContextPath() + "/views/craftsman");
            return true;
        } else if (req.getRequestURI().equals("/views/craftsman/complete")) {
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.COMPLETED);
            resp.sendRedirect(req.getContextPath() + "/views/craftsman");
            return true;
        }
        return false;
    }
}
