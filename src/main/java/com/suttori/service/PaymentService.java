package com.suttori.service;

import com.suttori.dao.UserDAO;
import com.suttori.entity.Order;
import com.suttori.entity.User;
import com.suttori.entity.enams.OrderStatus;
import org.apache.log4j.Logger;

public class PaymentService {

    UserDAO userDAO = new UserDAO();

    Logger logger = Logger.getLogger(PaymentService.class);
    public String error;

    public boolean topUpBalance(User user, float sum) {
        if (sum <= 0) {
            error = "negativeSumError";
            logger.info("Negative sum");
            return false;
        }
        user.setBalance(user.getBalance() + sum);
        userDAO.setBalance(user.getId(), user.getBalance());
        logger.info("Top up user balance");
        return true;
    }

    public boolean payForOrder(User user, int orderId) {
        OrderService orderService = new OrderService();
        Order order = orderService.getById(orderId);

        if (user.getId() != order.getUserId()) {
            error = "notUsersOrderError";
            logger.info("User can't pay for not him order");
            return false;
        }

        if (order.getStatus().equals(OrderStatus.PAID)) {
            error = "alreadyPayError";
            logger.info("User already pay for this order");
            return false;
        }

        float sum = user.getBalance() - order.getPrice();

        if (sum < 0) {
            error = "notEnoughMoneyError";
            logger.info("User have not enough money");
            return false;
        }


        orderService.setOrderStatus(orderId, OrderStatus.PAID);

        user.setBalance(sum);
        userDAO.setBalance(user.getId(), user.getBalance());

        logger.info("User paid for order");
        return true;

    }


}
