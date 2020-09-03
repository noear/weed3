package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.DbTranQueue;
import org.noear.weed.VarHolder;
import weed3test.DbUtil;

public class TranTest {
    @Test
    public void test0() throws Throwable {
        DbContext db1 = DbUtil.db;

        clear(db1);

        db1.tran(t -> {
            db1.sql("insert into test (v1) values (1024);").insert();
            db1.sql("insert into test (v1) values (1024);").insert();
        });

        assert  db1.table("test").count()==2;
    }

    @Test
    public void test01() throws Throwable {
        DbContext db1 = DbUtil.db;

        clear(db1);

        try {
            db1.tran(t -> {
                db1.sql("insert into test (v1) values (1024);").insert();
                db1.sql("insert into test (v1) values (1024);").insert();

                throw new RuntimeException("不让你加");
            });
        }catch (Exception ex){

        }

        assert  db1.table("test").count()==0;
    }

    @Test
    public void test1() throws Throwable {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;

        clear(db1);

        new DbTranQueue().execute((tq) -> {
            db1.tran(tq, t -> {
                db1.sql("insert into test (v1) values (1024);").insert();
            });

            db2.tran(tq, t -> {
                db2.sql("insert into test (v1) values (1024);").insert();
            });
        });

        assert  db1.table("test").count()==2;
    }

    @Test
    public void test11() throws Throwable {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;

        clear(db1);

        try {
            new DbTranQueue().execute((tq) -> {
                db1.tran(tq, t -> {
                    db1.sql("insert into test (v1) values (1024);").insert();
                });

                db2.tran(tq, t -> {
                    db2.sql("insert into test (v1) values (1024);").insert();
                });

                throw new RuntimeException("不让你加");
            });

        } catch (Exception ex) {

        }


        assert db1.table("test").count() == 0;
    }

    public void demo2() throws Throwable {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;

        new DbTranQueue().execute((tq) -> {
            VarHolder<Long> tmp = new VarHolder<>();

            db1.tran(tq, t -> {
                tmp.value = db1.sql("").insert();
            });

            db2.tran(tq, t -> {
                db2.sql("").update();
            });
        });
    }

    private void clear(DbContext db) throws Exception {
        db.exe("TRUNCATE TABLE test");
    }
}
