package com.suttori.service;

import com.suttori.dao.CommentDAO;
import com.suttori.dao.OrderDAO;
import com.suttori.dao.UserDAO;
import com.suttori.entity.Comment;
import com.suttori.entity.Order;
import com.suttori.entity.enams.OrderStatus;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();
    private UserDAO userDAO = new UserDAO();
    private CommentDAO commentDAO = new CommentDAO();

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

//    public List<Order> getOrdersAll(int startPosition, int total) {
//        return orderDAO.findAll(startPosition, total);
//    }

    public boolean saveManagerAnswer(int price, int masterId, int orderId) {
        if (price < 5) {
            logger.info("Manager set low price");
            error = "priceLowError";
            return false;
        }
        orderDAO.setPrice(orderId, price);
        orderDAO.setMaster(orderId, masterId);
        setOrderStatus(orderId, OrderStatus.PENDING_PAYMENT);
        logger.info("Manager check order");
        return true;
    }

    public void setOrderStatus(int orderId, OrderStatus status) {
        orderDAO.setStatus(orderId, status);

//        Order order = orderDAO.findById(orderId);
//        User user = orderDAO.findById(order.getUserId());
//        mailSender.sendOrderStatusUpdate(user, order);
//        log.info("Order " + orderId + " get status: " + status.name());
    }



    public Order getById(int id) {
        return orderDAO.findById("id", id);
    }


    public List<Order> getSortedOrders(boolean sortForUser, int userId, String masterId, String status, String sort, int startPosition, int totalOrders) {
        Map<String, Object> filterParams = new HashMap<>();
        //Map<String, SortingParams> sortingParams = null;
        String sortingParams = null;
        Map<String, Integer> limitingParams = new LinkedHashMap<>();

        if (masterId != null && !masterId.equals("-1")) {
            filterParams.put("craftsman_id", Integer.parseInt(masterId));
        }

        if (status != null && !status.equals("ALL")) {
            filterParams.put("status", status);
        }

        if (sortForUser) {
            filterParams.put("user_id", userId);
        }

        if (sort != null && !sort.equals("none")) {
            sortingParams = sort;
        }

        limitingParams.put("LIMIT" , totalOrders);
        limitingParams.put("OFFSET" , startPosition);

        return orderDAO.findBy(filterParams, sortingParams, limitingParams);
    }

    public boolean saveComment(int orderId, Comment comment) {
//        if (comment.getRate() <= 0) {
//            error = "nullRateError";
//            return false;
//        }
        if (comment.getDescription().length() < 2) {
            error = "descriptionShortError";
            return false;
        }
        commentDAO.insert(comment);
        orderDAO.setCommentId(orderId, comment);
        return true;
    }


    //забираем количество строк
    public int getNumberOfRows() {
        return orderDAO.totalRows;
    }
}
