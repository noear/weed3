package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3rdb.DbUtil;
import webapp.model.AppxModel;

import java.util.Map;

public class TableTest {
    DbContext db = DbUtil.db;

    @Test
    public void test0() throws Exception {
        Map<String, Object> map = db.table("appx").whereEq("app_id", 1).select("*").getMap();

        map.remove("app_id");

        assert db.table("appx_copy")
                .setMap(map)
                .whereEq("app_id",11)
                .update() > 0;

        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test02() throws Exception {
        Map<String, Object> map = db.table("appx").whereEq("app_id", 1).select("*").getMap();

        map.remove("app_id");

        assert db.table("appx_copy")
                .setMap(map)
                .whereEq("app_id",11).orEq("agroup_id",null)
                .update() > 0;

        System.out.println(db.lastCommand.text);
    }

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


    @Test
    public void test2() throws Exception {
        //删
        db.table("test").where("1=1").delete();

        //增
        db.table("test").set("v1", 1).set("id", 1).insert();
        db.table("test").set("v1", 2).set("id", 2).insert();
        db.table("test").set("v1", 3).set("id", 3).insert();

        assert db.table("test").count() == 3;


        //改
        long id = 10;
        db.table("test").set("v1", 1).set("id", 10).insert();
        assert db.table("test")
                .set("v1", 10)
                .whereEq("id", id)
                .update() == 1;

        //查
        assert db.table("test")
                .whereEq("id", id)
                .select("v1")
                .getVariate().longValue(0l)== 10;
    }

    @Test
    public void test3() throws Exception{
        assert db.table("test")
                .set("v1", 10)
                .whereEq("id", 10)
                .update() == 1;
    }
}
