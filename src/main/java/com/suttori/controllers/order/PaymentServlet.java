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

@WebServlet(name = "payment")
public class PaymentServlet extends HttpServlet {

    Logger logger = Logger.getLogger(PaymentServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("doGet");
        User user = (User) req.getSession().getAttribute("user");
        String orderId = req.getParameter("orderId");
        if (orderId != null) {
            PaymentService paymentService = new PaymentService();
            boolean successful = paymentService.payForOrder(user, Integer.parseInt(orderId));
            if (successful) {
                req.setAttribute("message", "orderPaymentSuccess");
            } else {
                req.setAttribute("error", paymentService.error);
            }
        }
        req.getRequestDispatcher("/views/profile/orders.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        logger.info("doPost");
        User user = (User) req.getSession().getAttribute("user");
        float sum = Integer.parseInt(req.getParameter("sum"));
        System.out.println(sum);
        PaymentService paymentService = new PaymentService();
        paymentService.topUpBalance(user, sum);
        req.setAttribute("message", "orderPaymentSuccess");
        req.getRequestDispatcher("/views/profile/orders.jsp").forward(req, resp);
    }
}