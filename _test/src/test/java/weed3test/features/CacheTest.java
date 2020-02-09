package weed3test.features;

import org.junit.Test;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import weed3test.DbUtil;
import weed3test.dso.rocedure.appx_get;
import weed3test.model.AppxModel;
import weed3test.model.AppxModel2;

public class CacheTest {
    DbContext db2 = DbUtil.db;
    ICacheServiceEx cache = new LocalCache();

    @Test
    public void test1() throws Exception {
        AppxModel tmp = db2.table("appx")
                .whereEq("app_id", 23)
                .caching(cache)
                .select("*")
                .cacheTag("app_23")
                .getItem(AppxModel.class);

        System.out.println("tmp.app_id = " + tmp.app_id);
        assert tmp.app_id == 23;

        cache.tags().update("app_23", (DataItem di) -> {
            AppxModel m = di.toEntity(AppxModel.class);
            assert m.app_id == 23;
            return di;
        });
    }

    @Test
    public void test2() throws Exception{
        AppxModel tmp = db2.table("appx")
                .whereEq("app_id", 23)
                .caching(cache)
                .select("*")
                .getItem(AppxModel.class, (uc, m) -> {
                    uc.cacheTag("app_" + m.app_id);
                });

        System.out.println("tmp.app_id = " + tmp.app_id);
        assert  tmp.app_id == 23;

        cache.tags().update("app_23", (DataItem di) -> {
            AppxModel m = di.toEntity(AppxModel.class);
            System.out.println("tmp.app_id = " + tmp.app_id);
            assert m.app_id == 23;
            return di;
        });
    }

    @Test
    public void test3() throws Exception{
        AppxModel2 tmp = db2.table("appx")
                .whereEq("app_id", 23)
                .caching(cache)
                .select("*")
                .getItem(new AppxModel2(), (uc, m) -> {
                    uc.cacheTag("app_" + m.app_id);
                });

        System.out.println("tmp.app_id = " + tmp.app_id);
        assert  tmp.app_id == 23;

        cache.tags().update("app_23", (DataItem di) -> {
            AppxModel2 m = di.toEntity(AppxModel2.class);
            System.out.println("tmp.app_id = " + tmp.app_id);
            assert m.app_id == 23;
            return di;
        });
    }

    @Test
    public void test13() throws Exception{
        appx_get sp = new appx_get(db2);
        sp.app_id = 23;

        AppxModel2 tmp =  sp.caching(cache)
                            .getItem(new AppxModel2(), (uc, m) -> {
                                uc.cacheTag("app_" + m.app_id);
                            });

        System.out.println("tmp.app_id = " + tmp.app_id);
        assert  tmp.app_id == 23;

        cache.tags().update("app_23", (DataItem di) -> {
            AppxModel2 m = di.toEntity(AppxModel2.class);
            System.out.println("tmp.app_id = " + tmp.app_id);
            assert m.app_id == 23;
            return di;
        });
    }
}
