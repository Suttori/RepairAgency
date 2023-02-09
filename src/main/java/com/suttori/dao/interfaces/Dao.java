package com.suttori.dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {

    boolean insert(T t);

    T findById(int id);

    T buildObjectFromResultSet(ResultSet resultSet) throws SQLException;
}