package org.noear.weed.utils;

public class PropertyWrap {
    public final ClassWrap clzWrap;
    public final String name;

    public PropertyWrap(Class<?> clz, String name){
        this.clzWrap = ClassWrap.get(clz);
        this.name = name;
    }
}
