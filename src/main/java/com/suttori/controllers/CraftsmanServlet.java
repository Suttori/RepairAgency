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

@WebServlet(name = "craftsman")
public class CraftsmanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (changeStatus(req, resp)) {
            return;
        }


        User master = (User) req.getSession().getAttribute("user");
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

        List<Order> orders = orderService.getSortedOrders(false, 0, String.valueOf(master.getId()), status, sort, startPosition, ordersOnPage);
        //количество страниц с записями
        int nOfPages = (int) Math.ceil(orderService.getNumberOfRows() * 1.0 / ordersOnPage);

        req.setAttribute("orders", orders);
        req.setAttribute("nOfPages", nOfPages);
        req.setAttribute("page", page);


        req.getRequestDispatcher("/views/craftsman.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("orderId" + "----"));
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        OrderService orderService = new OrderService();
        if (req.getRequestURI().equals("/craftsman/complete")) {
            orderService.setOrderStatus(orderId, OrderStatus.COMPLETED);
        } else if(req.getRequestURI().equals("/views/craftsman/execute")) {
            orderService.setOrderStatus(orderId, OrderStatus.IN_PROGRESS);
        }
        resp.sendRedirect(req.getContextPath() + "/views/craftsman");
    }



    private boolean changeStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getRequestURI().equals("/views/craftsman/execute")) {
            OrderService orderService = new OrderService();
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.IN_PROGRESS);
            resp.sendRedirect(req.getContextPath() + "/views/craftsman");
            return true;
        } else if (req.getRequestURI().equals("/views/craftsman/complete")) {
            OrderService orderService = new OrderService();
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.COMPLETED);
            resp.sendRedirect(req.getContextPath() + "/views/craftsman");
            return true;
        }
        return false;
    }
}
