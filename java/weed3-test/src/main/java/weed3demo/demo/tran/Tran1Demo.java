package weed3demo.demo.tran;

import org.noear.weed.DbContext;
import org.noear.weed.DbTranQueue;
import weed3demo.config.DbConfig;

import java.sql.SQLException;

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
    private static void test_db1_tran() throws SQLException {
        //1.简单处理
        DbConfig.pc_user.tran((t) -> {
            //
            // 此表达式内的操作，会自动加入事务（3.2.0.3 开始支持）
            //
            t.db().sql("insert into test(txt) values(?)", "cc").execute();
            t.db().sql("insert into test(txt) values(?)", "dd").execute();

            t.db().sql("update test set txt='1' where id=1").execute();
        });
    }

    /*2个数据库的事务（在同一个函数内，分布式）*/
    public static void test_db2_tran() throws SQLException {
        DbContext db = DbConfig.pc_user;
        DbContext db2 = DbConfig.pc_base;

        //1.建立主事务，并执于
        DbTranQueue queue = new DbTranQueue();

        queue.execute((qt)->{
            db.table("").tran(db.tran().join(qt))
                    .select("");
        });

        db.tran().join(queue).execute((t) -> {
            db.sql("insert into test(txt) values(?)", "cc").execute();
            db.sql("insert into test(txt) values(?)", "dd").execute();
            db.sql("insert into test(txt) values(?)", "ee").execute();
        });

        //2.执行第二个事务
        db2.tran().join(queue).execute((t) -> {
            db2.sql("insert into test(txt) values(?)", "gg")
                    .execute();
        });

        queue.complete();
    }

    /*2个数据库的事务，后面的根据前面的执行结果再决定要不要跟进（在同一个函数内，分布式）*/
    public static void test_db2_tran2() throws SQLException {
        DbContext db = DbConfig.pc_user;
        DbContext db2 = DbConfig.pc_base;

        //1.建立主事务，并执于
        DbTranQueue queue = new DbTranQueue();

        //1.建立主事务，并执于
        db.tran().join(queue).execute((t) -> {
            db.sql("insert into test(txt) values(?)", "cc").execute();
            db.sql("insert into test(txt) values(?)", "dd").execute();
            db.sql("insert into test(txt) values(?)", "ee").execute();

            queue.result = 1;
        });


        //2.根据执行结果判断
        if ((int) queue.result == 1) {
            //3.执行第二个事务
            db2.tran().join(queue).execute((t) -> {
                db2.sql("insert into test(txt) values(?)", "gg").execute();
            });//json(tran) 时，会自动调用 await(true);  当 await(true)时，需要之后的事务来触发或手动触发
        }

        //4.统一触发事务
        queue.complete();
    }
}
