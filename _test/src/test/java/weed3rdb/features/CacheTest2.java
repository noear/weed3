package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

/**
 * @author noear 2021/4/2 created
 */
public class CacheTest2 {
    ICacheServiceEx cache = new LocalCache();

    @Test
    public void test() throws Exception{
        long time1 = cache.getBy(60,"cache_test",(cu)->{
            return System.currentTimeMillis();
        });


        long time2 = cache.getBy(60,"cache_test",(cu)->{
            return System.currentTimeMillis();
        });

        assert time1 == time2;

        Thread.sleep(1000 * 50);


        long time3 = cache.getBy(60,"cache_test",(cu)->{
            return System.currentTimeMillis();
        });


        assert time1 == time3;


        Thread.sleep(1000 * 20);


        long time4 = cache.getBy(60,"cache_test",(cu)->{
            return System.currentTimeMillis();
        });


        assert time1 != time4;


    }
}
