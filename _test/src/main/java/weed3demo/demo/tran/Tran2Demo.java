package weed3demo.demo.tran;

import org.noear.weed.DbContext;
import org.noear.weed.DbTran;
import org.noear.weed.Trans;
import weed3demo.config.DbConfig;

import java.sql.SQLException;

/**
 * Created by noear on 2017/7/22.
 */
public class Tran2Demo {
    //不同函数串一起，跨多个数据库
    public static void tast_tran() throws Throwable{


        Trans.tran(() -> {
            //以下操作在同一个事务队列里执行（各事务独立）
            tast_db1_tran();
            tast_db2_tran();
            tast_db3_tran();
        });
    }

    //------------------

    private static void tast_db1_tran( ) throws Throwable {
        //使用了 .await(true) 将不提交事务（交由上一层控制）
        //
        DbContext db = DbConfig.pc_user;

        Trans.tran(() -> {
            db.sql("insert into $.test(txt) values(?)", "cc").insert();
            db.sql("insert into $.test(txt) values(?)", "dd").execute();
            db.sql("insert into $.test(txt) values(?)", "ee").execute();

            db.sql("select name from $.user_info where user_id=3").getValue("");
        });
    }

    private static void tast_db2_tran() throws Throwable {
        //使用了 .await(true) 将不提交事务（交由上一层控制）
        //
        Trans.tranNew(()->{
            DbConfig.pc_user.sql("insert into $.test(txt) values(?)", "gg").execute();
        });
    }

    private static void tast_db3_tran() throws Throwable {
        //使用了 .await(true) 将不提交事务（交由上一层控制）
        //

        Trans.tran(() -> {
            DbConfig.pc_live.sql("insert into $.test(txt) values(?)", "xx").execute();

            throw new SQLException("xxxx");
        });
    }
}
