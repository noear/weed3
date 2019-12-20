package weed3test.features;

import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.model.AppxModel;

import java.util.List;
import java.util.Map;

public class WithTest {
    DbContext db = DbUtil.db;

    @Test
    public void test() throws Exception {
        //
        // mysql 8.0 才支持
        //
        List<AppxModel> list  = db.table("#ag").innerJoin("#ax").on("ag.agroup_id = ax.agroup_id")
                .limit(10)
                .with("ax", db.table("appx").selectQ("*"))
                .with("ag", db.table("appx_agroup").where("agroup_id < 10").selectQ("*"))
                .with("ah", "select * from $.appx_agroup where agroup_id<?", 10)
                .select("ax.*")
                .getList(AppxModel.class);

        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test2() throws Exception {
        //
        // mysql 8.0 才支持
        //
        Object tmp = db.table("#ax")
                .orderByDesc("app_id")
                .limit(2)
                .with("ax", db.table("appx").selectQ("*"))
                .select("ax.*")
                .getMapList();

        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test3() throws Exception {
        BaseMapper<AppxModel> mapper = db.mapperBase(AppxModel.class);
        Object tmp = mapper.selectById(48);

        AppxModel tmp2 = mapper.selectItem(m -> m.where("app_id=?", 12));

        AppxModel tmp3 = mapper.selectItem(m -> m.whereEq("app_id", 12));

        assert  tmp2.app_id == tmp3.app_id;
    }
}
