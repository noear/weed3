package weed3cache;

import org.junit.Test;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

/**
 * @author noear 2021/4/1 created
 */
public class CacheTest {
    static ICacheServiceEx cache = new LocalCache();

    @Test
    public void test() throws Exception{
        long tmp = cache.getBy(60, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        long tmp2 = cache.getBy(60, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        assert tmp == tmp2;


        Thread.sleep(1000 * 50);

        tmp2 = cache.getBy(60, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        assert tmp == tmp2;

        Thread.sleep(1000 * 20);

        tmp2 = cache.getBy(60, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        assert tmp != tmp2;
    }

    @Test
    public void test2() throws Exception{
        long tmp = cache.getBy(300, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        long tmp2 = cache.getBy(300, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        assert tmp == tmp2;


        Thread.sleep(1000 * 200);

        tmp2 = cache.getBy(300, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        assert tmp == tmp2;
    }
}
