package entity;

import entity.enams.OrderStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private String name;
    private String description;
    private int price;
    private String location;
    private int userId;
    private int masterId;
    private int commentId;
    private List<OrderStatus> status;
    private Date date = new Date(System.currentTimeMillis());
}
