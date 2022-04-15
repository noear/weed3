package org.noear.weed.cache.redis;

import org.noear.redisx.RedisClient;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.ISerializer;
import org.noear.weed.utils.EncryptUtils;
import org.noear.weed.utils.StringUtils;

import java.util.Properties;

public class RedisCache implements ICacheServiceEx {
    private String _cacheKeyHead;
    private int _defaultSeconds;

    private RedisClient _cache = null;
    private ISerializer<String> _serializer = null;

    public RedisCache serializer(ISerializer<String> serializer) {
        if (serializer != null) {
            this._serializer = serializer;
        }

        return this;
    }

    public RedisCache(String keyHeader, int defSeconds, String server, String user, String password) {
        Properties prop = new Properties();
        prop.setProperty("server", server);

        if (user != null) {
            prop.setProperty("user", user);
        }

        if (password != null) {
            prop.setProperty("password", password);
        }

        initDo(prop, keyHeader, defSeconds);
    }

    public RedisCache(Properties prop) {
        initDo(prop, prop.getProperty("keyHeader"), 0);
    }

    public RedisCache(Properties prop, String keyHeader, int defSeconds) {
        initDo(prop, keyHeader, defSeconds);
    }

    private void initDo(Properties prop, String keyHeader, int defSeconds) {
        String defSeconds_str = prop.getProperty("defSeconds");

        if (defSeconds == 0) {
            if (StringUtils.isEmpty(defSeconds_str) == false) {
                defSeconds = Integer.parseInt(defSeconds_str);
            }
        }


        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;

        if (_defaultSeconds < 1) {
            _defaultSeconds = 30;
        }

        _cache = new RedisClient(prop);
        _serializer = JavabinSerializer.instance;
    }

    @Override
    public void store(String key, Object obj, int seconds) {
        if (_cache != null) {
            String newKey = newKey(key);

            try {
                String val = _serializer.serialize(obj);

                if (seconds > 0) {
                    _cache.open((ru) -> ru.key(newKey).expire(seconds).set(val));
                } else {
                    _cache.open((ru) -> ru.key(newKey).expire(getDefalutSeconds()).set(val));
                }
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
            String val = _cache.openAndGet((ru) -> ru.key(newKey).get());
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
            _cache.open((ru) -> {
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
