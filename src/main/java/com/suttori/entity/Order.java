package com.suttori.entity;

import com.suttori.dao.CommentDAO;
import com.suttori.entity.enams.OrderStatus;
import com.suttori.service.UserService;

import java.util.Date;

public class Order {
    private int id;
    private String orderName;
    private String description;
    private int price;
    private int userId;
    private int craftsmanId;
    private int commentId;
    private OrderStatus status = OrderStatus.ACCEPTED;
    private Date date = new Date(System.currentTimeMillis());

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String name) {
        this.orderName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int personId) {
        this.userId = personId;
    }

    public int getCraftsmanId() {
        return craftsmanId;
    }

    public void setCraftsmanId(int craftsmanId) {
        this.craftsmanId = craftsmanId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getCraftsman() {
        UserService userService = new UserService();
        return userService.getUserById(craftsmanId);
    }

    public User getUser() {
        UserService userService = new UserService();
        return userService.getUserById(userId);
    }

    public String getComment(){
        CommentDAO commentDao = new CommentDAO();
        Comment comment = commentDao.findById(commentId);
        return comment.getDescription();
    }

    public boolean isNew() {
        return status.equals(OrderStatus.ACCEPTED);
    }

    public boolean isPendingPayment() {
        return status.equals(OrderStatus.PENDING_PAYMENT);
    }

    public boolean isPaid() {
        return status.equals(OrderStatus.PAID);
    }

    public boolean isCanceled() {
        return status.equals(OrderStatus.CANCELED);
    }

    public boolean isInProgress() {
        return status.equals(OrderStatus.IN_PROGRESS);
    }

    public boolean isCompleted() {
        return status.equals(OrderStatus.COMPLETED);
    }



    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + orderName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", personId=" + userId +
                ", craftsmanId=" + craftsmanId +
                ", comment=" + commentId +
                ", status=" + status +
                ", date=" + date +
                '}';
    }
}
