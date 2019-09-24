package weed3demo.config;

import weed3demo.config.cache.MemCache;
import org.noear.weed.cache.ICacheService;
import org.noear.weed.cache.LocalCache;

/**
 * Created by noear on 2017/7/22.
 */
public class CacheUtil {
    public static boolean isUsingCache;

    ICacheService text = new MemCache("text", 60, "127.0.0.0:8001",null,null);
    ICacheService xxxx = new LocalCache("xxxx", 60);
}
