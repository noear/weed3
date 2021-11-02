package org.noear.weed.mongo;

import com.mongodb.client.*;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.noear.weed.DataItem;
import org.noear.weed.utils.StringUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 此包装，更适合在PaaS里使用
 *
 * @author noear 2021/2/1 created
 */
public class MongoX implements AutoCloseable {
    MongoClient client;
    MongoDatabase database;


    public MongoX(Properties props, String schema) {
        this(props.getProperty("url"), schema);
    }

    public MongoX(String url, String schema) {
        if (StringUtils.isEmpty(schema)) {
            throw new IllegalArgumentException("MongoX: Missing schema(db) configuration");
        }

        client = MongoClients.create(url);
        database = client.getDatabase(schema);
    }

    public MongoCollection<Document> getCollection(String coll) {
        return database.getCollection(coll);
    }

    public <T> MongoCollection<T> getCollection(String coll, Class<T> clz) {
        return database.getCollection(coll, clz);
    }


    public void insertOne(Class<?> coll, Map<String, Object> data) {
        insertOne(coll.getSimpleName(), data);
    }

    public void insertOne(String coll, Map<String, Object> data) {
        MongoCollection<Document> collM = getCollection(coll);

        collM.insertOne(new Document(data));
    }

    public void insertMany(Class<?> coll, List<Map<String, Object>> dataList) {
        insertMany(coll.getSimpleName(), dataList);
    }

    public void insertMany(String coll, List<Map<String, Object>> dataList) {
        MongoCollection<Document> collM = getCollection(coll);
        List<Document> list = new ArrayList<>();

        for (Map data : dataList) {
            list.add(new Document(data));
        }

        collM.insertMany(list);
    }

    public long updateOne(Class<?> coll, Map<String, Object> filter, Map<String, Object> data) {
        return updateOne(coll.getSimpleName(), filter, data);
    }

    public long updateOne(String coll, Map<String, Object> filter, Map<String, Object> data) {
        MongoCollection<Document> collM = getCollection(coll);

        Document newData = new Document();
        newData.put("$set", data);


        return collM.updateOne(new Document(filter), newData).getModifiedCount();
    }

    public long updateMany(Class<?> coll, Map<String, Object> filter, Map<String, Object> data) {
        return updateMany(coll.getSimpleName(), filter, data);
    }

    public long updateMany(String coll, Map<String, Object> filter, Map<String, Object> data) {
        MongoCollection<Document> collM = getCollection(coll);

        Document newData = new Document();
        newData.put("$set", data);

        return collM.updateMany(new Document(filter), newData).getModifiedCount();
    }

    public long replaceOne(Class<?> coll, Map<String, Object> filter, Map<String, Object> data) {
        return replaceOne(coll, filter, data);
    }

    public long replaceOne(String coll, Map<String, Object> filter, Map<String, Object> data) {
        MongoCollection<Document> collM = getCollection(coll);

        return collM.replaceOne(new Document(filter), new Document(data)).getModifiedCount();
    }

    public Document findOne(Class<?> coll, Map<String, Object> filter) {
        return findOne(coll.getSimpleName(), filter);
    }

    public Document findOne(String coll, Map<String, Object> filter) {
        FindIterable<Document> listM = find(coll, filter);
        listM.limit(1);

        for (Document item : listM) {
            return item;
        }

        return null;
    }

    public List<Document> findMany(Class<?> coll, Map<String, Object> filter, Map<String, Object> sort) {
        return findMany(coll.getSimpleName(), filter, sort);
    }

    public List<Document> findMany(String coll, Map<String, Object> filter, Map<String, Object> sort) {
        FindIterable<Document> cursor = find(coll, filter);

        if (sort != null && sort.size() > 0) {
            cursor.sort(new Document(sort));
        }

        List<Document> list = new ArrayList<>();

        for (Document item : cursor) {
            list.add(item);
        }

        return list;
    }

    public List<Document> findTop(Class<?> coll, Map<String, Object> filter, Map<String, Object> sort, int top) {
        return findTop(coll.getSimpleName(), filter, sort, top);
    }

    public List<Document> findTop(String coll, Map<String, Object> filter, Map<String, Object> sort, int top) {
        return findPage(coll, filter, sort, 0, top);
    }

    public List<Document> findPage(Class<?> coll, Map<String, Object> filter, Map<String, Object> sort, int start, int size) {
        return findPage(coll.getSimpleName(), filter, sort, start, size);
    }

    /**
     * @param start 起始位
     * @param size  数量
     */
    public List<Document> findPage(String coll, Map<String, Object> filter, Map<String, Object> sort, int start, int size) {
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

        List<Document> list = new ArrayList<>();

        for (Document item : cursor) {
            list.add(item);
        }

        return list;
    }


    public FindIterable<Document> find(Class<?> coll, Map<String, Object> filter) {
        return find(coll.getSimpleName(), filter);
    }

    public FindIterable<Document> find(String coll, Map<String, Object> filter) {
        MongoCollection<Document> collM = getCollection(coll);

        return collM.find(new Document(filter));
    }

    public long deleteOne(Class<?> coll, Map<String, Object> filter) {
        return deleteOne(coll.getSimpleName(), filter);
    }

    public long deleteOne(String coll, Map<String, Object> filter) {
        MongoCollection<Document> collM = getCollection(coll);

        return collM.deleteOne(new Document(filter)).getDeletedCount();
    }

    public long deleteMany(Class<?> coll, Map<String, Object> filter) {
        return deleteMany(coll.getSimpleName(), filter);
    }

    public long deleteMany(String coll, Map<String, Object> filter) {
        MongoCollection<Document> collM = getCollection(coll);

        return collM.deleteMany(new Document(filter)).getDeletedCount();
    }

    public long count(Class<?> coll) {
        return count(coll.getSimpleName());
    }

    public long count(String coll) {
        MongoCollection<Document> collM = getCollection(coll);
        return collM.estimatedDocumentCount();
    }

    public long countDocuments(Class<?> coll, Map<String, Object> filter) {
        return countDocuments(coll.getSimpleName(), filter);
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

    public String createIndex(String coll, Map<String, Object> keys, IndexOptions options) {
        MongoCollection<Document> collM = getCollection(coll);

        return collM.createIndex(new Document(keys), options);
    }

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

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }
}
