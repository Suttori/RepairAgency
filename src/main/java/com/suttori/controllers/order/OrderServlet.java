package com.suttori.controllers.order;

import com.suttori.entity.Order;
import com.suttori.entity.User;
import com.suttori.service.OrderService;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "order")
public class OrderServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(OrderServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("doGet");
        RequestDispatcher view = req.getRequestDispatcher("/views/profile/addOrder.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("doPost");
        User user = (User) req.getSession().getAttribute("user");
        String nameOrder = req.getParameter("nameOrder");
        String description = req.getParameter("description");

        Order order = new Order();
        order.setOrderName(nameOrder);
        order.setDescription(description);
        order.setUserId(user.getId());

        OrderService orderService = new OrderService();
        if(orderService.save(order)){
            resp.sendRedirect(req.getContextPath() + "/profile");
        }else{
            req.setAttribute("error", orderService.error);
            doGet(req, resp);
        }
    }
}
