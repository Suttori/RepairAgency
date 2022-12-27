package com.suttori.dao;

import com.suttori.dao.interfaces.ElasticDao;
import com.suttori.db.ConnectionManager;
import com.suttori.entity.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO implements ElasticDao<User> {
    private final Logger logger = Logger.getLogger(UserDAO.class);


    @Override
    public boolean insert(User user) {
        String insertUser = "INSERT INTO Person(first_name, last_name, email, phone_number, password, balance, photo, activation_code, locale, salt, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(insertUser)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPhoneNumber());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setInt(6, 0);
            preparedStatement.setString(7, "defaultProfile.jpg");
            preparedStatement.setString(8, user.getActivationCode());
            preparedStatement.setString(9, user.getLocale());
            preparedStatement.setBytes(10, user.getSalt());
            preparedStatement.setString(11, "USER");
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.info("error in save method");
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findBy(String byName, int value) {
        String find = String.format("SELECT * FROM Person WHERE %s = ?", value);
        try (Connection con = ConnectionManager.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(find)) {
            preparedStatement.setInt(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return buildObjectFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findBy(String byName, String value) {
        String find = String.format("SELECT * FROM Person WHERE %s = ?", byName);
        try (Connection con = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(find)) {
            preparedStatement.setString(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return buildObjectFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findBy(String byName, String value, int start, int total) {
        return null;
    }

    @Override
    public List<User> findBy(String byName, int value, int start, int total) {
        return null;
    }

    @Override
    public void setVariable(String variable, int id, String value) {
        String setPassword = String.format("UPDATE Person SET %s = ? WHERE id = ?", variable);
        try (Connection con = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(setPassword)) {
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVariable(String variable, int id, int value) {

    }


    @Override
    public User findById(int id) {
        return findBy("id", id);
    }

    @Override
    public List<User> findAll(int start, int total) {
        return null;
    }

    @Override
    public User buildObjectFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setPassword(resultSet.getString("password"));
        user.setBalance(resultSet.getInt("balance"));
        user.setPhoto(resultSet.getString("photo"));
        user.setActivationCode(resultSet.getString("activation_code"));
        user.setLocale(resultSet.getString("locale"));
        user.setSalt(resultSet.getBytes("salt"));
        user.setRole(user.getRole());
        return user;
    }


    public User findByEmail(String email) {
        return findBy("email", email);
    }

    public void setPassword(int id, String password) {
        setVariable("password", id, password);
    }


}
