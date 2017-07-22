package weed3demo.config.utils;

import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.SerializingTranscoder;
import noear.snacks.ONode;

/**
 * Created by yuety on 2017/7/4.
 */
public class SerializeUtil {
    private static SerializingTranscoder tc = new SerializingTranscoder();

    public static  String toString(Object val) {
        CachedData cd = tc.encode(val);

        ONode obj = new ONode();

        obj.set("flags",cd.getFlags());
        obj.set("data",Base64Util.encodeByte(cd.getData()));

        return obj.toJson();
    }

    public  static  <T> T byString(String str) {
        if(str == null)
            return null;

        ONode obj = ONode.tryLoad(str);

        int flags = obj.get("flags").getInt();
        String data_b64 =obj.get("data").getString();
        byte[] data = Base64Util.decodeByte(data_b64);

        CachedData cd = new CachedData(flags, data, tc.getMaxSize());

        return (T)tc.decode(cd);
    }
}
