package org.noear.weed.cache;

import org.noear.weed.WeedConfig;
import org.noear.weed.ext.Fun1;

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

    /** 名字设置（自动注册到cache库）*/
    default ICacheServiceEx nameSet(String name) {
        if (name != null) {
            WeedConfig.libOfCache.put(name, this);
        }

        return this;
    }
}
