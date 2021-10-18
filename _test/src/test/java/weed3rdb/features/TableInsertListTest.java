package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import weed3rdb.DbUtil;
import webapp.model.TestModel;

import java.util.ArrayList;
import java.util.List;

public class TableInsertListTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception{
        //删
        db.table("test").where("1=1").delete();

        //增
        db.table("test").set("v1", 1).set("id", 1).insert();
        db.table("test").set("v1", 2).set("id", 2).insert();
        db.table("test").set("v1", 3).set("id", 3).insert();

        assert db.table("test").selectCount() == 3;
    }

    @Test
    public void test11() throws Exception{
        //删
        db.table("test").where("1=1").delete();

        List<DataItem> items = new ArrayList<>();
        items.add(new DataItem().set("id",1).set("v1",1));
        items.add(new DataItem().set("id",2).set("v1",2));
        items.add(new DataItem().set("id",3).set("v1",3));

        //增
        db.table("test").insertList(items);

        assert db.table("test").selectCount() == 3;
    }

    public void test11_2() throws Exception{
        List<Object[]> list = new ArrayList<>();
        list.add(new Object[]{1,2});
        list.add(new Object[]{1,3});

        db.exeBatch("INSERT INTO test(a,b) VALUES(?,?)", list);
    }

    @Test
    public void test12() throws Exception{
        //删
        db.table("test").where("1=1").delete();

        List<TestModel> items = new ArrayList<>();
        items.add(new TestModel(1,1));
        items.add(new TestModel(2,2));
        items.add(new TestModel(3,3));

        //增
        db.mapperBase(TestModel.class).insertList(items);

        assert db.table("test").selectCount() == 3;
    }
}
