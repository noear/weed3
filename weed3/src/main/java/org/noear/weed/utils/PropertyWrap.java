package org.noear.weed.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyWrap {
    private static Map<String,Class<?>> _clzCache = new ConcurrentHashMap<>();
    private static Class<?> getClz(String implClz){
        Class<?> clz = _clzCache.get(implClz);
        if(clz == null){
            try {
                clz = Class.forName(implClz.replace("/", "."));
                _clzCache.putIfAbsent(implClz,clz);
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }
        return clz;
    }

    public final ClassWrap clzWrap;
    public final String name;


    public PropertyWrap(String implClz, String name){
        this.clzWrap = ClassWrap.get(getClz(implClz));
        this.name = name;
    }
}
