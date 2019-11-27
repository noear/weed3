package org.noear.weed;

import org.noear.weed.xml.Namespace;
import org.noear.weed.xml.XmlSqlBlock;
import org.noear.weed.xml.XmlSqlFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

class XSqlHandlerForXml {
    public static Object forXml(Object proxy, Method method, Object[] vals) throws Throwable {
        //1.构建xml namme
        Class<?> clazz = method.getDeclaringClass();
        DbContext db = WeedConfig.libOfDb.get(clazz);

        Namespace c_meta = clazz.getAnnotation(Namespace.class);
        String fun_name = method.getName();

        String xml_name = null;
        if (c_meta == null) {
            xml_name = clazz.getPackage().getName() + "." + fun_name;
        } else {
            xml_name = c_meta.value() + "." + fun_name;
        }

        //2.构建参数
        Map<String, Object> _map = new LinkedHashMap<>();
        Parameter[] names = method.getParameters();
        for (int i = 0, len = names.length; i < len; i++) {
            if (vals[i] != null) {
                String key = names[i].getName();
                Object val = vals[i];

                //如果是_map参数，则做特殊处理
                if ("_map".equals(key) && val instanceof Map) {
                    _map.putAll((Map<String, Object>) val);
                } else {
                    _map.put(key, val);
                }
            }
        }

        Class<?> type1 = method.getReturnType();
        Type     type2 = method.getGenericReturnType();
        return XSqlUtil.exec(db,"@" + xml_name, _map, type1, type2);
    }
}
