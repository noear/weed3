package weed3rdb.speed;

import org.junit.Test;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import org.noear.weed.Trans;
import weed3rdb.DbUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author noear 2021/9/6 created
 */
public class BatchTest {
    DbContext db = DbUtil.db;

    @Test
    public void test11() throws Exception {
        db.table("test").whereTrue().delete();

        List<DataItem> items = new ArrayList<>();
        for (int i = 1; i <= 100000; i++) {
            items.add(new DataItem().set("id", i).set("v1", i));
        }

        //预热
        db.table("test").insertList(items);
        db.table("test").whereTrue().delete();

        long start = System.currentTimeMillis();

        db.table("test").insertList(items);

        System.out.println("used time: " + (System.currentTimeMillis() - start));

        assert  db.table("test").selectCount() == 100000;
    }

    @Test
    public void test11_2() throws Exception{
        db.table("test").whereTrue().delete();

        List<Object[]> list = new ArrayList<>();
        for (int i = 1; i <= 100000; i++) {
            list.add(new Object[]{i, i});
        }

        //预热
        Trans.tran(()-> {
            db.exeBatch("INSERT INTO test(`id`,`v1`) VALUES(?,?)", list);
            db.table("test").whereTrue().delete();
        });

        long start = System.currentTimeMillis();

        Trans.tran((()->{
            db.exeBatch("INSERT INTO test(`id`,`v1`) VALUES(?,?)", list);
        }));

        System.out.println("used time: " + (System.currentTimeMillis() - start));

        //assert  db.table("test").selectCount() == 100000;
    }
}
