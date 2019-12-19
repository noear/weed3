package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.model.AppxModel;

import java.util.HashMap;
import java.util.Map;

public class SqlTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception {
        assert db.sql("select * from $.appx where app_id=?", 32)
                .getItem(AppxModel.class)
                .app_id == 32;
    }

    public void test2() throws Exception {
        Map<String, Object> map = new HashMap<>();

        db.table("appx").setMap(map).upsert("akey");
    }
}
