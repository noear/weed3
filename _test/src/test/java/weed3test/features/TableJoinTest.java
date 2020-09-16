package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import webapp.model.AgroupModel;
import webapp.model.AppxModel;


public class TableJoinTest {
    DbContext db = DbUtil.db;

    @Test
    public void join_select() throws Exception {
        AppxModel m = db.table("appx a")
                .innerJoin("appx_agroup g").onEq("a.agroup_id", "g.agroup_id")
                .whereEq("a.app_id", 22)
                .select("a.*,g.name agroup_name")
                .getItem(AppxModel.class);

        assert m.app_id == 22;

        System.out.println(db.lastCommand.text);
    }

}
