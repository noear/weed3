package weed3demo.config.cache;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import noear.weed.cache.ICacheService;
import weed3demo.config.utils.EncryptUtil;

/**
 * Created by noear on 2017/4/25.
 */

public final class MemCache implements ICacheService {
    private String _cacheKeyHead;
    private int _defaultSeconds;

    private MemcachedClient mcc = null;

    public MemCache(String keyHeader, int defSeconds, String server, String user, String password) {
        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;

        try {
            if (server.indexOf("aliyun") > 0) {
                //1.阿里云
                AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"},
                        new PlainCallbackHandler(user, password));

                mcc = new MemcachedClient(new ConnectionFactoryBuilder()
                        .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                        .setAuthDescriptor(ad).build(),
                        AddrUtil.getAddresses(server));
            } else {
                //2.本地
                mcc = new MemcachedClient(new ConnectionFactoryBuilder()
                        .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).build(),
                        AddrUtil.getAddresses(server));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void store(String key, Object obj, int seconds) {

        if (mcc != null) {
            String newKey = newKey(key);
            try {
                mcc.set(newKey, seconds, obj);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Object get(String key) {
        if (mcc != null ) {
            String newKey = newKey(key);
            try {
                return mcc.get(newKey);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public void remove(String key) {
        if (mcc != null) {
            String newKey = newKey(key);
            mcc.delete(newKey);
        }
    }

    public int getDefalutSeconds()
    {
        return _defaultSeconds;
    }

    public String getCacheKeyHead()
    {
        return _cacheKeyHead;
    }

    private String newKey(String key) {
        return _cacheKeyHead + "$" + EncryptUtil.md5(key);
    }
}