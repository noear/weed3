package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.Trans;
import org.noear.weed.VarHolder;
import weed3test.DbUtil;

public class TranTest {
    @Test
    public void test0() throws Throwable {
        DbContext db1 = DbUtil.db;

        clear(db1);

        Trans.tran(() -> {
            db1.sql("insert into test (v1) values (1024);").insert();
            db1.sql("insert into test (v1) values (1024);").insert();
        });

        assert db1.table("test").selectCount() == 2;
    }

    @Test
    public void test01() throws Throwable {
        DbContext db1 = DbUtil.db;

        clear(db1);

        try {
            Trans.tran(() -> {
                db1.sql("insert into test (v1) values (1024);").insert();
                db1.sql("insert into test (v1) values (1024);").insert();

                throw new RuntimeException("不让你加");
            });
        } catch (Exception ex) {

        }

        assert db1.table("test").selectCount() == 0;
    }

    @Test
    public void test1() throws Throwable {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;

        clear(db1);

        Trans.tran(() -> {
            db1.sql("insert into test (v1) values (1024);").insert();

            db2.sql("insert into test (v1) values (1024);").insert();
        });

        assert db1.table("test").selectCount() == 2;
    }

    @Test
    public void test1_1() throws Throwable {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;

        clear(db1);

        Trans.tran(() -> {
            db1.sql("insert into test (v1) values (1024);").insert();
            db2.sql("insert into test (v1) values (1024);").insert();
        });

        assert db1.table("test").selectCount() == 2;
    }

    @Test
    public void test11() throws Throwable {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;

        clear(db1);

        try {
            Trans.tran(() -> {
                db1.sql("insert into test (v1) values (1024);").insert();

                db2.sql("insert into test (v1) values (1024);").insert();


                throw new RuntimeException("不让你加");
            });

        } catch (Exception ex) {

        }

        db1.sql("insert into test (v1) values (1024);").insert();

        long count = db1.table("test").selectCount();
        System.out.print(count);
        assert count == 1;
    }

    @Test
    public void test11_1() throws Throwable {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;

        clear(db1);

        try {
            Trans.tran(() -> {
                db1.sql("insert into test (v1) values (1024);").insert();
                db2.sql("insert into test (v1) values (1024);").insert();

                throw new RuntimeException("不让你加");
            });

        } catch (Exception ex) {

        }

        db1.sql("insert into test (v1) values (1024);").insert();

        long count = db1.table("test").selectCount();
        System.out.print(count);
        assert count == 1;
    }

    public void demo2() throws Throwable {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;

        Trans.tran(() -> {
            VarHolder<Long> tmp = new VarHolder<>();

            tmp.value = db1.sql("").insert();

            db2.sql("").update();

        });
    }

    public void demo2_1() throws Throwable {
        DbContext db1 = DbUtil.db;
        DbContext db2 = DbUtil.db;

        Trans.tran(() -> {
            VarHolder<Long> tmp = new VarHolder<>();

            tmp.value = db1.sql("").insert();
            db2.sql("").update();

        });
    }

    private void clear(DbContext db) throws Exception {
        db.exe("TRUNCATE TABLE test");
    }
}
