package weed3demo.demo.tran;

import org.noear.weed.xml.XmlSqlProxy;
import weed3demo.config.DbConfig;
import weed3demo.xmlsql.DbUserApi;

import java.sql.SQLException;

public class Tran3Demo {
    /*所有的执行在一个事务控制范围内*/
    private static void test_db1_tran() throws SQLException {
        DbUserApi dbUserApi = XmlSqlProxy.getSingleton(DbUserApi.class);

        //1.简单处理
        DbConfig.pc_user.tran((t) -> {
            //
            // 此表达式内的操作，会自动加入事务（3.2.0.3 开始支持）
            //
            t.db().sql("insert into test(txt) values(?)", "cc").insert();
            t.db().sql("update test set txt='1' where id=1").execute();

            t.db().table("a_config").set("ser_id",1).insert();

            dbUserApi.user_add(12);
        });
    }
}
