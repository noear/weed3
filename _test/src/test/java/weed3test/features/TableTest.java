package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.model.AgroupModel;
import weed3test.model.AppxModel;
import weed3test.model.TestModel;

import java.util.Map;

import static org.noear.weed.wrap.PropertyWrap.$;


public class TableTest {
    DbContext db = DbUtil.db;

    @Test
    public void test0() throws Exception {
        Map<String, Object> map = db.table("appx").whereEq("app_id", 1).select("*").getMap();

        map.remove("app_id");

        assert db.table("appx_copy")
                .setMap(map)
                .whereEq("app_id",101)
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
    public void test11() throws Exception {
        assert db.table(AppxModel.class)
                .whereEq(AppxModel::getApp_id, 21)
                .select("*")
                .getItem(AppxModel.class).app_id == 21;

        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test21() throws Exception {
        //删
        db.table(TestModel.class).where("1=1").delete();

        //增
        db.table(TestModel.class).set(TestModel::getV1, 1).set(TestModel::getId, 1).insert();
        db.table(TestModel.class).set(TestModel::getV1, 2).set(TestModel::getId, 2).insert();
        db.table(TestModel.class).set(TestModel::getV1, 3).set(TestModel::getId, 3).insert();

        assert db.table(TestModel.class).count() == 3;


        //改
        long id = 10;
        db.table(TestModel.class).set(TestModel::getV1, 1).set(TestModel::getId, 10).insert();
        assert db.table(TestModel.class)
                .set(TestModel::getV1, 10)
                .whereEq(TestModel::getId, id)
                .update() == 1;

        //查
        assert db.table(TestModel.class)
                .whereEq(TestModel::getId, id)
                .select($(TestModel::getV1))
                .getVariate().longValue(0l)== 10;
    }

    @Test
    public void test22() throws Exception{
        assert db.table(TestModel.class)
                .set(TestModel::getV1, 10)
                .whereEq(TestModel::getId, 10)
                .update() == 1;
    }
}
