package dao.interfaces;

import java.util.List;

public interface ElasticDao<T> extends Dao<T> {

    T findBy(String byName, int value);

    T findBy(String byName, String value);

    List<T> findBy(String byName, String value, int start, int total);

    List<T> findBy(String byName, int value, int start, int total);

    void setVariable(String variable, int id, String value);

    void setVariable(String variable, int id, int value);

}