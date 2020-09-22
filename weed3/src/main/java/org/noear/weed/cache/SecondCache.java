package org.noear.weed.cache;

import org.noear.weed.ext.Fun1;

/**
 * 二级缓存容器
 *
 * @author noear
 * @since 3.0
 * */
public class SecondCache implements ICacheServiceEx {
    private ICacheServiceEx cache1;
    private ICacheServiceEx cache2;
    private int bufferSeconds;

    public SecondCache(ICacheServiceEx cache1, ICacheServiceEx cache2) {
        this(cache1, cache2, 5);
    }

    public SecondCache(ICacheServiceEx cache1, ICacheServiceEx cache2, int bufferSeconds) {
        this.cache1 = cache1;
        this.cache2 = cache2;
        this.bufferSeconds = bufferSeconds;
    }

    @Override
    public void store(String key, Object obj, int seconds) {
        cache1.store(key, obj, seconds);
        cache2.store(key, obj, seconds);
    }

    @Override
    public Object get(String key) {
        Object temp = cache1.get(key);
        if (temp == null) {
            temp = cache2.get(key);
            if (bufferSeconds > 0 && temp != null) {
                cache1.store(key, temp, bufferSeconds);
            }
        }
        return temp;
    }

    @Override
    public void remove(String key) {
        cache1.remove(key);
        cache2.remove(key);
    }

    @Override
    public int getDefalutSeconds() {
        return cache1.getDefalutSeconds();
    }

    @Override
    public String getCacheKeyHead() {
        return cache1.getCacheKeyHead();
    }

    //////////

    @Override
    public CacheTags tags() {
        return cache1.tags();
    }

    @Override
    public void clear(String tag) {
        cache1.clear(tag);
        cache2.clear(tag);
    }

    @Override
    public <T> void update(String tag, Fun1<T, T> setter) {
        cache1.update(tag, setter);
        cache2.update(tag, setter);
    }
}
