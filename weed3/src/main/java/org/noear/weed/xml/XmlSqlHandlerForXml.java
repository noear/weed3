package org.noear.weed.xml;

import org.noear.weed.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class XmlSqlHandlerForXml {
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

        //3.获取代码块，并检测有效性
        XmlSqlBlock block = XmlSqlFactory.get(xml_name);
        if (block == null) {
            throw new RuntimeException("Xmlsql does not exist:" + xml_name);
        }

        //4.生成命令
        DbProcedure sp = db.call("@" + xml_name).setMap(_map);

        //5.构建输出
        Class<?> rst_type = method.getReturnType();
        Type rst_type2 = method.getGenericReturnType();

        if (block.isSelect()) {
            if (block._return.indexOf(".") > 0) {
                //实体化处理
                if (Collection.class.isAssignableFrom(rst_type)) {
                    //是实体集合
                    rst_type2 = ((ParameterizedType) rst_type2).getActualTypeArguments()[0];

                    if (IBinder.class.isAssignableFrom(rst_type)) {
                        return sp.getList((IBinder) rst_type.newInstance());
                    } else {
                        return sp.getList((Class<?>) rst_type2);
                    }
                } else {
                    //是单实体
                    if (IBinder.class.isAssignableFrom(rst_type)) {
                        return sp.getItem((IBinder) rst_type.newInstance());
                    } else {
                        return sp.getItem(rst_type);
                    }
                }
            } else {
                if (block._return.startsWith("List<")) {
                    return sp.getDataList().toArray(0);
                }

                //普通职处理
                switch (block._return) {
                    case "Map":
                        return sp.getMap();
                    case "MapList":
                        return sp.getMapList();
                    case "DataItem":
                        return sp.getDataItem();
                    case "DataList":
                        return sp.getDataList();
                    default: {
                        Variate val = sp.getVariate();

                        if (val.getValue() == null) {
                            return 0;
                        }

                        //解决 BigDecimal BigInteger 问题
                        if (block._return.toLowerCase().startsWith("int")) {
                            return val.intValue(0);
                        }

                        if (block._return.toLowerCase().startsWith("long")) {
                            return val.longValue(0);
                        }

                        if (block._return.toLowerCase().startsWith("doub")) {
                            return val.doubleValue(0);
                        }

                        if (block._return.toLowerCase().startsWith("str")) {
                            return val.stringValue(null);
                        }

                        return val.getValue();
                    }
                }
            }
        } else {
            if (block.isInsert()) {
                long rst = sp.insert();
                if (Boolean.class.isAssignableFrom(rst_type)) {
                    return rst > 0;
                } else {
                    return rst;
                }
            } else {
                int rst = sp.execute();
                if (Boolean.class.isAssignableFrom(rst_type)) {
                    return rst > 0;
                } else {
                    return rst;
                }
            }
        }
    }
}
