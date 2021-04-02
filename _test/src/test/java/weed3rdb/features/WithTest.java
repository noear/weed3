package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.wrap.DbType;
import webapp.model.AppxModel;
import weed3rdb.DbUtil;

import java.util.List;

public class WithTest {
    DbContext db = DbUtil.db;

    @Test
    public void test() throws Exception {
        if(db.dbType() == DbType.Oracle){
            return;
        }

        //
        // mysql 8.0 才支持
        //
        List<AppxModel> list  = db.table("#ag").innerJoin("#ax").onEq("ag.agroup_id","ax.agroup_id")
                .limit(10)
                .with("ax", db.table("appx").selectQ("*"))
                .with("ag", db.table("appx_agroup").whereLt("agroup_id",10).selectQ("*"))
                .with("ah", "select * from $.appx_agroup where agroup_id<?", 10)
                .selectList("ax.*", AppxModel.class);

        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test2() throws Exception {
        if(db.dbType() == DbType.Oracle){
            return;
        }

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

}
