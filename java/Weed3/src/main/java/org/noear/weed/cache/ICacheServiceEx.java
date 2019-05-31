package org.noear.weed.cache;

import org.noear.weed.ext.Fun1;

/**
 * Created by noear on 14-6-12.
 */
public interface ICacheServiceEx extends ICacheService {
    CacheTags tags();
    void clear(String tag);
    <T> void update(String tag, Fun1<T, T> setter);
}
