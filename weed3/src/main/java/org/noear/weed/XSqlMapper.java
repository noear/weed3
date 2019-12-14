package org.noear.weed;

import org.noear.weed.xml.XmlSqlLoader;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

class XSqlMapper {
    private static String _lock = "";
    private static Map<Class<?>, Object> _cache = new HashMap<>();

    /**
     * 获取Mapper
     */
    public static <T> T get(Class<?> clz, DbContext db) {
        Object tmp = _cache.get(clz);
        if (tmp == null) {
            synchronized (_lock) {
                tmp = _cache.get(clz);
                if (tmp == null) {
                    tmp = do_get(clz, db);
                    _cache.put(clz, tmp);
                }
            }
        }

        return (T) tmp;
    }

    /**
     * 获取代理实例
     */
    private static <T> T do_get(Class<?> clz, DbContext db) {
        XmlSqlLoader.tryLoad();

        return (T) Proxy.newProxyInstance(
                clz.getClassLoader(),
                new Class[]{clz},
                new XSqlMapperHandler(db));
    }
}
