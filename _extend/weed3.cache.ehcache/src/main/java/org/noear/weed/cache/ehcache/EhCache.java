package org.noear.weed.cache.ehcache;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.utils.EncryptUtils;


import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import java.util.concurrent.TimeUnit;


public class EhCache implements ICacheServiceEx {
    private String _cacheKeyHead;
    private int _defaultSeconds;

    private Cache<String,Object> _cache = null;

    public EhCache(String keyHeader, int defSeconds) {
        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;

        // 配置默认缓存属性
        CacheConfiguration<String, Object> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(
                // 缓存数据K和V的数值类型
                // 在ehcache3.3中必须指定缓存键值类型,如果使用中类型与配置的不同,会报类转换异常
                String.class, Object.class,
                ResourcePoolsBuilder
                        .newResourcePoolsBuilder()
                        //设置缓存堆容纳元素个数(JVM内存空间)超出个数后会存到offheap中
                        .heap(1000L, EntryUnit.ENTRIES)
                        //设置堆外储存大小(内存存储) 超出offheap的大小会淘汰规则被淘汰
                        .offheap(100L, MemoryUnit.MB)
                        // 配置磁盘持久化储存(硬盘存储)用来持久化到磁盘,这里设置为false不启用
                        .disk(500L, MemoryUnit.MB, false)
        ).withExpiry(Expirations.timeToLiveExpiration(
                //设置缓存过期时间
                Duration.of(defSeconds, TimeUnit.SECONDS))
        ).withExpiry(Expirations.timeToIdleExpiration(
                //设置被访问后过期时间(同时设置和TTL和TTI之后会被覆盖,这里TTI生效,之前版本xml配置后是两个配置了都会生效)
                Duration.of(60L, TimeUnit.SECONDS))
        ).build();

        // CacheManager管理缓存
        CacheManager manager = CacheManagerBuilder.newCacheManagerBuilder()
                // 设置一个默认缓存配置
                .withCache("defaultCache", cacheConfiguration)
                //创建之后立即初始化
                .build(true);

        _cache = manager.getCache("defaultCache", String.class, Object.class);//获得缓存

        if (_defaultSeconds < 1) {
            _defaultSeconds = 30;
        }
    }

    @Override
    public void store(String key, Object obj, int seconds) {
        if (_cache != null) {
            String newKey = newKey(key);
            try {
                _cache.put(newKey, obj);
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
            _cache.remove(newKey);
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

