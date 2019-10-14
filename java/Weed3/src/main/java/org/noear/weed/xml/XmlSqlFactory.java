package org.noear.weed.xml;

import org.noear.weed.DbContext;
import org.noear.weed.DbQuery;
import org.noear.weed.SQLBuilder;

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
}
