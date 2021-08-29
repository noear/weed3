package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import org.noear.weed.Variate;
import org.noear.weed.wrap.Property;
import webapp.model.TestModel;
import weed3rdb.DbUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author noear 2021/8/29 created
 */
public class TableUpdateListTest {
    DbContext db = DbUtil.db;

    @Test
    public void test11() throws Exception {
        //删
        db.table("test").where("1=1").delete();

        List<DataItem> items = new ArrayList<>();
        items.add(new DataItem().set("id", 1).set("v1", 1));
        items.add(new DataItem().set("id", 2).set("v1", 2));
        items.add(new DataItem().set("id", 3).set("v1", 3));

        //增
        db.table("test").insertList(items);


        items.clear();
        items.add(new DataItem().set("id", 1).set("v1", 11));
        items.add(new DataItem().set("id", 2).set("v1", 12));
        items.add(new DataItem().set("id", 3).set("v1", 13));

        db.table("test").updateList(items, "id");

        assert db.table("test").whereEq("id", 1).selectValue("v1", 0) == 11;
        assert db.table("test").whereEq("id", 2).selectValue("v1", 0) == 12;
    }

    @Test
    public void test12() throws Exception {
        //删
        db.table("test").where("1=1").delete();

        List<Object[]> items = new ArrayList<>();
        items.add(new Object[]{1, 1});
        items.add(new Object[]{2, 2});
        items.add(new Object[]{3, 3});

        //增
        db.exeBatch("INSERT INTO test(id,v1) VALUES(?,?)", items);


        List<DataItem> item2 = new ArrayList<>();
        item2.add(new DataItem().set("id", 1).set("v1", 11));
        item2.add(new DataItem().set("id", 2).set("v1", 12));
        item2.add(new DataItem().set("id", 3).set("v1", 13));

        db.table("test").updateList(item2, "id");

        assert db.table("test").whereEq("id", 1).selectValue("v1", 0) == 11;
        assert db.table("test").whereEq("id", 2).selectValue("v1", 0) == 12;
    }

    @Test
    public void test13() throws Exception {
        //删
        db.table("test").where("1=1").delete();

        List<Object[]> items = new ArrayList<>();
        items.add(new Object[]{1, 1});
        items.add(new Object[]{2, 2});
        items.add(new Object[]{3, 3});

        //增
        db.exeBatch("INSERT INTO test(id,v1) VALUES(?,?)", items);


        List<TestModel> item2 = new ArrayList<>();
        item2.add(new TestModel(1, 11));
        item2.add(new TestModel(2, 12));
        item2.add(new TestModel(3, 13));

        db.mapperBase(TestModel.class).updateList(item2, (d, m) -> m.setEntity(d), TestModel::getId);

        assert db.table("test").whereEq("id", 1).selectValue("v1", 0) == 11;
        assert db.table("test").whereEq("id", 2).selectValue("v1", 0) == 12;
    }
}
