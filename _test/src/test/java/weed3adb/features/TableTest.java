package weed3adb.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import webapp.model.AppxModel;
import weed3adb.DbUtil;

import java.util.Map;

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
    public void test12() throws Exception {
        assert db.table("appx")
                .whereEq("app_id", null)
                .select("*")
                .getItem(AppxModel.class).app_id == null;

        System.out.println(db.lastCommand.text);
    }


}
