package weed3demo.config.cache;

import org.noear.weed.cache.ICacheService;
import weed3demo.config.CacheUtil;
import weed3demo.config.utils.*;

/**
 * Created by Mazexal on 2017/4/25.
 */

public final class RedisCache implements ICacheService {
    private String _cacheKeyHead;
    private int _defaultSeconds;

    private RedisX mcc = null;


    public RedisCache(String keyHeader, int defSeconds, String server, String user, String password) {
        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;

        try {
            mcc = new RedisX(server, user, password, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void store(String key, Object obj, int seconds) {

        if (mcc != null && CacheUtil.isUsingCache) {
            String newKey = newKey(key);
            try {
                String val = SerializeUtil.toString(obj);

                RedisX.RedisUsing ru = mcc.open();

                ru.key(newKey).expire(seconds).set(val).close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Object get(String key) {
        if (mcc != null && CacheUtil.isUsingCache) {
            String newKey = newKey(key);
            try {
                RedisX.RedisUsing ru = mcc.open();

                String val = ru.key(newKey).get();
                Object obj = SerializeUtil.byString(val);

                ru.close();
                return obj;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public void remove(String key) {
        if (mcc != null && CacheUtil.isUsingCache) {
            String newKey = newKey(key);
            mcc.open().key(newKey).delete();
        }
    }

    public int getDefalutSeconds() {
        return _defaultSeconds;
    }

    public String getCacheKeyHead() {
        return _cacheKeyHead;
    }

    private String newKey(String key) {
        return _cacheKeyHead + "$" + EncryptUtil.md5(key);
    }
}