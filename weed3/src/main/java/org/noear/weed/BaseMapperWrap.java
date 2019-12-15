package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.utils.RunUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseMapperWrap<T> implements BaseMapper<T> {
    private DbContext _db;
    private BaseEntityWrap _table;

    private Type _entityClz;
    protected Type entityType() {
        return _entityClz;
    }

    public BaseMapperWrap(DbContext db, Class<?> entityClz) {
        _db = db;
        _entityClz = entityClz;
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
    public Long insert(T entity)  {
        return RunUtils.call(()
                -> db().table(tableName()).setEntity(entity).insert());
    }

    @Override
    public Long insert(DataItem data) {
        return RunUtils.call(()
                -> db().table(tableName()).insert(data));
    }

    @Override
    public void insertBatch(List<T> list) {
        List<DataItem> list2 = new ArrayList<>();
        for(T d : list){
            list2.add(new DataItem().setEntity(d));
        }

        RunUtils.call(()
                -> db().table(tableName()).insertList(list2));
    }

    @Override
    public Integer deleteById(Object id) {
        return RunUtils.call(()
                -> db().table(tableName()).whereEq(pk(),id ).delete());
    }

    @Override
    public Integer deleteByIds(Iterable<Object> idList) {
        return RunUtils.call(()
                -> db().table(tableName()).whereIn(pk(), idList ).delete());
    }

    @Override
    public Integer deleteByMap(Map<String, Object> columnMap) {
        return RunUtils.call(()
                -> db().table(tableName()).whereMap(columnMap).delete());
    }

    @Override
    public Integer delete(Act1<WhereQ> condition) {
        return RunUtils.call(() -> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.delete();
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
                -> db().table(tableName()).whereEq(pk(), id ).update(data));
    }

    @Override
    public Integer update(T entity, boolean excludeNull, Act1<WhereQ> condition) {
        DataItem data = new DataItem();

        if(excludeNull) {
            data.setEntityIf(entity, (k,v)-> v!=null);
        }else{
            data.setEntity(entity);
        }

        return RunUtils.call(() -> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.update();
        });
    }

    @Override
    public boolean existsById(Object id) {
        return RunUtils.call(()
                -> db().table(tableName()).whereEq(pk(), id ).exists());
    }

    @Override
    public boolean exists(Act1<WhereQ> condition) {
        return RunUtils.call(() -> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.exists();
        });
    }

    @Override
    public T selectById(Object id) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()
                -> db().table(tableName()).whereEq(pk(), id).limit(1).select("*").getItem(clz));
    }

    @Override
    public List<T> selectByIds(Iterable<Object> idList) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()
                -> db().table(tableName()).whereIn(pk(), idList).select("*").getList(clz));
    }

    @Override
    public List<T> selectByMap(Map<String, Object> columnMap) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()
                -> db().table(tableName()).whereMap(columnMap).select("*").getList(clz));
    }

    @Override
    public T selectItem(T entity) {
        Class<T> clz = (Class<T>) entityClz();

        DataItem data = new DataItem();
        data.setEntityIf(entity,(k,v)-> v!=null );

        return RunUtils.call(()
                -> db().table(tableName()).whereMap(data.getMap()).limit(1).select("*").getItem(clz));
    }

    @Override
    public T selectItem(Act1<WhereQ> condition) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.select("*").getItem(clz);
        });
    }

    @Override
    public Object selectValue(String column, Act1<WhereQ> condition) {
        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.select(column).getValue();
        });
    }

    @Override
    public Map<String, Object> selectMap(Act1<WhereQ> condition) {
        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.select("*").getMap();
        });
    }

    @Override
    public Long selectCount(Act1<WhereQ> condition) {
        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.count();
        });
    }

    @Override
    public List<T> selectList(Act1<WhereQ> condition) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.select("*").getList(clz);
        });
    }

    @Override
    public List<Map<String, Object>> selectMapList(Act1<WhereQ> condition) {
        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.select("*").getMapList();
        });
    }

    @Override
    public List<Object> selectArray(String column, Act1<WhereQ> condition) {
        return RunUtils.call(() -> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.select(column).getArray(column);
        });
    }


    @Override
    public List<T> selectPage(int start, int end, Act1<WhereQ> condition) {
        Class<T> clz = (Class<T>) entityClz();

        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.limit(start, end).select("*").getList(clz);
        });
    }

    @Override
    public List<Map<String, Object>> selectMapPage(int start, int end, Act1<WhereQ> condition) {
        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.limit(start, end).select("*").getMapList();
        });
    }
}
