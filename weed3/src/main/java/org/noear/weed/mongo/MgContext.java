package org.noear.weed.mongo;

import java.util.Map;
import java.util.Properties;

/**
 * @author noear 2021/2/5 created
 */
public class MgContext {
    MongoX mongoX;

    public MgContext(Properties properties, String db) {
        mongoX = new MongoX(properties, db);
    }

    public MgContext(String host, int port, String db) {
        mongoX = new MongoX(host, port, db);
    }

    /**
     * 表操作
     * */
    public MgTable table(String table) {
        return new MgTable(mongoX).table(table);
    }

    /**
     * 创建索引
     */
    public String createIndex(String coll, Map<String, Object> keys, Map<String, Object> options) {
        return mongoX.createIndex(coll, keys, options);
    }

    public String createIndex(String coll, Map<String, Object> keys) {
        return createIndex(coll, keys);
    }
}
