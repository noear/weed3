package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.model.AgroupModel;
import weed3test.model.AppxModel;

import static org.noear.weed.utils.PropertyWrap.$;


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
        assert db.table("appx a")
                 .innerJoin("appx_agroup g").onEq("a.agroup_id", "g.agroup_id")
                 .whereEq("app_id", 22)
                 .select("*,g.name gname")
                 .getItem(AppxModel.class).app_id == 22;

        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test11() throws Exception {
        assert db.table(AppxModel.class)
                .whereEq(AppxModel::getApp_id, 21)
                .select("*")
                .getItem(AppxModel.class).app_id == 21;

        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test12() throws Exception {
        assert db.table(AppxModel.class)
                 .innerJoin(AgroupModel.class).onEq(AppxModel::getAgroup_id,AgroupModel::getAgroup_id)
                 .whereEq(AppxModel::getApp_id, 22)
                 .select(AppxModel.class, $(AgroupModel::getName).alias("gname"))
                 .getItem(AppxModel.class).app_id == 22;

        System.out.println(db.lastCommand.text);
    }
}