package org.noear.weed.cache.redis;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.ISerializer;
import org.noear.weed.utils.EncryptUtils;

import java.util.Properties;

public class RedisCache implements ICacheServiceEx {
    private String _cacheKeyHead;
    private int _defaultSeconds;

    private RedisX _cache = null;
    private ISerializer<String> _serializer = null;

    public RedisCache serializer(ISerializer<String> serializer) {
        if(serializer != null) {
            this._serializer = serializer;
        }

        return this;
    }

    public RedisCache(Properties prop) {
        this(prop, prop.getProperty("keyHeader"), 0);
    }

    public RedisCache(Properties prop, String keyHeader, int defSeconds) {
        String defSeconds_str = prop.getProperty("defSeconds");
        String server = prop.getProperty("server");
        String password = prop.getProperty("password");
        String db_str = prop.getProperty("db");
        String maxTotaol_str = prop.getProperty("maxTotaol");

        if (defSeconds == 0) {
            if(TextUtils.isEmpty(defSeconds_str) == false){
                defSeconds = Integer.parseInt(defSeconds_str);
            }
        }

        int db = 1;
        int maxTotaol = 200;

        if (TextUtils.isEmpty(db_str) == false) {
            db = Integer.parseInt(db_str);
        }

        if (TextUtils.isEmpty(maxTotaol_str) == false) {
            maxTotaol = Integer.parseInt(maxTotaol_str);
        }


        do_init(keyHeader, defSeconds, server, password, db, maxTotaol, JavabinSerializer.instance);
    }

    public RedisCache(String keyHeader, int defSeconds, String server, String password, int db, int maxTotaol) {
        do_init(keyHeader, defSeconds, server, password, db, maxTotaol, JavabinSerializer.instance);
    }

    public RedisCache(String keyHeader, int defSeconds, String server, String password, int db, int maxTotaol, ISerializer<String> serializer) {
        do_init(keyHeader, defSeconds, server, password, db, maxTotaol, serializer);
    }

    private void do_init(String keyHeader, int defSeconds, String server, String password, int db, int maxTotaol, ISerializer<String> serializer) {
        if (defSeconds < 10) {
            defSeconds = 60;
        }

        if (db < 1) {
            db = 1;
        }

        if (maxTotaol < 10) {
            maxTotaol = 10;
        }

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
