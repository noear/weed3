package org.noear.weed.cache.j2cache;

import net.oschina.j2cache.CacheChannel;
import org.noear.weed.cache.ICacheServiceEx;

public class J2Cache implements ICacheServiceEx {
    private String _cacheKeyHead;
    private int _defaultSeconds;
    private CacheChannel _cache;

    public J2Cache(String keyHeader, int defSeconds) {
        this(keyHeader,defSeconds,net.oschina.j2cache.J2Cache.getChannel());
    }

    public J2Cache(String keyHeader, int defSeconds,  CacheChannel cacheChannel) {
        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;

        _cache = cacheChannel;

        if (_defaultSeconds < 1) {
            _defaultSeconds = 30;
        }
    }

    @Override
    public void store(String key, Object obj, int seconds) {
        if (seconds <= 0) {
            seconds = getDefalutSeconds();
        }

        _cache.set(_cacheKeyHead, key, obj, seconds);
    }

    @Override
    public Object get(String key) {
        return _cache.get(_cacheKeyHead, key).getValue();
    }

    @Override
    public void remove(String key) {
        _cache.evict(_cacheKeyHead, key);
    }

    @Override
    public int getDefalutSeconds() {
        return _defaultSeconds;
    }

    @Override
    public String getCacheKeyHead() {
        return _cacheKeyHead;
    }
}
