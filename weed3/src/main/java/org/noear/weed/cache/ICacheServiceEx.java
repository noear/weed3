package org.noear.weed.cache;

import org.noear.weed.WeedConfig;
import org.noear.weed.ext.Fun0Ex;
import org.noear.weed.ext.Fun1;

import java.sql.SQLException;

/**
 * Created by noear on 14-6-12.
 */
public interface ICacheServiceEx extends ICacheService {
    /** 缓存标签管理器 */
    default CacheTags tags(){
        return new CacheTags(this);
    }

    /** 清空缓存 */
    default void clear(String tag){
        tags().clear(tag);
    }

    /** 更新缓存 */
    default <T> void update(String tag, Fun1<T, T> setter){
        tags().update(tag,setter);
    }

    /** 获取 */
    default <T> T getBy(int seconds, String key, Fun0Ex<T, Exception> builder) throws Exception{
        CacheUsing cu  = new CacheUsing(this);
        return cu.usingCache(12).getEx(key, builder);
    }

    /** 名字设置（自动注册到cache库）*/
    default ICacheServiceEx nameSet(String name) {
        if (name != null) {
            WeedConfig.libOfCache.put(name, this);
        }

        return this;
    }
}
