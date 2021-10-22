package org.noear.weed.elasticsearch;

import org.noear.weed.utils.Base64Utils;
import org.noear.weed.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * ElasticSearch 上下文（只支持 7.x +）
 *
 * @author noear
 */
public class EsContext {
    private final String[] urls;
    private int urlIndex;
    private final String username;
    private final String paasword;

    public EsContext(Properties prop) {
        this(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("paasword"));
    }

    public EsContext(String url) {
        this(url, null, null);
    }

    public EsContext(String url, String username, String paasword) {
        this.username = username;
        this.paasword = paasword;

        List<String> urlAry = new ArrayList<>();
        for (String ser : url.split(",")) {
            if (ser.contains("://")) {
                urlAry.add(ser);
            } else {
                urlAry.add("http://" + ser);
            }
        }
        this.urls = urlAry.toArray(new String[urlAry.size()]);
    }

    private String getUrl() {
        if (urls.length == 0) {
            return urls[0];
        } else {
            if (urlIndex > 10000000) {
                urlIndex = 0;
            }

            return urls[urlIndex % urls.length];
        }
    }

    protected HttpUtils getHttp(String path) {
        HttpUtils http = HttpUtils.http(getUrl() + path);

        if (StringUtils.isNotEmpty(username)) {
            String token = Base64Utils.encode(username + ":" + paasword);
            String auth = "Basic " + token;

            http.header("Authorization", auth);
        }

        return http;
    }

    /**
     * 获取表操作
     */
    public EsTableQuery table(String table) {
        return new EsTableQuery(this, table);
    }

    /**
     * 表创建
     *
     * @param indiceName 索引名字
     */
    public String tableCreate(String indiceName, String dsl) throws IOException {
        HttpUtils http = getHttp(String.format("/%s", indiceName));

        String tmp = http.bodyTxt(dsl, EsTableQuery.mime_json).put();
        //return: {"acknowledged":true,"shards_acknowledged":true,"index":"water$water_log_api_202110"}

        return tmp;
    }

    /**
     * 表是否存在
     *
     * @param indiceName 索引名字
     */
    public boolean tableExist(String indiceName) throws IOException {
        int tmp = getHttp(String.format("/%s", indiceName)).head();

        return tmp == 200; //404不存在
    }

    /**
     * 表删除
     *
     * @param indiceName 索引名字
     */
    public String tableDrop(String indiceName) throws IOException {
        String tmp = getHttp(String.format("/%s", indiceName)).delete();

        return tmp;
    }

    /**
     * 表结构获取
     *
     * @param indiceName 索引名字
     */
    public String tableGet(String indiceName) throws IOException {
        String tmp = getHttp(String.format("/%s", indiceName)).get();

        return tmp;
    }
}
