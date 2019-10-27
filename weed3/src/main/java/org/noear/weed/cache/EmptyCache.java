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
}
