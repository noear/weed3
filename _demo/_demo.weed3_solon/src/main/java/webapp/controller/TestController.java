package webapp.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.annotation.XSingleton;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import webapp.Config;
import webapp.dso.SqlMapper;
import webapp.model.AppxModel;

@XMapping("/test")
@XSingleton(true)
@XController
public class TestController {
    DbContext db2 = Config.db2();

    @XMapping("demo1")
    public Object test(String sql) throws Exception {
        //
        // mysql 8.0 才支持
        //
        Object tmp = db2.table("ag").innerJoin("ax").on("ag.agroup_id = ax.agroup_id")
                .limit(10)
                .with("ax", db2.table("appx").selectQ("*"))
                .with("ag", db2.table("appx_agroup").where("agroup_id < 10").selectQ("*"))
                .with("ah", "select * from appx_agroup where agroup_id<?", 10)
                .select("ax.*")
                .getMapList();

        if (sql == null) {
            return tmp;
        } else {
            return db2.lastCommand.text;
        }
    }

    @XMapping("demo2")
    public Object test2(String sql) throws Exception {
        //
        // mysql 8.0 才支持
        //
        Object tmp = db2.table("ax")
                .orderByDesc("app_id")
                .limit(2)
                .with("ax", db2.table("appx").selectQ("*"))
                .select("ax.*")
                .getMapList();

        if (sql == null) {
            return tmp;
        } else {
            return db2.lastCommand.text;
        }
    }

    @XMapping("demo3")
    public Object test3(String sql) throws Exception {
        BaseMapper<AppxModel> mapper = db2.mapperBase(AppxModel.class);
        Object tmp = mapper.selectById(48);

        Object tmp2 = mapper.selectItem(m -> m.where("app_id=?", 12));

        Object tmp3 = mapper.selectItem(m -> m.whereEq("app_id", 21));

        if (sql == null) {
            return tmp;
        } else {
            return db2.lastCommand.text;
        }
    }

    @XMapping("demo4")
    public Object test4(String sql) throws Exception {
        SqlMapper mapper = db2.mapper(SqlMapper.class);

        Object tmp = mapper.selectById(48);

        Object tmp2 = mapper.selectItem(m -> m.where("app_id=?", 12));

        Object tmp3 = mapper.selectItem(m -> m.whereEq("app_id", 21));

        if (sql == null) {
            return tmp;
        } else {
            return db2.lastCommand.text;
        }
    }
}
