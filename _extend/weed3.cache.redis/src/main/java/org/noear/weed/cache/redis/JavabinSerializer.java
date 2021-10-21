package org.noear.weed.cache.redis;

import org.noear.redisx.utils.SerializationUtil;
import org.noear.weed.cache.ISerializer;

import java.util.Base64;

/**
 * @author noear 2021/6/15 created
 */
public class JavabinSerializer implements ISerializer<String> {
    public static final JavabinSerializer instance = new JavabinSerializer();

    @Override
    public String name() {
        return "java-bin";
    }

    @Override
    public String serialize(Object obj) throws Exception {
        if(obj == null){
            return null;
        }

        byte[] tmp = SerializationUtil.serialize(obj);
        return Base64.getEncoder().encodeToString(tmp);
    }

    @Override
    public Object deserialize(String dta) throws Exception {
        if(dta == null){
            return null;
        }

        byte[] bytes = Base64.getDecoder().decode(dta);
        return SerializationUtil.deserialize(bytes);
    }
}
