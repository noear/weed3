package org.noear.weed.cache;

import org.noear.weed.ext.Fun1;

/**
 * Created by noear on 2017/7/22.
 */
public class EmptyCache implements ICacheServiceEx {
    @Override
    public void store(String s, Object o, int i) {

    }

    @Override
    public Object get(String s) {
        return null;
    }

    @Override
    public void remove(String s) {

    }

    @Override
    public int getDefalutSeconds() {
        return 0;
    }

    @Override
    public String getCacheKeyHead() {
        return "";
    }



    //==================
    //
    @Override
    public CacheTags tags() {
        return new CacheTags(this);
    }
    @Override
    public void clear(String tag) {
        tags().clear(tag);
    }
    @Override
    public <T> void update(String tag, Fun1<T, T> setter) {
        tags().update(tag, setter);
    }
}
