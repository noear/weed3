package org.noear.weed.mongo;

import java.util.Properties;

/**
 * @author noear 2021/2/5 created
 */
public class MgContext {
    private MongoX mongoX;

    public MgContext(Properties properties, String db) {
        mongoX = new MongoX(properties, db);
    }

    public MgContext(String host, int port, String db) {
        mongoX = new MongoX(host, port, db);
    }

    /**
     * 表操作
     * */
    public MgTableQuery table(String table) {
        return new MgTableQuery(mongoX).table(table);
    }


    public MongoX mongoX(){
        return mongoX;
    }
}
