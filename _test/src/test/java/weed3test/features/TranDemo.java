package weed3test.features;

import org.noear.weed.DbContext;
import org.noear.weed.DbTranQueue;
import weed3test.DbUtil;

public class TranDemo {
    public void test1() throws Exception {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;

        new DbTranQueue().execute((tq) -> {
            db1.tran().join(tq).execute(t -> {
                db1.sql("").insert();
            });

            db2.tran().join(tq).execute(t -> {
                db2.sql("").update();
            });
        });
    }
}
