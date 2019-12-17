package weed3test.features;

import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.model.AppxModel;

public class TableTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception {
        assert db.table("appx")
                .whereEq("app_id", 22)
                .select("*")
                .getItem(AppxModel.class).app_id == 22;

        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test2() throws Exception {
        assert db.table(AppxModel.class)
                .whereEq(AppxModel::getApp_id, 21)
                .select("*")
                .getItem(AppxModel.class).app_id == 21;

        System.out.println(db.lastCommand.text);
    }
}
