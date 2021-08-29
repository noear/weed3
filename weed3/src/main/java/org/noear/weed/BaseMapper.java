package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.ext.Act2;

import java.util.List;
import java.util.Map;

/**
 * @author noear
 */
public interface BaseMapper<T> {
    Long insert(T entity, boolean excludeNull);
    void insertList(List<T> list);

    Integer deleteById(Object id);
    Integer deleteByIds(Iterable idList);
    Integer deleteByMap(Map<String, Object> columnMap);
    Integer delete(Act1<MapperWhereQ> condition);

    /**
     * @param excludeNull 排除null
     * */
    Integer updateById(T entity, boolean excludeNull);
    Integer update(T entity, boolean excludeNull, Act1<MapperWhereQ> condition);

    int[] upsertList(List<T> list, Act2<T,DataItem> dataBuilder, String conditionFields);

    Long upsert(T entity, boolean excludeNull);
    Long upsertBy(T entity, boolean excludeNull, String conditionFields);

    boolean existsById(Object id);
    boolean exists(Act1<MapperWhereQ> condition);

    T selectById(Object id);
    List<T> selectByIds(Iterable idList);
    List<T> selectByMap(Map<String, Object> columnMap);

    T selectItem(T entity);
    T selectItem(Act1<MapperWhereQ> condition);
    Map<String, Object> selectMap(Act1<MapperWhereQ> condition);

    Object selectValue(String column, Act1<MapperWhereQ> condition);

    Long selectCount(Act1<MapperWhereQ> condition);

    List<T> selectList(Act1<MapperWhereQ> condition);
    List<Map<String, Object>> selectMapList(Act1<MapperWhereQ> condition);
    List<Object> selectArray(String column, Act1<MapperWhereQ> condition);

    /**
     * @param start 从0开始
     * */
    List<T> selectPage(int start, int size, Act1<MapperWhereQ> condition);
    List<Map<String, Object>> selectMapPage(int start, int size, Act1<MapperWhereQ> condition);

    List<T> selectTop(int size, Act1<MapperWhereQ> condition);
    List<Map<String, Object>> selectMapTop(int size, Act1<MapperWhereQ> condition);
}
