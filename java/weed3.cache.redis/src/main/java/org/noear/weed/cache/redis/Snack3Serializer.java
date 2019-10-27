package org.noear.weed.cache.redis;

import org.noear.snack.ONode;
import org.noear.weed.cache.ISerializer;

public class Snack3Serializer implements ISerializer<String> {
    public static final Snack3Serializer instance = new Snack3Serializer();

    @Override
    public String name() {
        return "snack3-json";
    }

    @Override
    public String serialize(Object obj) throws Exception {
        return ONode.serialize(obj);
    }

    @Override
    public Object deserialize(String dta) throws Exception {
        return ONode.deserialize(dta, Object.class);
    }
}
