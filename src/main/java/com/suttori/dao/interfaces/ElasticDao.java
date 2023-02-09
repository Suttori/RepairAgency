package com.suttori.dao.interfaces;

import com.suttori.entity.Order;
import com.suttori.entity.enams.SortingParams;

import java.util.List;
import java.util.Map;

public interface ElasticDao<T> extends Dao<T> {

    List<T> findBy(Map<String, Object> filterParams, String sortingParams, Map<String, Integer> limitingParams);

    T findBy(String by, int value);

    T findBy(String by, String value);

    Order findById(String variable, int id);

    void setVariable(String variable, int id, String value);

    void setVariable(String variable, int id, int value);

    void setVariable(String variable, int id, float value);
}