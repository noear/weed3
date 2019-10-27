package org.noear.weed.cache;

/**
 * 对象序列化接口
 * */
public interface ISerializer<T> {
    /**
     * 名称
     * */
    String name();
    /**
     * 序列化
     * */
    T serialize(Object obj) throws Exception ;
    /**
     * 反序列化
     * */
    Object deserialize(T dta) throws Exception ;
}
