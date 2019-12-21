package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.utils.RunUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 19-12-11.
 */
public class BaseMapperWrap<T> implements BaseMapper<T> {
    private DbContext _db;
    private BaseEntityWrap _table;

    private Class<?> _entityType;
    protected Class<?> entityType() {
        return _entityType;
    }

    public BaseMapperWrap(DbContext db, Class<?> entityType) {
        _db = db;
        _entityType = entityType;
        _table = BaseEntityWrap.get(this);
    }

    public BaseMapperWrap(DbContext db, BaseMapper<T> baseMapper) {
        _db = db;
        _table = BaseEntityWrap.get(baseMapper);
    }

    private DbContext db(){
        return _db;
    }

    private String tableName(){
        return _table.tableName;
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
    public Integer deleteByIds(Iterable<Object> idList) {
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
            return getQr(c).update();
        });
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
            return RunUtils.call(() -> getQr().upsert(data, pk()));
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

        return RunUtils.call(() -> getQr().upsert(data, conditionFields));
    }

    @Override
    public boolean existsById(Object id) {
        return RunUtils.call(()
                -> getQr().whereEq(pk(), id ).exists());
    }

    @Override
    public boolean exists(Act1<MapperWhereQ> c) {
        return RunUtils.call(() -> {
            return getQr(c).exists();
        });
    }

    @Override
    public T selectById(Object id) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()
                -> getQr().whereEq(pk(), id).limit(1).select("*").getItem(clz));
    }

    @Override
    public List<T> selectByIds(Iterable<Object> idList) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()
                -> getQr().whereIn(pk(), idList).select("*").getList(clz));
    }

    @Override
    public List<T> selectByMap(Map<String, Object> columnMap) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()
                -> getQr().whereMap(columnMap).select("*").getList(clz));
    }

    @Override
    public T selectItem(T entity) {
        Class<T> clz = (Class<T>) entityClz();

        DataItem data = new DataItem();
        data.setEntityIf(entity,(k,v)-> v!=null );

        return RunUtils.call(()
                -> getQr().whereMap(data.getMap()).limit(1).select("*").getItem(clz));
    }

    @Override
    public T selectItem(Act1<MapperWhereQ> c) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()-> {
            return getQr(c).select("*").getItem(clz);
        });
    }

    @Override
    public Object selectValue(String column, Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> {
            return getQr(c).select(column).getValue();
        });
    }

    @Override
    public Map<String, Object> selectMap(Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> {
            return getQr(c).select("*").getMap();
        });
    }

    @Override
    public Long selectCount(Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> {
            return getQr(c).count();
        });
    }

    @Override
    public List<T> selectList(Act1<MapperWhereQ> c) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()-> {
            return getQr(c).select("*").getList(clz);
        });
    }

    @Override
    public List<Map<String, Object>> selectMapList(Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> {
            return getQr(c).select("*").getMapList();
        });
    }

    @Override
    public List<Object> selectArray(String column, Act1<MapperWhereQ> c) {
        return RunUtils.call(() -> {
            return getQr(c).select(column).getArray(column);
        });
    }


    @Override
    public List<T> selectPage(int start, int size, Act1<MapperWhereQ> c) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()-> {
            return getQr(c).limit(start, size).select("*").getList(clz);
        });
    }

    @Override
    public List<Map<String, Object>> selectMapPage(int start, int size, Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> {
            return getQr(c).limit(start, size).select("*").getMapList();
        });
    }

    @Override
    public List<T> selectTop(int size, Act1<MapperWhereQ> c) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()-> {
            return getQr(c).top(size).select("*").getList(clz);
        });
    }

    @Override
    public List<Map<String, Object>> selectMapTop(int size, Act1<MapperWhereQ> c) {
        return RunUtils.call(()-> {
            return getQr(c).top(size).select("*").getMapList();
        });
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
