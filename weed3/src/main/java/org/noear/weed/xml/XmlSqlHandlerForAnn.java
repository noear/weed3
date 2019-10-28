package org.noear.weed.xml;

import org.noear.weed.*;
import org.noear.weed.annotation.Sql;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class XmlSqlHandlerForAnn {
    public static Object forAnn(Object proxy, Method method, Object[] vals, Sql ann) throws Throwable {
        Class<?> clazz = method.getDeclaringClass();
        DbContext db = WeedConfig.libOfDb.get(clazz);

        String sqlUp = "# " + ann.value().toUpperCase();
        DbProcedure sp = db.call(ann.value());

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

        sp.setMap(_map);

        if (sqlUp.indexOf(" DELETE ") > 0 || sqlUp.indexOf(" UPDATE ") > 0) {
            return sp.execute();
        }

        if (sqlUp.indexOf(" INSERT ") > 0) {
            return sp.insert();
        }

        if (sqlUp.indexOf(" SELECT ") > 0) {
            //5.构建输出
            return forSelect(sp, method, ann);

        }

        return null;
    }

    private static Object forSelect(DbProcedure sp, Method method, Sql ann) throws Throwable {
        Class<?> rst_type = method.getReturnType();
        Type rst_type2 = method.getGenericReturnType();

        String rst_type_str = rst_type.getName();
        String rst_type2_str = null;

        if (DataItem.class.isAssignableFrom(rst_type)) {
            return sp.getDataItem();
        }

        if (DataList.class.isAssignableFrom(rst_type)) {
            return sp.getDataList();
        }

        if (Map.class.isAssignableFrom(rst_type)) {
            return sp.getMap();
        }

        if (Collection.class.isAssignableFrom(rst_type)) {
            //是实体集合
            //
            rst_type2 = ((ParameterizedType) rst_type2).getActualTypeArguments()[0];
            rst_type2_str = rst_type2.getTypeName();

            if (rst_type2_str.startsWith("java.") == false) {
                //
                //list<Model>
                //
                if (IBinder.class.isAssignableFrom(rst_type)) {
                    return sp.getList((IBinder) rst_type.newInstance());
                } else {
                    return sp.getList((Class<?>) rst_type2);
                }
            } else {
                //list<Map>
                if (rst_type2_str.indexOf("Map") > 0) {
                    return sp.getMapList();
                } else {
                    //list<Object>
                    return sp.getDataList().toArray(0);
                }
            }
        }

        //是单实体
        if (rst_type_str.startsWith("java") == false && rst_type_str.indexOf(".") > 0) {
            if (IBinder.class.isAssignableFrom(rst_type)) {
                return sp.getItem((IBinder) rst_type.newInstance());
            } else {
                return sp.getItem(rst_type);
            }
        }

        Variate val = sp.getVariate();

        if (Long.class == (rst_type) || rst_type == Long.TYPE) {
            return val.longValue(0);
        }

        if (Integer.class == (rst_type) || rst_type == Integer.TYPE) {
            return val.intValue(0);
        }

        return val.getValue();
    }
}
