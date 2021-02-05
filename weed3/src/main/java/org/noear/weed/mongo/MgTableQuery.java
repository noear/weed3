package org.noear.weed.mongo;

import com.mongodb.client.model.IndexOptions;
import org.noear.weed.DataItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author noear 2021/2/5 created
 */
public class MgTableQuery {
    private String table;
    private Map<String, Object> whereMap;
    private Map<String, Object> orderMap;
    private Map<String, Object> dataItem;
    private int limit_size;
    private int limit_start;

    private MongoX mongoX;

    private void initWhereMap() {
        if (whereMap == null) {
            whereMap = new LinkedHashMap<>();
        }
    }


    public MgTableQuery(MongoX mongoX) {
        this.mongoX = mongoX;
    }

    public MgTableQuery table(String table) {
        this.table = table;
        return this;
    }

    public MgTableQuery whereMap(Map<String, Object> map) {
        this.whereMap = map;
        return this;
    }

    //添加SQL where = 语句
    public MgTableQuery whereTrue() {
        initWhereMap();
        return this;
    }

    //添加SQL where = 语句
    public MgTableQuery whereEq(String col, Object val) {
        initWhereMap();

        whereMap.put(col, val);
        return this;
    }

    //添加SQL where != 语句
    public MgTableQuery whereNeq(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$ne", val);
        whereMap.put(col, tmp);
        return this;
    }


    //添加SQL where < 语句
    public MgTableQuery whereLt(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$lt", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where <= 语句
    public MgTableQuery whereLte(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$lte", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where > 语句
    public MgTableQuery whereGt(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gt", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where >= 语句
    public MgTableQuery whereGte(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gte", val);
        whereMap.put(col, tmp);
        return this;
    }


    public MgTableQuery whereExists(String col, boolean exists) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$exists", exists);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTableQuery whereIn(String col, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$in", ary);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTableQuery whereNin(String col, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$nin", ary);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTableQuery whereLk(String col, String regex) {
        initWhereMap();

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        whereMap.put(col, pattern);
        return this;
    }


    //
    // for and
    //
    //添加SQL and = 语句
    public MgTableQuery andEq(String col, Object val) {
        initWhereMap();

        whereMap.put(col, val);
        return this;
    }

    //添加SQL where != 语句
    public MgTableQuery andNeq(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$ne", val);
        whereMap.put(col, tmp);
        return this;
    }


    //添加SQL where < 语句
    public MgTableQuery andLt(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$lt", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where <= 语句
    public MgTableQuery andLte(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$lte", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where > 语句
    public MgTableQuery andGt(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gt", val);
        whereMap.put(col, tmp);
        return this;
    }

    //添加SQL where >= 语句
    public MgTableQuery andGte(String col, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gte", val);
        whereMap.put(col, tmp);
        return this;
    }


    public MgTableQuery andExists(String col, boolean exists) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$exists", exists);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTableQuery andIn(String col, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$in", ary);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTableQuery andNin(String col, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$nin", ary);
        whereMap.put(col, tmp);
        return this;
    }

    public MgTableQuery andLk(String col, String regex) {
        initWhereMap();

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        whereMap.put(col, pattern);
        return this;
    }


    private Map<String, Object> buildFilter(boolean forced) {
        if (whereMap == null) {
            throw new IllegalArgumentException("No where condition...");
        }

        if (forced) {
            if (whereMap.size() == 0) {
                throw new IllegalArgumentException("No where condition...");
            }
        }

        return whereMap;
    }

    //
    // set
    //
    public MgTableQuery set(String col, Object val) {
        if (dataItem == null) {
            dataItem = new LinkedHashMap<>();
        }

        dataItem.put(col, val);

        return this;
    }

    public MgTableQuery setMap(Map<String, Object> map) {
        dataItem = map;
        return this;
    }

    public MgTableQuery setEntity(Object bean) {
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

        Map<String, Object> filter = buildFilter(true);

        return mongoX.updateMany(table, filter, dataItem);
    }

    //
    // 替换
    //
    public long replace() {
        Map<String, Object> filter = buildFilter(true);

        return mongoX.replaceOne(table, filter, dataItem);
    }

    //
    // 删除
    //

    public long delete() {
        Map<String, Object> filter = buildFilter(true);

        return mongoX.deleteMany(table, filter);
    }

    //
    // 查询
    //

    public MgTableQuery limit(int size) {
        limit_size = size;
        return this;
    }

    public MgTableQuery limit(int start, int size) {
        limit_size = size;
        limit_start = start;
        return this;
    }

    public MgTableQuery orderByAsc(String col) {
        if (orderMap == null) {
            orderMap = new LinkedHashMap<>();
        }

        orderMap.put(col, 1);

        return this;
    }

    public MgTableQuery orderByDesc(String col) {
        if (orderMap == null) {
            orderMap = new LinkedHashMap<>();
        }

        orderMap.put(col, -1);
        return this;
    }

    public MgTableQuery andByAsc(String col) {
        return orderByAsc(col);
    }

    public MgTableQuery andByDesc(String col) {
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
        Map<String, Object> filter = buildFilter(true);

        if (limit_size > 0) {
            return mongoX.findPage(table, filter, orderMap, limit_start, limit_size);
        } else {
            return mongoX.findMany(table, filter, orderMap);
        }
    }

    public <T> List<T> selectArray(String col) {
        List<T> list = new ArrayList<>();
        List<Map<String, Object>> listTmp = selectMapList();

        for (Map<String, Object> map : listTmp) {
            Object v1 = map.get(col);
            if (v1 != null) {
                list.add((T) v1);
            }
        }

        return list;
    }

    public Map<String, Object> selectMap() {
        Map<String, Object> filter = buildFilter(true);

        return mongoX.findOne(table, filter);
    }


    public long selectCount() {
        Map<String, Object> filter = buildFilter(false);
        if (filter.size() > 0) {
            return mongoX.countDocuments(table, filter);
        } else {
            return mongoX.count(table);
        }
    }

    public boolean selectExists() {
        Map map = selectMap();

        return (map != null && map.size() > 0);
    }

    public String createIndex(boolean background) {
        return createIndex(new IndexOptions().background(background));
    }

    public String createIndex(IndexOptions options) {
        if (orderMap == null || orderMap.size() == 0) {
            throw new IllegalArgumentException("No index keys...");
        }

        if (options == null) {
            return mongoX.createIndex(table, orderMap);
        } else {
            return mongoX.createIndex(table, orderMap, options);
        }
    }

    public MgTableQuery build(Consumer<MgTableQuery> builder){
        builder.accept(this);
        return this;
    }
}
