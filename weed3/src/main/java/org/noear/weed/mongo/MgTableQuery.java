package org.noear.weed.mongo;

import com.mongodb.client.model.IndexOptions;
import org.noear.weed.DataItem;
import org.noear.weed.cache.CacheUsing;
import org.noear.weed.cache.ICacheController;
import org.noear.weed.cache.ICacheService;
import org.noear.weed.ext.Fun2;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author noear 2021/2/5 created
 */
public class MgTableQuery implements ICacheController<MgTableQuery> {
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

    /**
     * <p><code>
     * db.table("user").whereScript("this.age > 20 && this.age <= 40")
     * </code></p>
     * 添加SQL where script 语句，需要服务器开启脚本能力
     */
    public MgTableQuery whereScript(String code) {
        initWhereMap();

        String fun = null;
        if (code.contains("return ")) {
            fun = "function (){" + code + "};";
        } else {
            fun = "function (){return " + code + "};";
        }

        whereMap.put("$where", fun);
        return this;
    }

    //添加SQL where = 语句
    public MgTableQuery whereEq(String field, Object val) {
        initWhereMap();

        whereMap.put(field, val);
        return this;
    }

    //添加SQL where != 语句
    public MgTableQuery whereNeq(String field, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$ne", val);
        whereMap.put(field, tmp);
        return this;
    }


    //添加SQL where < 语句
    public MgTableQuery whereLt(String field, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$lt", val);
        whereMap.put(field, tmp);
        return this;
    }

    //添加SQL where <= 语句
    public MgTableQuery whereLte(String field, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$lte", val);
        whereMap.put(field, tmp);
        return this;
    }

    //添加SQL where > 语句
    public MgTableQuery whereGt(String field, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gt", val);
        whereMap.put(field, tmp);
        return this;
    }

    //添加SQL where >= 语句
    public MgTableQuery whereGte(String field, Object val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gte", val);
        whereMap.put(field, tmp);
        return this;
    }

    public MgTableQuery whereBtw(String field, Object start, Object end) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gte", start);
        tmp.put("$lte", end);

        whereMap.put(field, tmp);
        return this;
    }

    public MgTableQuery whereNbtw(String field, Object start, Object end) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$gte", start);
        tmp.put("$lte", end);

        Map<String, Object> tmp2 = new LinkedHashMap<>();
        tmp2.put("$not",tmp);

        whereMap.put(field, tmp2);
        return this;
    }

    public MgTableQuery whereIn(String field, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$in", ary);
        whereMap.put(field, tmp);
        return this;
    }

    public MgTableQuery whereNin(String field, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$nin", ary);
        whereMap.put(field, tmp);
        return this;
    }

    public MgTableQuery whereLk(String field, String regex) {
        initWhereMap();

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        whereMap.put(field, pattern);
        return this;
    }

    public MgTableQuery whereNlk(String field, String regex) {
        initWhereMap();

        Pattern expr = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$not", expr);

        whereMap.put(field, tmp);
        return this;
    }




    public MgTableQuery whereMod(String field, long base, long val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$mod", Arrays.asList(base, val));
        whereMap.put(field, tmp);
        return this;
    }

    public MgTableQuery whereNmod(String field, long base, long val) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$mod", Arrays.asList(base, val));

        Map<String, Object> tmp2 = new LinkedHashMap<>();
        tmp2.put("$not", tmp2);

        whereMap.put(field, tmp2);
        return this;
    }

    public MgTableQuery whereAll(String field, Iterable<Object> ary) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$all", ary);
        whereMap.put(field, tmp);
        return this;
    }

    public MgTableQuery whereSize(String field, long size) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$size", size);
        whereMap.put(field, tmp);
        return this;
    }

    public MgTableQuery whereExists(String field, boolean exists) {
        initWhereMap();

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("$exists", exists);
        whereMap.put(field, tmp);
        return this;
    }




    //
    // for and
    //
    //添加SQL and = 语句
    public MgTableQuery andEq(String field, Object val) {
        return whereEq(field, val);
    }

    //添加SQL where != 语句
    public MgTableQuery andNeq(String field, Object val) {
        return whereNeq(field, val);
    }


    //添加SQL where < 语句
    public MgTableQuery andLt(String field, Object val) {
        return whereLt(field, val);
    }

    //添加SQL where <= 语句
    public MgTableQuery andLte(String field, Object val) {
        return whereLte(field, val);
    }

    //添加SQL where > 语句
    public MgTableQuery andGt(String field, Object val) {
        return whereGt(field, val);
    }

    //添加SQL where >= 语句
    public MgTableQuery andGte(String field, Object val) {
        return whereGte(field, val);
    }

    public MgTableQuery andBtw(String field, Object start, Object end) {
        return whereBtw(field, start, end);
    }

    public MgTableQuery andExists(String field, boolean exists) {
        return whereExists(field, exists);
    }

    public MgTableQuery andMod(String field, long base, long val) {
        return whereMod(field, base, val);
    }

    public MgTableQuery andNmod(String field, long base, long val) {
        return whereNmod(field, base, val);
    }

    public MgTableQuery andSize(String field, long size) {
        return whereSize(field, size);
    }

    public MgTableQuery andAll(String field, Iterable<Object> ary) {
        return whereAll(field, ary);
    }

    public MgTableQuery andIn(String field, Iterable<Object> ary) {
        return whereIn(field, ary);
    }

    public MgTableQuery andNin(String field, Iterable<Object> ary) {
        return whereNin(field, ary);
    }

    public MgTableQuery andLk(String field, String regex) {
        return whereLk(field, regex);
    }

    public MgTableQuery andNlk(String field, String regex) {
        return whereNlk(field, regex);
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
    public MgTableQuery set(String field, Object val) {
        if (dataItem == null) {
            dataItem = new LinkedHashMap<>();
        }

        dataItem.put(field, val);

        return this;
    }

    /**
     * 设置自增加
     */
    public MgTableQuery setInc(String field, long val) {
        if (dataItem == null) {
            dataItem = new LinkedHashMap<>();
        }

        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put(field, val);

        dataItem.put("$inc", tmp);

        return this;
    }

    public MgTableQuery setMap(Map<String, Object> map) {
        dataItem = map;
        return this;
    }

    public MgTableQuery setMapIf(Map<String, Object> map, Fun2<Boolean,String,Object> condition) {
        dataItem = new DataItem().setMapIf(map, condition).getMap();
        return this;
    }

    public MgTableQuery setEntity(Object bean) {
        dataItem = new DataItem().setEntity(bean).getMap();
        return this;
    }

    public MgTableQuery setEntityIf(Object bean, Fun2<Boolean,String,Object> condition) {
        dataItem = new DataItem().setEntityIf(bean, condition).getMap();
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

    public MgTableQuery orderByAsc(String field) {
        if (orderMap == null) {
            orderMap = new LinkedHashMap<>();
        }

        orderMap.put(field, 1);

        return this;
    }

    public MgTableQuery orderByDesc(String field) {
        if (orderMap == null) {
            orderMap = new LinkedHashMap<>();
        }

        orderMap.put(field, -1);
        return this;
    }

    public MgTableQuery andByAsc(String field) {
        return orderByAsc(field);
    }

    public MgTableQuery andByDesc(String field) {
        return orderByDesc(field);
    }

    public <T> List<T> selectList(Class<T> clz) {
        List<T> list = new ArrayList<>();
        List<Map<String, Object>> listTmp = selectMapList();

        if (listTmp != null) {
            for (Map<String, Object> itemTmp : listTmp) {
                list.add(new DataItem().setMap(itemTmp).toEntity(clz));
            }
        }

        return list;
    }

    public <T> T selectItem(Class<T> clz) {
        Map<String, Object> itemTmp = selectMap();
        if (itemTmp == null) {
            return null;
        } else {
            return new DataItem().setMap(itemTmp).toEntity(clz);
        }
    }

    private String getWeedkey(Map<String, Object> filter){
        StringBuilder buf = new StringBuilder();
        buf.append(table)
                .append("@").append(filter.toString())
                .append("@").append(limit_size)
                .append("@").append(limit_start);

        return buf.toString();
    }

    public List<Map<String, Object>> selectMapList() {
        Map<String, Object> filter = buildFilter(false);

        if (_cache == null) {
            return selectMapListDo(filter);
        } else {
            String weedKey = getWeedkey(filter);
            return _cache.getEx(weedKey, () -> selectMapListDo(filter));
        }
    }

    private List<Map<String, Object>> selectMapListDo(Map<String, Object> filter) {
        if (limit_size > 0) {
            return mongoX.findPage(table, filter, orderMap, limit_start, limit_size);
        } else {
            return mongoX.findMany(table, filter, orderMap);
        }
    }

    public <T> List<T> selectArray(String field) {
        List<T> list = new ArrayList<>();
        List<Map<String, Object>> listTmp = selectMapList();

        if(listTmp != null) {
            for (Map<String, Object> map : listTmp) {
                Object v1 = map.get(field);
                if (v1 != null) {
                    list.add((T) v1);
                }
            }
        }

        return list;
    }

    public Map<String, Object> selectMap() {
        Map<String, Object> filter = buildFilter(true);

        if (_cache == null) {
            return mongoX.findOne(table, filter);
        } else {
            String weedKey = getWeedkey(filter);
            return _cache.getEx(weedKey, () -> mongoX.findOne(table, filter));
        }
    }

    public long selectCount() {
        Map<String, Object> filter = buildFilter(false);

        if (_cache == null) {
            return selectCountDo(filter);
        } else {
            String weedKey = getWeedkey(filter);
            return _cache.getEx(weedKey, () -> selectCountDo(filter));
        }
    }

    private long selectCountDo(Map<String, Object> filter) {
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

    public String createIndex(Map<String,Object> options) {
        IndexOptions options1 = new DataItem().setMap(options).toEntity(IndexOptions.class);
        return createIndex(options1);
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

    public MgTableQuery build(Consumer<MgTableQuery> builder) {
        builder.accept(this);
        return this;
    }


    //=======================
    //
    // 缓存控制相关
    //

    protected CacheUsing _cache = null;

    /**
     * 使用一个缓存服务
     */
    public MgTableQuery caching(ICacheService service) {
        _cache = new CacheUsing(service);
        return this;
    }

    /**
     * 是否使用缓存
     */
    public MgTableQuery usingCache(boolean isCache) {
        _cache.usingCache(isCache);
        return this;
    }

    /**
     * 使用缓存时间（单位：秒）
     */
    public MgTableQuery usingCache(int seconds) {
        _cache.usingCache(seconds);
        return this;
    }

    /**
     * 为缓存添加标签
     */
    public MgTableQuery cacheTag(String tag) {
        _cache.cacheTag(tag);
        return this;
    }
}
