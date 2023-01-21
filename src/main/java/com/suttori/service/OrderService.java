package com.suttori.service;

import com.suttori.dao.OrderDAO;
import com.suttori.dao.UserDAO;
import com.suttori.entity.Order;
import com.suttori.entity.enams.OrderStatus;
import com.suttori.entity.enams.Role;
import org.apache.log4j.Logger;

import java.util.List;


public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();
    private UserDAO userDAO = new UserDAO();

    private final Logger logger = Logger.getLogger(OrderService.class);
    private EmailSenderService mailSender = new EmailSenderService();
    public String error;

    public boolean save(Order order) {
        if (order.getOrderName().length() < 1) {
            error = "problemShortError";
            return false;
        }
        if (order.getDescription().length() < 1) {
            error = "descriptionShortError";
            return false;
        }

        logger.info("User add new order");
        return orderDAO.insert(order);
    }

    public List<Order> getOrdersByUser(int userId, int startPosition, int total) {
        return orderDAO.findByUser("user_id", userId, startPosition, total);
    }

    public List<Order> getOrdersAll(int startPosition, int total) {
        return orderDAO.findAll(startPosition, total);
    }

    public List<Order> getSortedOrders(String masterId, String status, String sort, int startPosition, int totalOrders) {
        StringBuilder stringBuilder = new StringBuilder();

        boolean needSortMaster = false;
        boolean needSortStatus = false;

        //Все мастера, все статусы, нет сортировки
        if ((masterId == null || masterId.equals("-1")) && (status == null || status.equals("ALL")) && (sort == null || sort.equals("none"))) {
            return orderDAO.findAll(startPosition, totalOrders);
        }

        if (masterId != null && !masterId.equals("-1")) {
            stringBuilder.append("WHERE craftsman_id = ? ");
            needSortMaster = true;
        }

        if (!needSortMaster && status != null && !status.equals("ALL")) {
            stringBuilder.append("WHERE status = ? ");
        }

        if (needSortMaster && status != null && !status.equals("ALL")) {
            stringBuilder.append("AND status = ? ");
            needSortStatus = true;
        }

        if (sort != null && !sort.equals("none")) {
            stringBuilder.append("ORDER BY ").append(sort);
        }

        String findBy = String.valueOf(stringBuilder);

        if (needSortMaster && needSortStatus) {
            return orderDAO.findBy(findBy, Integer.parseInt(masterId), status, startPosition, totalOrders);
        } else if (needSortMaster){
            return orderDAO.findByMaster(findBy, Integer.parseInt(masterId), startPosition, totalOrders);
        } else return orderDAO.findByStatus(findBy, status, startPosition, totalOrders);
    }

    //забираем количество строк
    public int getNumberOfRows() {
        return orderDAO.totalRows;
    }
}
