package org.noear.weed.xml;

import org.noear.weed.DbContext;
import org.noear.weed.DbQuery;
import org.noear.weed.SQLBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XmlSqlFactory {
    private static Map<String, XmlSqlBlock> _sqlMap = new ConcurrentHashMap<>();

    public static void register(String name, XmlSqlBlock sqlBlock) {
        _sqlMap.put(name, sqlBlock);
    }

    public static void register(String name, IXmlSqlBuilder xmlSqlBuilder) {
        XmlSqlBlock tmp = _sqlMap.get(name);

        if (tmp != null) {
            tmp.builder = xmlSqlBuilder;
        }
    }

    public static XmlSqlBlock get(String name){
        return _sqlMap.get(name);
    }
}
