package com.suttori.dao;

import com.suttori.dao.interfaces.ElasticDao;
import com.suttori.db.ConnectionManager;
import com.suttori.entity.Order;
import com.suttori.entity.User;
import com.suttori.entity.enams.Locales;
import com.suttori.entity.enams.Role;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.joining;

public class UserDAO implements ElasticDao<User> {
    private final Logger logger = Logger.getLogger(UserDAO.class);
    public int totalRows;

    @Override
    public boolean insert(User user) {
        String insertUser = "INSERT INTO \"user\"(first_name, last_name, email, phone_number, password, balance, photo, activation_code, email_activated, locale, salt, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            preparedStatement.setString(9, "false");
            preparedStatement.setString(10, user.getLocale());
            preparedStatement.setBytes(11, user.getSalt());
            preparedStatement.setString(12, Role.USER.name());
            preparedStatement.execute();


            return true;
        } catch (SQLException e) {
            logger.info("error in save method");
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<User> findBy(Map<String, Object> filterParams, String sortingParams, Map<String, Integer> limitingParams) {

        AtomicInteger count = new AtomicInteger(1);
        StringBuilder stringBuilder = new StringBuilder();
        List<User> users = new ArrayList<>();

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

        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM \"user\" %s;", stringBuilder);
        System.out.println(find);
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
                users.add(buildObjectFromResultSet(resultSet));

            }
            resultSet.close();
            return users;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }





    @Override
    public User findBy(String byName, int value) {
        String find = String.format("SELECT * FROM \"user\" WHERE %s = ?", byName);
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
        String find = String.format("SELECT * FROM \"user\" WHERE %s = ?", byName);
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
    public Order findById(String variable, int id) {
        return null;
    }

    public List<User> findByRole(Role role) {
        String selectUsers = "Select * from \"user\" where role = ?";
        List<User> craftsmanList = new ArrayList<>();
        try (Connection con = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(selectUsers)) {
            preparedStatement.setString(1, role.name());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = findById(resultSet.getInt("id"));
                craftsmanList.add(user);
            }
            resultSet.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return craftsmanList;
    }

    @Override
    public void setVariable(String variable, int id, String value) {
        String setPassword = String.format("UPDATE \"user\" SET %s = ? WHERE id = ?", variable);
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
    public void setVariable(String variable, int id, float value) {
        String setBalance = String.format("UPDATE \"user\" SET %s = ? WHERE id = ?", variable);
        try (Connection con = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(setBalance)) {
            preparedStatement.setFloat(1, value);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        user.setEmailActivated(resultSet.getString("email_activated"));
        user.setLocale(Locales.valueOf(resultSet.getString("locale")));
        user.setSalt(resultSet.getBytes("salt"));
        user.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));
        return user;
    }


    public User findByEmail(String email) {
        return findBy("email", email);
    }

    public User findByActivationCode(String code) {
        return findBy("activation_code", code);
    }

    public void setPassword(int id, String password) {
        setVariable("password", id, password);
    }

    public void setEmailActivated(User user) {
        setVariable("email_activated", user.getId(), user.getEmailActivated());
    }

    public void setBalance(int userId, float balance){
        setVariable("balance", userId, balance);
    }

    public void setLocale(User user) {
        setVariable("locale", user.getId(), user.getLocale());
    }



}
