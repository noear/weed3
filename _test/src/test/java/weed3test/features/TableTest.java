package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.model.AgroupModel;
import weed3test.model.AppxCopyModel;
import weed3test.model.AppxModel;
import weed3test.model.TestModel;

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
        AppxModel m = db.table("appx a")
                .innerJoin("appx_agroup g").onEq("a.agroup_id", "g.agroup_id")
                .whereEq("app_id", 22)
                .select("*,g.name agroup_name")
                .getItem(AppxModel.class);

        assert m.app_id == 22;

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
        AppxModel m = db.table(AppxModel.class)
                .innerJoin(AgroupModel.class).onEq(AppxModel::getAgroup_id, AgroupModel::getAgroup_id)
                .whereEq(AppxModel::getApp_id, 22)
                .select(AppxModel.class, $(AgroupModel::getName).alias("agroup_name"))
                .getItem(AppxModel.class);

        assert m.app_id == 22;

        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test21() throws Exception {
        //删
        db.table(TestModel.class).where("1=1").delete();

        //增
        db.table(TestModel.class).set(TestModel::getV1,1).insert();
        db.table(TestModel.class).set(TestModel::getV1,2).insert();
        db.table(TestModel.class).set(TestModel::getV1,3).insert();

        assert  db.table(TestModel.class).count() == 3;


        //改
        long id = db.table(TestModel.class).set(TestModel::getV1,1).insert();
        assert  db.table(TestModel.class).set(TestModel::getV1,10).whereEq(TestModel::getId,id).update() == 1;

        //查
        assert  db.table(TestModel.class)
                  .whereEq(TestModel::getId,id)
                  .select($(TestModel::getV1))
                  .getValue(0) == 10;
    }
}
