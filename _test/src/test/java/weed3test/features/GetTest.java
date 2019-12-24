package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.model.AppxModel;

public class GetTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception {
        assert db.table("appx")
                .whereEq("app_id", 22)
                .select("*")
                .getItem(AppxModel.class).app_id == 22;

    }

    @Test
    public void test2() throws Exception {
        assert db.table("appx")
                .whereEq("app_id", 22)
                .select("*")
                .getDataItem().count() > 2;

    }

    @Test
    public void test3() throws Exception {
        assert db.table("appx")
                .whereEq("app_id", 22)
                .select("*")
                .getMap().size() > 2;

    }


    @Test
    public void test11() throws Exception {
         db.table("appx")
                .whereLt("app_id", 22)
                .select("*")
                .getList(AppxModel.class).forEach(m -> m.getApp_id());

    }

    @Test
    public void test12() throws Exception {
         db.table("appx")
                .whereLt("app_id", 22)
                .select("*")
                .getDataList().forEach(item -> item.getInt("app_id"));

    }

    @Test
    public void test13() throws Exception {
        db.table("appx")
                .whereLt("app_id", 22)
                .select("*")
                .getMapList().forEach(map -> map.get("app_id"));

    }
}
