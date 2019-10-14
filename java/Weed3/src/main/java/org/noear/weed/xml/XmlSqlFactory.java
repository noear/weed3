package org.noear.weed.xml;

import org.noear.weed.DbContext;
import org.noear.weed.DbQuery;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XmlSqlFactory {
    private static Map<String, IXmlSqlBuilder> _sqlMap = new ConcurrentHashMap<>();

    public static void register(String name, IXmlSqlBuilder xmlSqlBuilder) {
        _sqlMap.put(name, xmlSqlBuilder);
    }

    public static IXmlSqlBuilder get(String name){
        return _sqlMap.get(name);
    }

    public static DbQuery call(DbContext db, String name, Map map) {
        IXmlSqlBuilder xmlSqlBuilder = _sqlMap.get(name);
        if (xmlSqlBuilder != null) {
            return db.sql(xmlSqlBuilder.build(map));
        }

        return null;
    }
}
