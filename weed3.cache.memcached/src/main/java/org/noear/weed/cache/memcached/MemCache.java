package org.noear.weed.cache.memcached;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.utils.EncryptUtils;

public class MemCache implements ICacheServiceEx {
    private String _cacheKeyHead;
    private int _defaultSeconds;

    private MemcachedClient _cache = null;

    public MemCache(String keyHeader, int defSeconds, String server, String user, String password) {
        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;

        try {
            if (server.indexOf("aliyun") > 0) {
                //1.阿里云
                AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"},
                        new PlainCallbackHandler(user, password));

                _cache = new MemcachedClient(new ConnectionFactoryBuilder()
                        .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                        .setAuthDescriptor(ad).build(),
                        AddrUtil.getAddresses(server));
            } else {
                //2.本地
                _cache = new MemcachedClient(new ConnectionFactoryBuilder()
                        .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).build(),
                        AddrUtil.getAddresses(server));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void store(String key, Object obj, int seconds) {
        if (_cache != null) {
            String newKey = newKey(key);
            try {
                _cache.set(newKey, seconds, obj);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Object get(String key) {
        if (_cache != null) {
            String newKey = newKey(key);
            try {
                return _cache.get(newKey);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void remove(String key) {
        if (_cache != null) {
            String newKey = newKey(key);
            _cache.delete(newKey);
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
