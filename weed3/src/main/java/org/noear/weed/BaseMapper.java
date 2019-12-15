package org.noear.weed;

import org.noear.weed.ext.Act1;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {
    Long insert(T entity);
    Long insert(DataItem data);
    void insertBatch(List<T> list);

    Integer deleteById(Object id);
    Integer deleteByIds(Iterable<Object> idList);
    Integer deleteByMap(Map<String, Object> columnMap);
    Integer delete(Act1<WhereQ> condition);

    Integer updateById(T entity, boolean excludeNull);
    Integer update(T entity, boolean excludeNull, Act1<WhereQ> condition);

    T selectById(Object id);
    List<T> selectByIds(Iterable<Object> idList);
    List<T> selectByMap(Map<String, Object> columnMap);

    T selectItem(T entity);
    T selectItem(Act1<WhereQ> condition);
    Map<String, Object> selectMap(Act1<WhereQ> condition);

    Object selectValue(String column, Act1<WhereQ> condition);

    Long selectCount(Act1<WhereQ> condition);

    List<T> selectList(Act1<WhereQ> condition);
    List<Map<String, Object>> selectMapList(Act1<WhereQ> condition);
    List<Object> selectArray(String column, Act1<WhereQ> condition);

    List<T> selectPage(int start, int end, Act1<WhereQ> condition);
    List<Map<String, Object>> selectMapPage(int start, int end, Act1<WhereQ> condition);
}
