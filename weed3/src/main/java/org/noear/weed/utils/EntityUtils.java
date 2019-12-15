package org.noear.weed.utils;

import org.noear.weed.DataItem;
import org.noear.weed.annotation.Exclude;
import org.noear.weed.ext.Act2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityUtils {
    private static Map<Class<?>, List<FieldWrap>> _clzFieldLib = new ConcurrentHashMap<>();

    private static List<FieldWrap> getFieldWraps(Class<?> clz) {
        List<FieldWrap> fields = _clzFieldLib.get(clz);
        if (fields == null) {
            fields = new ArrayList<>();

            Field[] fAry = clz.getDeclaredFields();
            for (Field f1 : fAry) {
                if (f1.getAnnotation(Exclude.class) == null) {
                    fields.add(new FieldWrap(clz, f1));
                }
            }
            _clzFieldLib.put(clz, fields);
        }

        return fields;
    }

    public static void fromEntity(Object obj, Act2<String, Object> setter) {
        try {
            List<FieldWrap> fields = getFieldWraps(obj.getClass());

            for (FieldWrap f : fields) {
                setter.run(f.getName(), f.getValue(obj));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static <T> T toEntity(Class<T> clz, DataItem data) {
        try {
            List<FieldWrap> fields = getFieldWraps(clz);

            String key = null;
            T item = clz.newInstance();

            for (FieldWrap fw : fields) {
                key = fw.getName();

                if (data.exists(key)) {
                    fw.setValue(item, data.get(key));
                }
            }

            return item;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
