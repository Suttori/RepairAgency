package com.suttori.dao;

import com.suttori.dao.interfaces.ElasticDao;
import com.suttori.db.ConnectionManager;
import com.suttori.entity.Comment;
import com.suttori.entity.Order;
import com.suttori.entity.enams.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.joining;

public class OrderDAO implements ElasticDao<Order> {

    public int totalRows;

    @Override
    public boolean insert(Order order) {
        String addOrder = "INSERT INTO \"order\"(user_id, craftsman_id, order_name, description, comment_id, price, status, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addOrder)) {
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setInt(2, order.getCraftsmanId());
            preparedStatement.setString(3, order.getOrderName());
            preparedStatement.setString(4, order.getDescription());
            preparedStatement.setInt(5, order.getCommentId());
            preparedStatement.setInt(6, order.getPrice());
            preparedStatement.setString(7, order.getStatus().name());
            preparedStatement.setDate(8, new Date(order.getDate().getTime()));
            preparedStatement.execute();
            return true;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    @Override
    public Order findById(String variable, int id) {
        String find = String.format("SELECT * FROM \"order\" WHERE %s = ?", variable);
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return buildObjectFromResultSet(resultSet);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public void setVariable(String variable, int orderId, String value) {
        String set = String.format("UPDATE \"order\" SET %s = ? WHERE id = ?", variable);
        System.out.println(orderId);
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(set)) {
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, orderId);
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void setVariable(String variable, int orderId, int value) {
        String set = String.format("UPDATE \"order\" SET %s = ? WHERE id = ?", variable);
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(set)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setInt(2, orderId);
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    /**
     * the method collects the query string from the passed parameters,
     * executes the request depending on the number of parameters
     * @param filterParams - filter options
     * @param sortingParams - sorter options
     * @param limitingParams - limit and offset
     * @return sorted order list from dao
     */
    @Override
    public List<Order> findBy(Map<String, Object> filterParams, String sortingParams, Map<String, Integer> limitingParams) {

        AtomicInteger count = new AtomicInteger(1);
        StringBuilder stringBuilder = new StringBuilder();
        List<Order> orders = new ArrayList<>();

        if (!filterParams.isEmpty()) {
            stringBuilder.append("WHERE ");
            stringBuilder.append(filterParams.keySet().stream().map(k -> k + " = ?").collect(joining( " AND ")));
        }

        if (sortingParams != null) {
            stringBuilder.append(" ORDER BY ").append(sortingParams);
        }

        if (!limitingParams.isEmpty()) {
            stringBuilder.append(" LIMIT ? OFFSET ?");
        }

        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM \"order\" %s;", stringBuilder);

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {

            filterParams.values().forEach(value -> { if (value instanceof String) {
                try {
                    preparedStatement.setString(count.getAndIncrement(), (String) value);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
                if (value instanceof Integer) {
                    try {
                        preparedStatement.setInt(count.getAndIncrement(), (int) value);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            limitingParams.values().forEach(value -> {
                try {
                    preparedStatement.setInt(count.getAndIncrement(), value);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                orders.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return orders;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public Order buildObjectFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setUserId(resultSet.getInt("user_id"));
        order.setCraftsmanId(resultSet.getInt("craftsman_id"));
        order.setOrderName(resultSet.getString("order_name"));
        order.setDescription(resultSet.getString("description"));
        order.setCommentId(resultSet.getInt("comment_id"));
        order.setPrice(resultSet.getInt("price"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
        order.setDate(resultSet.getDate("date"));
        return order;
    }

    public void setStatus(int id, OrderStatus status){
        setVariable("status", id, status.name());
    }

    public void setPrice(int id, int price){
        setVariable("price", id, price);
    }

    public void setMaster(int id, int masterId){
        setVariable("craftsman_id", id, masterId);
    }

    public void setCommentId(int id, Comment comment){
        setVariable("comment_id", id, comment.getId());
    }

    @Override
    public void setVariable(String variable, int id, float value) {

    }

    @Override
    public Order findBy(String byName, int value) {
        return null;
    }

    @Override
    public Order findBy(String byName, String value) {
        return null;
    }

    @Override
    public Order findById(int id) {
        return null;
    }
}
