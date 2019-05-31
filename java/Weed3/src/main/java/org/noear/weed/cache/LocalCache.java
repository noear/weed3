package org.noear.weed.cache;

import org.noear.weed.ext.Fun1;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yuety on 14-6-16.
 * 嵌入式缓存效率高；基于总量与超时双重管理(比自动超时要简单)
 *
 */
public class LocalCache implements ICacheServiceEx {
    private String _cacheKeyHead;
    private int _defaultSeconds;

    private int _max = 10000;
    private int _count = 0;

    private List<Integer> mks = new ArrayList<Integer>(); //key的顺序记录
    private Map<Integer, LocalCacheRecord> mcc = new ConcurrentHashMap<Integer, LocalCacheRecord>();   //缓存存储器

    public LocalCache(String keyHeader, int defSeconds) {
        this(keyHeader, defSeconds, 10000);
    }

    public LocalCache(String keyHeader, int defSeconds, int recordMax) {
        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;
        _max = recordMax;
    }

    @Override
    public void store(String key, Object obj, int seconds) {
        Integer hashKey = newKey(key);
        LocalCacheRecord val = new LocalCacheRecord(obj, seconds);

        mcc.put(hashKey, val);
        mks.add(hashKey);

        _count++;
        if (_count > _max) //总量控制
        {
            Integer k = mks.get(0);
            mcc.remove(k);
            mks.remove(0);
            _count--;
        }
    }

    @Override
    public Object get(String key) {
        Integer hashKey = newKey(key);
        LocalCacheRecord val = mcc.get(hashKey);

        if (val == null)
            return null;

        if (val.timeout < System.currentTimeMillis()) {
            mcc.remove(hashKey);
            mks.remove(hashKey);
            _count--;
            return null;
        } else
            return val.data;
    }

    @Override
    public void remove(String key) {
        Integer hashKey = newKey(key);

        mcc.remove(hashKey);
        mks.remove(hashKey);
        _count--;
    }

    public void clear() {
        mks.clear();
        mcc.clear();
        _count = 0;
    }

    private Integer newKey(String key){
        return (_cacheKeyHead + "$" + key).hashCode();
    }

    @Override
    public int getDefalutSeconds() {
        return _defaultSeconds;
    }

    @Override
    public String getCacheKeyHead() {
        return _cacheKeyHead;
    }

    //==================
    //
    @Override
    public CacheTags tags() {
        return new CacheTags(this);
    }
    @Override
    public void clear(String tag) {
        tags().clear(tag);
    }
    @Override
    public <T> void update(String tag, Fun1<T, T> setter) {
        tags().update(tag, setter);
    }
}
