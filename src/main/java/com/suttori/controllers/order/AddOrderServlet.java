package com.suttori.controllers.order;

import com.suttori.entity.Order;
import com.suttori.entity.User;
import com.suttori.service.OrderService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * the servlet processes the input data and passes it to the service
 * for further adding the order
 */
@WebServlet(name = "order")
public class AddOrderServlet extends HttpServlet {
    OrderService orderService = new OrderService();
    Order order = new Order();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/profile/addOrder.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String nameOrder = req.getParameter("nameOrder");
        String description = req.getParameter("description");
        order.setOrderName(nameOrder);
        order.setDescription(description);
        order.setUserId(user.getId());
        if(orderService.save(order)){
            resp.sendRedirect(req.getContextPath() + "/profile/orders");
        }else{
            req.setAttribute("error", orderService.error);
            doGet(req, resp);
        }
    }
}
