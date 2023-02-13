package com.suttori.controllers.order;

import com.suttori.entity.User;
import com.suttori.service.PaymentService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * the doGet method takes parameters and calls the corresponding service method to pay for the order,
 * the doPost method takes parameters and calls the corresponding service method to replenish the user's balance
 */
@WebServlet(name = "payment")
public class PaymentServlet extends HttpServlet {

    Logger logger = Logger.getLogger(PaymentServlet.class);
    PaymentService paymentService = new PaymentService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String orderId = req.getParameter("orderId");
        if (orderId != null) {
            boolean successful = paymentService.payForOrder(user, Integer.parseInt(orderId));
            if (successful) {
                req.setAttribute("message", "orderPaymentSuccess");
            } else {
                req.setAttribute("error", paymentService.error);
            }
        }
        req.getRequestDispatcher("/profile/orders").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getParameter("sum").equals("")) {
            logger.info("Incorrect value");
            resp.sendRedirect("/profile/orders");
            return;
        }
        float sum = Integer.parseInt(req.getParameter("sum"));
        User user = (User) req.getSession().getAttribute("user");
        paymentService.topUpBalance(user, sum);
        resp.sendRedirect("/profile/orders");
    }
}