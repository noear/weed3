package org.noear.weed.cache;

import org.noear.weed.WeedConfig;
import org.noear.weed.ext.Fun1;

/**
 * Created by noear on 14-6-12.
 */
public interface ICacheServiceEx extends ICacheService {
    CacheTags tags();
    void clear(String tag);
    <T> void update(String tag, Fun1<T, T> setter);
    /** 名字设置（自动注册到cache库）*/
    default ICacheServiceEx nameSet(String name) {
        if (name != null) {
            WeedConfig.libOfCache.put(name, this);
        }

        return this;
    }
}
