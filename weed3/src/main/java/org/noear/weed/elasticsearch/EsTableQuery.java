package org.noear.weed.elasticsearch;

import org.noear.snack.ONode;
import org.noear.weed.model.Page;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author noear 2021/10/22 created
 */
public class EsTableQuery {
    protected static final String mime_json = "application/json";
    protected static final String mime_ndjson = "application/x-ndjson";

    private final EsContext context;
    private final String table;

    private ONode query;
    private ONode queryWhere;
    private ONode item;

    protected EsTableQuery(EsContext context, String table) {
        this.context = context;
        this.table = table;
    }

    private HttpUtils getHttp(String path) {
        return context.getHttp(path);
    }

    private ONode getQuery() {
        if (query == null) {
            query = new ONode().asObject();
        }

        return query;
    }

    private ONode getQueryWhere() {
        if (queryWhere == null) {
            queryWhere = new ONode().asObject();
        }

        return query;
    }


    public EsTableQuery set(String field, Object value) {
        if (item == null) {
            item = new ONode();
        }

        item.set(field, value);
        return this;
    }

    //
    // insert
    //

    private String insertDo(ONode doc) throws IOException {
        String docJson = doc.toJson();

        HttpUtils http = getHttp(String.format("/%s/_doc/", table));

        String tmp = http.bodyTxt(docJson, mime_json).post(); //需要 post
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }

    private String upsertDo(String docId, ONode doc) throws IOException {
        String docJson = doc.toJson();

        HttpUtils http = getHttp(String.format("/%s/_doc/%s", table, docId));

        String tmp = http.bodyTxt(docJson, mime_json).put(); //需要 put
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }

    /**
     * 插入
     * */
    public String insert() throws IOException {
        return insertDo(item);
    }

    public <T> String insert(T doc) throws IOException {
        return insertDo(ONode.loadObj(doc));
    }


    public <T> String insertList(List<T> docs) throws IOException {
        StringBuilder docJson = new StringBuilder();
        docs.forEach((doc) -> {
            docJson.append(new ONode().build(n -> n.getOrNew("index").asObject()).toJson()).append("\n");
            docJson.append(ONode.loadObj(doc).toJson()).append("\n");
        });

        HttpUtils http = getHttp(String.format("/%s/_doc/_bulk", table));

        String tmp = http.bodyTxt(docJson.toString(), mime_ndjson).post();
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }


    public String upsert(String docId) throws IOException {
        return upsertDo(docId, item);
    }

    public <T> String upsert(String docId, T doc) throws IOException {
        return upsertDo(docId, ONode.loadObj(doc));
    }

    public <T> String upsertList(Map<String,T> docs) throws IOException {
        StringBuilder docJson = new StringBuilder();
        docs.forEach((docId, doc) -> {
            docJson.append(new ONode().build(n -> n.getOrNew("index").set("_id", docId)).toJson()).append("\n");
            docJson.append(ONode.loadObj(doc).toJson()).append("\n");
        });

        HttpUtils http = getHttp(String.format("/%s/_doc/_bulk", table));

        String tmp = http.bodyTxt(docJson.toString(), mime_ndjson).post(); //需要 post
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }

    //
    // delete
    //
    public String deleteByIds(String... docIds) throws IOException {
        String docIdsStr = String.join(",", docIds);

        HttpUtils http = getHttp(String.format("/%s/_doc/%s", table, docIdsStr));
        String tmp = http.delete();

        return tmp;
    }

    //
    // selectById
    //

    public <T> T selectById(String docId, Class<?> clz) throws IOException {
        String tmp = getHttp(String.format("/%s/_doc/%s", table, docId)).get();

        return ONode.loadStr(tmp).toObject(clz);
    }

    //
    // select
    //

    public EsTableQuery where(String field, Object value) {
        getQueryWhere().set(field, value);
        return this;
    }

    public EsTableQuery and(String field, Object value) {
        getQueryWhere().set(field, value);
        return this;
    }

    public EsTableQuery limit(int start, int size) {
        getQuery().set("from", start);
        getQuery().set("size", size);
        return this;
    }

    public EsTableQuery orderByAsc(String field) {
        getQuery().getOrNew("sort").getOrNew(field).set("order", "asc");
        return this;
    }

    public EsTableQuery orderByDesc(String field) {
        getQuery().getOrNew("sort").getOrNew(field).set("order", "desc");
        return this;
    }

    public EsTableQuery andByAsc(String field) {
        getQuery().getOrNew("sort").getOrNew(field).set("order", "asc");
        return this;
    }

    public EsTableQuery andByDesc(String field) {
        getQuery().getOrNew("sort").getOrNew(field).set("order", "desc");
        return this;
    }


    public <T> Page<T> select(Class<T> clz) throws IOException {
        if (queryWhere != null) {
            if (queryWhere.count() > 1) {
                getQuery().set("multi_match", queryWhere);
            } else {
                getQuery().set("match", queryWhere);
            }
        }

        String dsl = getQuery().toJson();
        String json = getHttp(String.format("/%s/_search", table)).bodyTxt(dsl, mime_json).post();

        ONode oHits = ONode.loadStr(json).get("hits");

        long total = oHits.get("total").get("value").getLong();
        List<T> list = oHits.get("hits").toObjectList(clz);

        return new Page<>(total, list);
    }
}
