package weed3demo.demo.tran;

import org.noear.weed.DbContext;
import org.noear.weed.Trans;
import weed3demo.config.DbConfig;

/**
 * Created by noear on 2017/7/22.
 */
public class Tran1Demo {

    /*
    private static void test_db1_tran_old() throws SQLException {
        //1.简单处理
        DbConfig.pc_user.tran((t) -> {
            t.db().sql("insert into test(txt) values(?)", "cc").tran(t).execute();
            t.db().sql("insert into test(txt) values(?)", "dd").tran(t).execute();

            t.db().sql("update test set txt='1' where id=1").tran(t).execute();
        });
    }*/

    /*所有的执行在一个事务控制范围内*/
    private static void test_db1_tran() throws Throwable {
        //1.简单处理
        Trans.tran(() -> {
            //
            // 此表达式内的操作，会自动加入事务（3.2.0.3 开始支持）
            //
            DbConfig.pc_user.sql("insert into test(txt) values(?)", "cc").execute();
            DbConfig.pc_user.sql("insert into test(txt) values(?)", "dd").execute();

            DbConfig.pc_user.sql("update test set txt='1' where id=1").execute();
        });
    }

    /*2个数据库的事务（在同一个函数内，分布式）*/
    public static void test_db2_tran() throws Throwable {
        DbContext db = DbConfig.pc_user;
        DbContext db2 = DbConfig.pc_base;

        Trans.tran(()->{
            db.table("").select("");

            db.sql("insert into test(txt) values(?)", "cc").execute();
            db.sql("insert into test(txt) values(?)", "dd").execute();
            db.sql("insert into test(txt) values(?)", "ee").execute();


            db2.sql("insert into test(txt) values(?)", "gg")
                    .execute();
        });


    }

    /*2个数据库的事务，后面的根据前面的执行结果再决定要不要跟进（在同一个函数内，分布式）*/
    public static void test_db2_tran2() throws Throwable {
        DbContext db = DbConfig.pc_user;
        DbContext db2 = DbConfig.pc_base;

        Trans.tran(()->{
            db.sql("insert into test(txt) values(?)", "cc").execute();
            db.sql("insert into test(txt) values(?)", "dd").execute();
            db.sql("insert into test(txt) values(?)", "ee").execute();

            db2.sql("insert into test(txt) values(?)", "gg").execute();

        });

    }
}
