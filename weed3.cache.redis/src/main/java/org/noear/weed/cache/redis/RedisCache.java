package org.noear.weed.cache.redis;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.ISerializer;
import org.noear.weed.utils.EncryptUtils;

public class RedisCache implements ICacheServiceEx {
    private String _cacheKeyHead;
    private int _defaultSeconds;

    private RedisX _cache = null;
    private ISerializer<String> _serializer = null;

    public RedisCache(String keyHeader, int defSeconds, String server, String password, int db, int maxTotaol) {
        this(keyHeader, defSeconds, server, password, db, maxTotaol, Snack3Serializer.instance);
    }

    public RedisCache(String keyHeader, int defSeconds, String server, String password, int db, int maxTotaol, ISerializer<String> serializer) {
        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;

        _cache = new RedisX(server, password, db, maxTotaol);
        _serializer = serializer;
    }

    @Override
    public void store(String key, Object obj, int seconds) {
        if (_cache != null) {
            String newKey = newKey(key);
            try {
                String val = _serializer.serialize(obj);
                _cache.open0((ru) -> ru.key(newKey).expire(seconds).set(val));

            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public Object get(String key) {
        if (_cache != null) {
            String newKey = newKey(key);
            String val = _cache.open1((ru) -> ru.key(newKey).get());
            try {
                return _serializer.deserialize(val);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        } else {
            return null;
        }
    }

    @Override
    public void remove(String key) {
        if (_cache != null) {
            String newKey = newKey(key);
            _cache.open0((ru) -> {
                ru.key(newKey).delete();
            });
        }
    }

    @Override
    public int getDefalutSeconds() {
        return _defaultSeconds;
    }

    @Override
    public String getCacheKeyHead() {
        return _cacheKeyHead;
    }

    private String newKey(String key) {
        return _cacheKeyHead + "$" + EncryptUtils.md5(key);
    }
}
