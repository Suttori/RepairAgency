package com.suttori.entity;

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
    private String comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + orderName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", personId=" + userId +
                ", craftsmanId=" + craftsmanId +
                ", comment=" + comment +
                ", status=" + status +
                ", date=" + date +
                '}';
    }
}
