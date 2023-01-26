package com.suttori.entity;

import java.util.Date;

public class Comment {

    private int id;
    private int userId;
    private int craftsmanId;
    private int rate;
    private String description;
    private Date date = new Date(System.currentTimeMillis());

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCraftsmanId() {
        return craftsmanId;
    }

    public void setCraftsmanId(int craftsmanId) {
        this.craftsmanId = craftsmanId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", craftsmanId=" + craftsmanId +
                ", rate=" + rate +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
