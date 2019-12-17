package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.DbTranQueue;
import weed3test.DbUtil;

public class TranDemo {
    public void test1() throws Exception {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;
        DbTranQueue queue = new DbTranQueue();

        queue.execute((tq) -> {
            db1.tran(tq, (t)->{
               db1.sql("").execute();
            });
        });
    }
}
