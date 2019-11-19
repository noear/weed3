package org.noear.weed.cache;

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
    private static ScheduledExecutorService _exec = Executors.newSingleThreadScheduledExecutor(); //计划线程池（用于超时处理）

    public LocalCache(String keyHeader, int defSeconds) {
        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;
    }

    @Override
    public void store(String key, Object obj, int seconds) {
        synchronized (key.intern()) {
            Entity val = _data.get(key);
            if (val != null) {
                //如果已存储，取消超时处理
                if (val.future != null) {
                    val.future.cancel(true);
                    val.future = null;
                }
            } else {
                //如果末存在
                val = new Entity(obj);
                _data.put(key, val);
            }

            if (seconds > 0) {
                //设定新的超时
                val.future = _exec.schedule(() -> {
                    _data.remove(key);
                }, seconds, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public Object get(String key) {
        Entity val = _data.get(key);

        return val == null ? null : val.value;
    }

    @Override
    public void remove(String key) {
        synchronized (key.intern()) {
            Entity val = _data.remove(key);

            if (val != null) {
                if (val.future != null) {
                    val.future.cancel(true);
                    val.future = null;
                }
            }
        }
    }

    public void clear() {
        for (Entity val : _data.values()) {
            //尝试取消超时
            if (val.future != null) {
                val.future.cancel(true);
                val.future = null;
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

    
    //存储实体
    private static class Entity {
        public Object value;
        public Future future;

        public Entity(Object val) {
            this.value = val;
        }
    }
}
