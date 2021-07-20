package weed3cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.memcached.MemCache;
import org.noear.weed.cache.redis.RedisCache;

/**
 * @author noear 2021/4/1 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class RedisCacheTest {

    @Inject("${cache2}")
    RedisCache cache;

    @Test
    public void test() throws Exception{
        cache.remove("key");

        long tmp = cache.getBy(6, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        long tmp2 = cache.getBy(6, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        System.out.println(String.format("tmp:%s, tmp2:%s", tmp,tmp2));
        assert tmp == tmp2;


        Thread.sleep(1000 * 5);

        tmp2 = cache.getBy(6, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        System.out.println(String.format("tmp:%s, tmp2:%s", tmp,tmp2));
        assert tmp == tmp2;

        Thread.sleep(1000 * 3);

        tmp2 = cache.getBy(6, "key", (uc) -> {
            return System.currentTimeMillis();
        });

        System.out.println(String.format("tmp:%s, tmp2:%s", tmp,tmp2));
        assert tmp != tmp2;
    }

    @Test
    public void test2() throws Exception{
        cache.remove("key2");

        long tmp = cache.getBy(30, "key2", (uc) -> {
            return System.currentTimeMillis();
        });

        long tmp2 = cache.getBy(30, "key2", (uc) -> {
            return System.currentTimeMillis();
        });

        System.out.println(String.format("tmp:%s, tmp2:%s", tmp,tmp2));
        assert tmp == tmp2;


        Thread.sleep(1000 * 20);

        tmp2 = cache.getBy(30, "key2", (uc) -> {
            return System.currentTimeMillis();
        });

        System.out.println(String.format("tmp:%s, tmp2:%s", tmp,tmp2));
        assert tmp == tmp2;
    }
}
