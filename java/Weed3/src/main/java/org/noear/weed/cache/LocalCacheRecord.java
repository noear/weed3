package org.noear.weed.cache;

import java.util.concurrent.Future;

/**
 * Created by yuety on 14/11/7.
 */
class LocalCacheRecord {
    public Object value;
    public Future future;

    public LocalCacheRecord(Object val) {
        this.value = val;
    }
}
