package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.model.AgroupModel;
import weed3test.model.AppxModel;

import static org.noear.weed.wrap.PropertyWrap.$;

public class TableJoinTest {
    DbContext db = DbUtil.db;

    @Test
    public void join_select() throws Exception {
        AppxModel m = db.table("appx a")
                .innerJoin("appx_agroup g").onEq("a.agroup_id", "g.agroup_id")
                .whereEq("a.app_id", 22)
                .select("*,g.name agroup_name")
                .getItem(AppxModel.class);

        assert m.app_id == 22;

        System.out.println(db.lastCommand.text);
    }


    @Test
    public void join_select2() throws Exception {
        AppxModel m = db.table(AppxModel.class)
                .innerJoin(AgroupModel.class).onEq(AppxModel::getAgroup_id, AgroupModel::getAgroup_id)
                .whereEq(AppxModel::getApp_id, 22)
                .select(AppxModel.class, $(AgroupModel::getName).alias("agroup_name"))
                .getItem(AppxModel.class);

        assert m.app_id == 22;

        System.out.println(db.lastCommand.text);
    }
}
