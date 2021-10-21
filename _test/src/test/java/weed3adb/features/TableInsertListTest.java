package weed3adb.features;

import org.junit.Test;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import webapp.model.TestModel;
import weed3adb.DbUtil;

import java.util.ArrayList;
import java.util.List;

public class TableInsertListTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception {
        //删
        db.table("test").where("1=1").delete();

        long idBase = System.currentTimeMillis();

        //增
        db.table("test").set("v1", 1).set("id", 1 + idBase).insert();
        db.table("test").set("v1", 2).set("id", 2 + idBase).insert();
        db.table("test").set("v1", 3).set("id", 3 + idBase).insert();

        assert db.table("test").selectCount() >= 3;
    }

    @Test
    public void test11() throws Exception{
        //删
        db.table("test").where("1=1").delete();

        long idBase = System.currentTimeMillis();

        List<DataItem> items = new ArrayList<>();
        items.add(new DataItem().set("id",1 + idBase).set("v1",1));
        items.add(new DataItem().set("id",2 + idBase).set("v1",2));
        items.add(new DataItem().set("id",3 + idBase).set("v1",3));

        //增
        db.table("test").insertList(items);

        assert db.table("test").selectCount() >= 3;
    }

    @Test
    public void test11_2() throws Exception{

        long idBase = System.currentTimeMillis();

        List<Object[]> list = new ArrayList<>();
        list.add(new Object[]{1 + idBase,2});
        list.add(new Object[]{2 + idBase,3});

        db.exeBatch("INSERT INTO test(id,v2) VALUES(?,?)", list);
    }

    @Test
    public void test12() throws Exception{
        //删
        db.table("test").where("1=1").delete();


        long idBase = System.currentTimeMillis();

        List<TestModel> items = new ArrayList<>();
        items.add(new TestModel(1 + idBase,1));
        items.add(new TestModel(2 + idBase,2));
        items.add(new TestModel(3 + idBase,3));

        //增
        db.mapperBase(TestModel.class).insertList(items);

        assert db.table("test").selectCount() >= 3;
    }
}
