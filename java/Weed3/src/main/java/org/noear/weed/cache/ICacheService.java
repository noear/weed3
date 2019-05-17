package org.noear.weed.cache;

/**
 * Created by noear on 14-6-12.
 */
public interface ICacheService {

    void store(String key, Object obj, int seconds);

    Object get(String key);

    void remove(String key);

    int getDefalutSeconds();

    String getCacheKeyHead();
}
