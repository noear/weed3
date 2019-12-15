package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.utils.TypeRef;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {
    Long insert(T entity);

    Integer deleteById(Object id);
    Integer deleteByIds(Iterable<Object> idList);
    Integer deleteByMap(Map<String, Object> columnMap);
    Integer delete(Act1<WhereQ> condition);

    Integer updateById(T entity);
    Integer update(T entity, Act1<WhereQ> condition);

    T selectById(Object id);
    List<T> selectByIds(Iterable<Object> idList);
    List<T> selectByMap(Map<String, Object> columnMap);

    T selectOne(T entity);
    T selectOne(Act1<WhereQ> condition);
    Map<String, Object> selectMap(Act1<WhereQ> condition);

    Object selectObj(String column, Act1<WhereQ> condition);

    Long selectCount(Act1<WhereQ> condition);

    List<T> selectList(Act1<WhereQ> condition);
    List<Map<String, Object>> selectMaps(Act1<WhereQ> condition);
    List<Object> selectObjs(String column, Act1<WhereQ> condition);

    List<T> selectPage(int start, int end, Act1<WhereQ> condition);
    List<Map<String, Object>> selectMapsPage(int start, int end, Act1<WhereQ> condition);
}
