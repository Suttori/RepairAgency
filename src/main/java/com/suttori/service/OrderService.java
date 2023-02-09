package com.suttori.service;

import com.suttori.dao.CommentDAO;
import com.suttori.dao.OrderDAO;
import com.suttori.dao.UserDAO;
import com.suttori.entity.Comment;
import com.suttori.entity.Order;
import com.suttori.entity.Pagination;
import com.suttori.entity.Sort;
import com.suttori.entity.enams.OrderStatus;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();
    private CommentDAO commentDAO = new CommentDAO();

    private final Logger logger = Logger.getLogger(OrderService.class);
    private EmailSenderService mailSender = new EmailSenderService();
    public String error;

    public boolean save(Order order) {
        if (order.getOrderName().length() < 6 || order.getOrderName().length() > 26) {
            logger.info("problemNameError");
            error = "problemNameError";
            return false;
        }
        if (order.getDescription().length() < 11 || order.getDescription().length() > 201) {
            logger.info("descriptionNameError");
            error = "descriptionNameError";
            return false;
        }

        logger.info("User add new order");
        return orderDAO.insert(order);
    }

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
    }

    public Order getById(int id) {
        return orderDAO.findById("id", id);
    }

    /**
     * the method fills in order sorting parameters and passes them to the dao
     * @param sortForUser - a marker responsible for the need for sorting for the user
     * @param sort - set of parameters for sorting
     * @param pagination - limit and offset
     * @return sorted order list from dao
     */

    public List<Order> getSortedOrders(boolean sortForUser, Sort sort, Pagination pagination) {
        Map<String, Object> filterParams = new HashMap<>();
        String sortingParams = null;
        Map<String, Integer> limitingParams = new LinkedHashMap<>();

        if (sort.getMasterId() != null && !sort.getMasterId().equals("-1")) {
            filterParams.put("craftsman_id", Integer.parseInt(sort.getMasterId()));
        }

        if (sort.getStatus() != null && !sort.getStatus().equals("ALL")) {
            filterParams.put("status", sort.getStatus());
        }

        if (sortForUser) {
            filterParams.put("user_id", sort.getUser().getId());
        }

        if (sort.getSort() != null && !sort.getSort().equals("none")) {
            sortingParams = sort.getSort();
        }

        limitingParams.put("LIMIT" , pagination.getOrdersOnPage());
        limitingParams.put("OFFSET" , pagination.getOffset());

        return orderDAO.findBy(filterParams, sortingParams, limitingParams);
    }

    public boolean saveComment(int orderId, Comment comment) {
        if (comment.getDescription().length() < 5) {
            logger.info("descriptionShortError");
            error = "descriptionShortError";
            return false;
        }
        commentDAO.insert(comment);
        orderDAO.setCommentId(orderId, comment);
        logger.info("comment has been saved");
        return true;
    }

    public int getNumberOfRows() {
        return orderDAO.totalRows;
    }
}
