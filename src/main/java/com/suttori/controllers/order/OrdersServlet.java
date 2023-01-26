package com.suttori.controllers.order;

import com.suttori.entity.Comment;
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


        User user = (User) req.getSession().getAttribute("user");
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

        List<Order> orders = orderService.getSortedOrders(true, user.getId(), masterId, status, sort, startPosition, ordersOnPage);

        //количество страниц с записями
        int nOfPages = (int)Math.ceil(orderService.getNumberOfRows() * 1.0 / ordersOnPage);
        req.setAttribute("orders", orders);
        req.setAttribute("nOfPages", nOfPages);
        req.setAttribute("page", page);
        UserService userService = new UserService();

        List<User> masters = userService.getAllMasters();

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
//        comment.setRate(rate);
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
