package weed3test.features;

import org.junit.Test;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import weed3test.DbUtil;
import weed3test.model.AppxModel;

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

        cache.tags().update("app_23", (DataItem di) -> {
            AppxModel m = di.toEntity(AppxModel.class);
            assert m.app_id == 23;
            return di;
        });
    }
}
