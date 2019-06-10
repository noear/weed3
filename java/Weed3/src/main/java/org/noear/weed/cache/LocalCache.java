package org.noear.weed.cache;

import org.noear.weed.ext.Fun1;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by noear on 19-06-05
 * 嵌入式缓存效率高；
 *
 */
public class LocalCache implements ICacheServiceEx {
    private String _cacheKeyHead;
    private int _defaultSeconds;

    private Map<String, Entity>             _data = new ConcurrentHashMap<>();   //缓存存储器
    private static ScheduledExecutorService _exec = Executors.newSingleThreadScheduledExecutor(); //线程池

    public LocalCache(String keyHeader, int defSeconds) {
        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;
    }

    @Override
    public void store(String key, Object obj, int seconds) {
        remove(key);

        Entity val = new Entity(obj);
        if(seconds>0){
            val.future = _exec.schedule(()->{
                _data.remove(key);
            },seconds, TimeUnit.SECONDS);
        }

        _data.put(key, val);
    }

    @Override
    public Object get(String key) {
        Entity val = _data.get(key);

        return val == null ? null : val.value;
    }

    @Override
    public void remove(String key) {
        Entity val = _data.remove(key);
        if (val != null) {
            if (val.future != null) {
                val.future.cancel(true);
            }
        }
    }

    public void clear() {
        for (Entity val : _data.values()) {
            if (val.future != null) {
                val.future.cancel(true);
            }
        }

        _data.clear();
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



    
    //存储实体
    private static class Entity {
        public Object value;
        public Future future;

        public Entity(Object val) {
            this.value = val;
        }
    }
}
