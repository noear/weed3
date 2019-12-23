package org.noear.weed;

import java.util.HashMap;

public class WdMap  extends HashMap<String,Object> {
    public WdMap set(String key, Object value) {
        put(key, value);
        return this;
    }
}
