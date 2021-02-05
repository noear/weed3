package org.noear.weed.mongo;

import org.noear.weed.DataItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author noear 2021/2/5 created
 */
public class MgTable {
    private String table;
    private Map<String, Object> whereMap;
    private Map<String, Object> orderMap;
    private Map<String, Object> dataItem;
    private int limit_size;
    private int limit_start;

    private MongoX mongoX;

    private void initWhereMap(){
        if(whereMap == null){
            whereMap = new LinkedHashMap<>();
        }
    }


    public MgTable(MongoX mongoX) {
        this.mongoX = mongoX;
    }

    public MgTable table(String table) {
        this.table = table;
        return this;
    }

    public MgTable whereMap(Map<String, Object> map) {
        this.whereMap = map;
        return this;
    }

    //添加SQL where = 语句
    public MgTable whereEq(String col, Object val) {
        initWhereMap();

        whereMap.put(col, val);
        return this;
    }

    //添加SQL where != 语句
    public MgTable whereNeq(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$ne", val);
        whereMap.put(col, tmp);
        return this;
    }


    //添加SQL where < 语句
    public MgTable whereLt(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$lt", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where <= 语句
    public MgTable whereLte(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$lte", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where > 语句
    public MgTable whereGt(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gt", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where >= 语句
    public MgTable whereGte(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gte", val);
        whereMap.put(col, tmp);
        return this;
    }


    public MgTable whereExists(String col, boolean exists) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$exists", exists);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTable whereIn(String col, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$in", ary);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTable whereNin(String col, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$nin", ary);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTable whereLk(String col, String regex) {
        initWhereMap();

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        whereMap.put(col, pattern);
        return this;
    }


    //
    // for and
    //
    //添加SQL and = 语句
    public MgTable andEq(String col, Object val) {
        initWhereMap();

        whereMap.put(col, val);
        return this;
    }

    //添加SQL where != 语句
    public MgTable andNeq(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$ne", val);
        whereMap.put(col, tmp);
        return this;
    }


    //添加SQL where < 语句
    public MgTable andLt(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$lt", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where <= 语句
    public MgTable andLte(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$lte", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where > 语句
    public MgTable andGt(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gt", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where >= 语句
    public MgTable andGte(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gte", val);
        whereMap.put(col, tmp);
        return this;
    }


    public MgTable andExists(String col, boolean exists) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$exists", exists);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTable andIn(String col, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$in", ary);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTable andNin(String col, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$nin", ary);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTable andLk(String col, String regex) {
        initWhereMap();

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        whereMap.put(col, pattern);
        return this;
    }


    private Map<String, Object> buildFilter() {
        if (whereMap == null || whereMap.size() == 0) {
            throw new IllegalArgumentException("No update condition...");
        }

        return whereMap;
    }

    //
    // set
    //
    public MgTable set(String col, Object val) {
        if (dataItem == null) {
            dataItem = new LinkedHashMap<>();
        }

        dataItem.put(col, val);

        return this;
    }

    public MgTable setMap(Map<String, Object> map) {
        dataItem = map;
        return this;
    }

    public MgTable setEntity(Object bean) {
        dataItem = new DataItem().setEntity(bean).getMap();
        return this;
    }


    //
    // 插入
    //
    public void insert() {
        insert(dataItem);
    }

    public void insert(Map<String, Object> data) {
        if (data == null || data.size() == 0) {
            throw new IllegalArgumentException("No insert data...");
        }

        mongoX.insertOne(table, data);
    }

    public void insertList(List<Map<String, Object>> dataList) {
        mongoX.insertMany(table, dataList);
    }

    //
    // 更新
    //
    public long update() {
        if (dataItem == null || dataItem.size() == 0) {
            throw new IllegalArgumentException("No update data...");
        }

        Map<String, Object> filter = buildFilter();

        return mongoX.updateMany(table, filter, dataItem);
    }

    //
    // 替换
    //
    public long replace(){
        Map<String, Object> filter = buildFilter();

        return mongoX.replaceOne(table, filter, dataItem);
    }

    //
    // 删除
    //

    public long delete() {
        Map<String, Object> filter = buildFilter();

        return mongoX.deleteMany(table, filter);
    }

    //
    // 查询
    //

    public MgTable limit(int size) {
        limit_size = size;
        return this;
    }

    public MgTable limit(int start, int size) {
        limit_size = size;
        limit_start = start;
        return this;
    }

    public MgTable orderByAsc(String col) {
        if (orderMap == null) {
            orderMap = new LinkedHashMap<>();
        }

        orderMap.put(col, 1);

        return this;
    }

    public MgTable orderByDesc(String col) {
        if (orderMap == null) {
            orderMap = new LinkedHashMap<>();
        }

        orderMap.put(col, -1);
        return this;
    }

    public MgTable andByAsc(String col) {
        return orderByAsc(col);
    }

    public MgTable andByDesc(String col) {
        return orderByDesc(col);
    }

    public <T> List<T> selectList(Class<T> clz) {
        List<T> list = new ArrayList<>();
        List<Map<String, Object>> listTmp = selectMapList();

        for (Map<String, Object> itemTmp : listTmp) {
            list.add(new DataItem().setMap(itemTmp).toEntity(clz));
        }

        return list;
    }

    public <T> T selectItem(Class<T> clz) {
        Map<String, Object> itemTmp = selectMap();
        return new DataItem().setMap(itemTmp).toEntity(clz);
    }

    public List<Map<String, Object>> selectMapList() {
        Map<String, Object> filter = buildFilter();

        if (limit_size > 0) {
            return mongoX.findPage(table, filter, orderMap, limit_start, limit_size);
        } else {
            return mongoX.findMany(table, filter, orderMap);
        }
    }

    public Map<String, Object> selectMap() {
        Map<String, Object> filter = buildFilter();

        return mongoX.findOne(table, filter);
    }


    public long selectCount(){
        Map<String, Object> filter = buildFilter();
        return mongoX.count(table);
    }

    public boolean selectExists(){
        Map map = selectMap();

        return (map != null && map.size() > 0);
    }
}