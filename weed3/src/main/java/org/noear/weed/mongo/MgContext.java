package org.noear.weed.mongo;

import org.noear.weed.utils.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author noear 2021/2/5 created
 */
public class MgContext implements AutoCloseable {
    private MongoX mongoX;

    public MgContext(Properties properties) {
        String schema = properties.getProperty("schema");
        if(StringUtils.isEmpty(schema)){
            schema = properties.getProperty("db");
        }

        mongoX = new MongoX(properties, schema);
    }

    public MgContext(Properties properties, String schema) {
        mongoX = new MongoX(properties, schema);
    }

    public MgContext(String url, String schema) {
        mongoX = new MongoX(url, schema);
    }

    /**
     * 获取表操作
     */
    public MgTableQuery table(String table) {
        return new MgTableQuery(mongoX).table(table);
    }

    /**
     * 获取驱动操作
     */
    public MongoX mongo() {
        return mongoX;
    }

    @Override
    public void close() throws IOException {
        if (mongoX != null) {
            mongoX.close();
        }
    }
}
