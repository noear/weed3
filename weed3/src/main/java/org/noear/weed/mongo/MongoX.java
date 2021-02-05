package org.noear.weed.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.noear.weed.DataItem;
import org.noear.weed.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 此包装，更适合在PaaS里使用
 *
 * @author noear 2021/2/1 created
 */
public class MongoX {
    MongoClient client;
    MongoDatabase database;


    public MongoX(Properties props, String db) {
        List<ServerAddress> lists = new ArrayList<>();
        MongoClientOptions options = new MongoClientOptions.Builder().build();

        String server = props.getProperty("server");
        String source = props.getProperty("source");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        String[] serverAry = server.split(";");

        for (String sev : serverAry) {
            if (StringUtils.isNotEmpty(sev)) {
                lists.add(buildServer(sev));
            }
        }

        if (StringUtils.isNotEmpty(username)) {
            MongoCredential credential = MongoCredential.createCredential(username, source, password.toCharArray());
            client = new MongoClient(lists, credential, options);
        } else {
            client = new MongoClient(lists, options);
        }


        database = client.getDatabase(db);
    }

    public MongoX(String host, int port, String db) {
        if (port > 0) {
            client = new MongoClient(host, port);
        } else {
            client = new MongoClient(host);
        }

        database = client.getDatabase(db);
    }

    private ServerAddress buildServer(String sev) {
        String host;
        int port = 0;

        if (sev.contains(":")) {
            String[] ss = sev.split(":");
            host = ss[0];
            port = Integer.parseInt(ss[1]);
        } else {
            host = sev;
            port = 27017;
        }

        return new ServerAddress(host, port);
    }

    public MongoCollection<Document> getCollection(String coll) {
        return database.getCollection(coll);
    }

    public <T> MongoCollection<T> getCollection(String coll, Class<T> clz) {
        return database.getCollection(coll, clz);
    }

    public void insertOne(String coll, Map<String, Object> data) {
        MongoCollection<Document> collM = getCollection(coll);

        collM.insertOne(new Document(data));
    }

    public void insertMany(String coll, List<Map<String, Object>> dataList) {
        MongoCollection<Document> collM = getCollection(coll);
        List<Document> list = new ArrayList<>();

        for (Map data : dataList) {
            list.add(new Document(data));
        }

        collM.insertMany(list);
    }

    public long updateOne(String coll, Map<String, Object> filter, Map<String, Object> data) {
        MongoCollection<Document> collM = getCollection(coll);


        return collM.updateOne(new Document(filter), new Document(data)).getModifiedCount();
    }

    public long updateMany(String coll, Map<String, Object> filter, Map<String, Object> data) {
        MongoCollection<Document> collM = getCollection(coll);

        return collM.updateMany(new Document(filter), new Document(data)).getModifiedCount();
    }


    public long replaceOne(String coll, Map<String, Object> filter, Map<String, Object> data) {
        MongoCollection<Document> collM = getCollection(coll);

        return collM.replaceOne(new Document(filter), new Document(data)).getModifiedCount();
    }


    public Map<String, Object> findOne(String coll, Map<String, Object> filter) {
        FindIterable<Document> listM = find(coll, filter);
        listM.limit(1);

        for (Document item : listM) {
            return item;
        }

        return null;
    }

    public List<Map<String, Object>> findMany(String coll, Map<String, Object> filter, Map<String, Object> sort) {
        FindIterable<Document> cursor = find(coll, filter);

        if (sort != null && sort.size() > 0) {
            cursor.sort(new Document(sort));
        }

        List<Map<String, Object>> list = new ArrayList<>();

        for (Document item : cursor) {
            list.add(item);
        }

        return list;
    }

    public List<Map<String, Object>> findTop(String coll, Map<String, Object> filter, Map<String, Object> sort, int top) {
        return findPage(coll, filter, sort, 0, top);
    }

    /**
     * @param start 起始位
     * @param size  数量
     */
    public List<Map<String, Object>> findPage(String coll, Map<String, Object> filter, Map<String, Object> sort, int start, int size) {
        FindIterable<Document> cursor = find(coll, filter);

        if (start > 0) {
            cursor.skip(start);
        }

        if (size > 0) {
            cursor.limit(size);
        }

        if (sort != null && sort.size() > 0) {
            cursor.sort(new Document(sort));
        }

        List<Map<String, Object>> list = new ArrayList<>();

        for (Document item : cursor) {
            list.add(item);
        }

        return list;
    }


    public FindIterable<Document> find(String coll, Map<String, Object> filter) {
        MongoCollection<Document> collM = getCollection(coll);

        return collM.find(new Document(filter));
    }


    public long deleteOne(String coll, Map<String, Object> filter) {
        MongoCollection<Document> collM = getCollection(coll);

        return collM.deleteOne(new Document(filter)).getDeletedCount();
    }

    public long deleteMany(String coll, Map<String, Object> filter) {
        MongoCollection<Document> collM = getCollection(coll);

        return collM.deleteMany(new Document(filter)).getDeletedCount();
    }

    public long count(String coll) {
        MongoCollection<Document> collM = getCollection(coll);
        return collM.estimatedDocumentCount();
    }

    public long countDocuments(String coll, Map<String, Object> filter) {
        MongoCollection<Document> collM = getCollection(coll);

        if (filter == null || filter.size() == 0) {
            return collM.countDocuments();
        } else {
            return collM.countDocuments(new Document(filter));
        }
    }

    /**
     * 创建索引
     */
    public String createIndex(String coll, Map<String, Object> keys, Map<String, Object> options) {
        MongoCollection<Document> collM = getCollection(coll);

        if (options == null || options.size() == 0) {
            return collM.createIndex(new Document(keys));
        } else {
            DataItem dataItem = new DataItem().setMap(options);
            IndexOptions optionsM = dataItem.toEntity(IndexOptions.class);

            return collM.createIndex(new Document(keys), optionsM);
        }
    }

    public String createIndex(String coll, Map<String, Object> keys) {
        return createIndex(coll, keys);
    }
}
