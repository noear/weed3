package org.noear.weed.utils;

import org.noear.weed.DataItem;
import org.noear.weed.ext.Act2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassWrap {
    private static Map<Class<?>, ClassWrap> _cache = new ConcurrentHashMap<>();

    public static ClassWrap get(Class<?> clz) {
        ClassWrap clzWrap = _cache.get(clz);
        if (clzWrap == null) {
            clzWrap = new ClassWrap(clz);
            _cache.put(clz, clzWrap);
        }

        return clzWrap;
    }


    public final Class<?> clazz;
    public final List<FieldWrap> fieldWraps;

    protected ClassWrap(Class<?> clz) {
        clazz = clz;
        fieldWraps = new ArrayList<>();

        Field[] fAry = clz.getDeclaredFields();
        for (Field f1 : fAry) {
            fieldWraps.add(new FieldWrap(clz, f1));
        }
    }

    //将 data 转为 entity
    public <T> T toEntity(DataItem data) {
        try {
            T item = (T) clazz.newInstance();

            for (FieldWrap fw : fieldWraps) {
                //转入时，不排除; 交dataItem检查
                if (data.exists(fw.name)) {
                    fw.setValue(item, data.get(fw.name));
                }
            }

            return item;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void fromEntity(Object obj, Act2<String, Object> setter) {
        try {
            for (FieldWrap fw : fieldWraps) {
                if (!fw.exclude) {
                    //转出时，进行排除
                    setter.run(fw.name, fw.getValue(obj));
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
