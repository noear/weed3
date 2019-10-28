package weed3demo.demo.procedure;

import org.noear.weed.DbContext;
import weed3demo.config.DbConfig;
import weed3demo.demo.model.UserInfoModel;

import java.sql.SQLException;

/**
 * Created by noear on 2017/7/22.
 */
public class Store1Demo {
    //1.存储过程::
    public static void demo_select() throws SQLException{
        DbContext db = DbConfig.pc_bcf;

        db.call("user_get")
                .set("_user_id",1).getItem(new UserInfoModel());
    }

    public static void demo_updateOrInsert() throws SQLException{
        DbContext db = DbConfig.pc_bcf;

        db.call("user_update")
                .set("_useer_id",11)
                .set("_age", 12).execute();
    }

    //2.查询过程::
    public static void demo_select2() throws SQLException{
        DbContext db = DbConfig.pc_bcf;

        db.call("select * from user user_id=@user_id") //查询过程的变量，须使用@号开头
                .set("@user_id",1)
                .getItem(new UserInfoModel());
    }

    public static void demo_update2() throws SQLException{
        DbContext db = DbConfig.pc_bcf;

        db.call("update user set age=@age where user_id=@user_id")
                .set("@useer_id",11)
                .set("@age", 12).execute();
    }
}
