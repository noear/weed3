package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.ext.Act2;
import org.noear.weed.utils.RunUtils;
import org.noear.weed.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 19-12-11.
 */
public class BaseMapperWrap<T> implements BaseMapper<T> {
    private DbContext _db;
    private BaseEntityWrap _table;
    private String _tabelName;

    private Class<?> _entityType;
    protected Class<?> entityType() {
        return _entityType;
    }

    public BaseMapperWrap(DbContext db, Class<?> entityType, String tableName) {
        _db = db;
        _entityType = entityType;
        _table = BaseEntityWrap.get(this);

        if (StringUtils.isEmpty(tableName)) {
            _tabelName = _table.tableName;
        } else {
            _tabelName = tableName;
        }
    }

    public BaseMapperWrap(DbContext db, BaseMapper<T> baseMapper) {
        _db = db;
        _table = BaseEntityWrap.get(baseMapper);
        _tabelName = _table.tableName;
    }

    private DbContext db(){
        return _db;
    }

    private String tableName(){
        return _tabelName;
    }

    private String pk(){
        return _table.pkName;
    }

    private Class<?> entityClz(){
        return _table.entityClz;
    }



    @Override
    public Long insert(T entity, boolean excludeNull)  {
        DataItem data = new DataItem();

        if(excludeNull) {
            data.setEntityIf(entity, (k,v)-> v!=null);
        }else{
            data.setEntity(entity);
        }

        return RunUtils.call(()
                -> getQr().insert(data));
    }

    @Override
    public void insertList(List<T> list) {
        List<DataItem> list2 = new ArrayList<>();
        for(T d : list){
            list2.add(new DataItem().setEntity(d));
        }

        RunUtils.call(()
                -> getQr().insertList(list2));
    }

    @Override
    public Integer deleteById(Object id) {
        return RunUtils.call(()
                -> getQr().whereEq(pk(),id ).delete());
    }

    @Override
    public Integer deleteByIds(Iterable idList) {
        return RunUtils.call(()
                -> getQr().whereIn(pk(), idList ).delete());
    }

    @Override
    public Integer deleteByMap(Map<String, Object> columnMap) {
        return RunUtils.call(()
                -> getQr().whereMap(columnMap).delete());
    }

    @Override
    public Integer delete(Act1<MapperWhereQ> c) {
        return RunUtils.call(() -> {
            return getQr(c).delete();
        });
    }

    @Override
    public Integer updateById(T entity, boolean excludeNull) {
        DataItem data = new DataItem();

        if(excludeNull) {
            data.setEntityIf(entity, (k,v)-> v!=null);
        }else{
            data.setEntity(entity);
        }

        Object id = data.get(pk());

        return RunUtils.call(()
                -> getQr().whereEq(pk(), id ).update(data));
    }

    @Override
    public Integer update(T entity, boolean excludeNull, Act1<MapperWhereQ> c) {
        DataItem data = new DataItem();

        if(excludeNull) {
            data.setEntityIf(entity, (k,v)-> v!=null);
        }else{
            data.setEntity(entity);
        }

        return RunUtils.call(() -> {
            return getQr(c).update(data);
        });
    }

    @Override
    public int[] upsertList(List<T> list, Act2<T,DataItem> dataBuilder, String conditionFields) {
        return RunUtils.call(()
                -> getQr().updateList(list, dataBuilder, conditionFields));
    }

    @Override
    public Long upsert(T entity, boolean excludeNull) {
        DataItem data = new DataItem();

        if (excludeNull) {
            data.setEntityIf(entity, (k, v) -> v != null);
        } else {
            data.setEntity(entity);
        }

        Object id = data.get(pk());

        if (id == null) {
            return RunUtils.call(() -> getQr().insert(data));
        } else {
            return RunUtils.call(() -> getQr().upsertBy(data, pk()));
        }
    }

    @Override
    public Long upsertBy(T entity, boolean excludeNull, String conditionFields) {
        DataItem data = new DataItem();

        if (excludeNull) {
            data.setEntityIf(entity, (k, v) -> v != null);
        } else {
            data.setEntity(entity);
        }

        return RunUtils.call(() -> getQr().upsertBy(data, conditionFields));
    }

    @Override
    public boolean existsById(Object id) {
        return RunUtils.call(()
                -> getQr().whereEq(pk(), id ).selectExists());
    }

    @Override
    public boolean exists(Act1<MapperWhereQ> c) {
        return RunUtils.call(() -> {
            return getQr(c).selectExists();
        });
    }

    @Override
    public T selectById(Object id) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()
                -> getQr().whereEq(pk(), id).limit(1).selectItem("*", clz));
    }

    @Override
    public List<T> selectByIds(Iterable idList) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()
                -> getQr().whereIn(pk(), idList).selectList("*", clz));
    }

    @Override
    public List<T> selectByMap(Map<String, Object> columnMap) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()
                -> getQr().whereMap(columnMap).selectList("*", clz));
    }

    @Override
    public T selectItem(T entity) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(() ->
                getQr().whereEntity(entity).limit(1).selectItem("*", clz));
    }

    @Override
    public T selectItem(Act1<MapperWhereQ> c) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(() -> getQr(c).selectItem("*", clz));
    }

    @Override
    public Object selectValue(String column, Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> getQr(c).selectValue(column));
    }

    @Override
    public Map<String, Object> selectMap(Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> getQr(c).selectMap("*"));
    }

    @Override
    public Long selectCount(Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> getQr(c).count());
    }

    @Override
    public List<T> selectList(Act1<MapperWhereQ> c) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()-> getQr(c).selectList("*", clz));
    }

    @Override
    public List<Map<String, Object>> selectMapList(Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> getQr(c).selectMapList("*"));
    }

    @Override
    public List<Object> selectArray(String column, Act1<MapperWhereQ> c) {
        return RunUtils.call(() -> getQr(c).selectArray(column));
    }


    @Override
    public List<T> selectPage(int start, int size, Act1<MapperWhereQ> c) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()-> getQr(c).limit(start, size).selectList("*", clz));
    }

    @Override
    public List<Map<String, Object>> selectMapPage(int start, int size, Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> getQr(c).limit(start, size).selectMapList("*"));
    }

    @Override
    public List<T> selectTop(int size, Act1<MapperWhereQ> c) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()-> getQr(c).top(size).selectList("*", clz));
    }

    @Override
    public List<Map<String, Object>> selectMapTop(int size, Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> getQr(c).top(size).selectMapList("*"));
    }

    private DbTableQuery getQr(){
        return db().table(tableName());
    }

    private DbTableQuery getQr(Act1<MapperWhereQ> c){
        DbTableQuery qr = db().table(tableName());

        if(c != null) {
            c.run(new MapperWhereQ(qr));
        }

        return qr;
    }
}
