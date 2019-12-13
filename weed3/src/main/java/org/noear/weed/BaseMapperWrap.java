package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.utils.RunUtils;
import org.noear.weed.utils.TypeRef;

import java.util.List;
import java.util.Map;

public class BaseMapperWrap<T> implements BaseMapper<T> {
    private DbContext db(){
        return null;
    }


    private TypeRef entityType(){
        return new TypeRef<T>() {};
    }

    private String tableName(){
        return entityType().getType().getTypeName();
    }

    private String pk(){
        return null;
    }

    @Override
    public Long insert(T entity)  {
        return RunUtils.call(()
                -> db().table(tableName()).setEntity(entity).insert());
    }

    @Override
    public Integer deleteById(Object id) {
        return RunUtils.call(()
                -> db().table(tableName()).whereEq(pk(),id ).delete());
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
    public Integer deleteBatchIds(Iterable<Object> idList) {
        return RunUtils.call(()
                -> db().table(tableName()).whereIn(pk(), idList ).delete());
    }

    @Override
    public Integer updateById(T entity) {
        DataItem data = new DataItem();
        data.setEntity(entity);

        Object id = data.get(pk());

        return RunUtils.call(()
                -> db().table(tableName()).whereEq(pk(), id ).update(data));
    }

    @Override
    public Integer update(T entity, Act1<WhereQ> condition) {
        DataItem data = new DataItem();
        data.setEntity(entity);

        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.update();
        });
    }

    @Override
    public T selectById(Object id) {
        Class<T> clz = (Class<T>) new TypeRef<T>() {}.getType();

        return RunUtils.call(()
                -> db().table(tableName()).whereEq(pk(), id).limit(1).select("*").getItem(clz));
    }

    @Override
    public List<T> selectBatchIds(Iterable<Object> idList) {
        Class<T> clz = (Class<T>) new TypeRef<T>() {}.getType();

        return RunUtils.call(()
                -> db().table(tableName()).whereIn(pk(), idList).select("*").getList(clz));
    }

    @Override
    public List<T> selectByMap(Map<String, Object> columnMap) {
        Class<T> clz = (Class<T>) new TypeRef<T>() {}.getType();

        return RunUtils.call(()
                -> db().table(tableName()).whereMap(columnMap).select("*").getList(clz));
    }

    @Override
    public T selectOne(T entity) {
        Class<T> clz = (Class<T>) new TypeRef<T>() {}.getType();

        DataItem data = new DataItem();
        data.setEntityIf(entity,(k,v)-> v!=null );

        return RunUtils.call(()
                -> db().table(tableName()).whereMap(data.getMap()).limit(1).select("*").getItem(clz));
    }

    @Override
    public T selectOne(Act1<WhereQ> condition) {
        Class<T> clz = (Class<T>) new TypeRef<T>() {}.getType();

        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.select("*").getItem(clz);
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
        Class<T> clz = (Class<T>) new TypeRef<T>() {}.getType();

        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.select("*").getList(clz);
        });
    }

    @Override
    public List<Map<String, Object>> selectMaps(Act1<WhereQ> condition) {
        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.select("*").getMapList();
        });
    }


    @Override
    public List<T> selectPage(int start, int end, Act1<WhereQ> condition) {
        Class<T> clz = (Class<T>) new TypeRef<T>() {}.getType();

        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.limit(start, end).select("*").getList(clz);
        });
    }

    @Override
    public List<Map<String, Object>> selectMapsPage(int start, int end, Act1<WhereQ> condition) {
        Class<T> clz = (Class<T>) new TypeRef<T>() {}.getType();

        return RunUtils.call(()-> {
            DbTableQuery qr = db().table(tableName());

            condition.run(new WhereQ(qr));

            return qr.limit(start, end).select("*").getMapList();
        });
    }
}
