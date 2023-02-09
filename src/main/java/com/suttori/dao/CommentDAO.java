package com.suttori.dao;

import com.suttori.dao.interfaces.Dao;
import com.suttori.db.ConnectionManager;
import com.suttori.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO implements Dao<Comment> {

    @Override
    public boolean insert(Comment comment) {
        String addComment = "INSERT INTO comment(user_id, craftsman_id, rate, description, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addComment, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, comment.getUserId());
            preparedStatement.setInt(2, comment.getCraftsmanId());
            preparedStatement.setInt(3, comment.getRate());
            preparedStatement.setString(4, comment.getDescription());
            preparedStatement.setDate(5, new Date(comment.getDate().getTime()));
            preparedStatement.execute();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            int commentId = rs.getInt(1);
            comment.setId(commentId);
            rs.close();

            return true;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    @Override
    public Comment findById(int id) {
        String find = "SELECT * FROM comment WHERE id = ?";
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
    public Comment buildObjectFromResultSet(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getInt("id"));
        comment.setUserId(resultSet.getInt("user_id"));
        comment.setCraftsmanId(resultSet.getInt("craftsman_id"));
        comment.setRate(resultSet.getInt("rate"));
        comment.setDescription(resultSet.getString("description"));
        comment.setDate(resultSet.getDate("date"));
        return comment;
    }
}
