package com.suttori.controllers.order;

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

@WebServlet(name = "orders")
public class OrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String masterId = req.getParameter("master");
        String status = req.getParameter("status");
        String sort = req.getParameter("sort");
        int OrdersOnPage = 6;
        int page = 1;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }
        //количесвто строк, которое необходимо пропустить
        int startPosition = page * OrdersOnPage - OrdersOnPage;
        OrderService orderService = new OrderService();

        List<Order> orders = orderService.getOrdersByUser(user.getId(), startPosition, OrdersOnPage);

        //количество страниц с записями
        int nOfPages = (int)Math.ceil(orderService.getNumberOfRows() * 1.0 / OrdersOnPage);
        req.setAttribute("orders", orders);
        req.setAttribute("nOfPages", nOfPages);
        req.setAttribute("page", page);
        UserService userService = new UserService();

        List<User> masters = userService.getAllMasters();

        req.setAttribute("masters", masters);
        RequestDispatcher view = req.getRequestDispatcher("/views/profile/orders.jsp");
        view.forward(req, resp);
    }
}
