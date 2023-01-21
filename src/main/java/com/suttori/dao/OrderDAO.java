package com.suttori.dao;

import com.suttori.dao.interfaces.Dao;
import com.suttori.dao.interfaces.ElasticDao;
import com.suttori.db.ConnectionManager;
import com.suttori.entity.Order;
import com.suttori.entity.enams.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements ElasticDao<Order> {

    public int totalRows;

    @Override
    public boolean insert(Order order) {
        String addOrder = "INSERT INTO \"order\"(user_id, craftsman_id, order_name, description, price, comment, status, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addOrder)) {
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setInt(2, order.getCraftsmanId());
            preparedStatement.setString(3, order.getOrderName());
            preparedStatement.setString(4, order.getDescription());
            preparedStatement.setInt(5, order.getPrice());
            preparedStatement.setString(6, order.getComment());
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
    public Order findById(int id) {
        String find = String.format("SELECT * FROM \"order\" WHERE %s = ?", id);
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


    public List<Order> findByUser(String by, int value, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM \"order\" WHERE %s = ? LIMIT ? OFFSET ?", by);
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);
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
    public List<Order> findAll(int start, int total) {
        String selectOrders = "Select *, count(*) OVER() AS total_count from \"order\" LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectOrders)) {
            preparedStatement.setInt(1, total);
            preparedStatement.setInt(2, start);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                Order order = buildObjectFromResultSet(resultSet);
                orders.add(order);
            }
            resultSet.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return orders;
    }

    public List<Order> findByMaster(String findBy, int masterId, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM \"order\" %s LIMIT ? OFFSET ?", findBy);
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, masterId);
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);
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

    public List<Order> findByStatus(String findBy, String status, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM \"order\" %s LIMIT ? OFFSET ?", findBy);
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);
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

    public List<Order> findBy(String findBy, int masterId, String status, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM \"order\" %s LIMIT ? OFFSET ?", findBy);
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, masterId);
            preparedStatement.setString(2, status);
            preparedStatement.setInt(3, total);
            preparedStatement.setInt(4, start);
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
        order.setPrice(resultSet.getInt("price"));
        order.setComment(resultSet.getString("comment"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
        order.setDate(resultSet.getDate("date"));
        return order;
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
    public List<Order> findBy(String byName, String value, int start, int total) {
        return null;
    }

    @Override
    public List<Order> findBy(String byName, int value, int start, int total) {
        return null;
    }

    @Override
    public void setVariable(String variable, int id, String value) {

    }

    @Override
    public void setVariable(String variable, int id, int value) {

    }
}