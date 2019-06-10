package weed3demo.demo.sqlbuilder;

import noear.weed.DbContext;
import noear.weed.SQLBuilder;
import weed3demo.config.DbConfig;
import weed3demo.demo.model.UserInfoModel;
import weed3demo.demo_plus.table.UserInfoM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by noear on 2017/7/22.
 */
public class demo {
    //
    // mysql批量操作需要数据库连接配置allowMultiQueries=true
    //
    static DbContext db = DbConfig.pc_user;

    public void demo1_insert_batch() throws SQLException{
        List<UserInfoModel> list  = new ArrayList<>();
        SQLBuilder sb = new SQLBuilder();

        for(UserInfoModel m : list) {
            sb.append("insert into user_info(user_id,mobile,icon) values(?,?,?);", m.user_id, m.mobile, m.icon);
        }

        db.sql("").getCount();

        db.sql(sb);
    }

    public void demo1_insert_batch2() throws SQLException{ //另一种批量插入的写法
        List<UserInfoModel> list  = new ArrayList<>();
        SQLBuilder sb = new SQLBuilder();

        sb.append("insert into user_info(user_id,mobile,icon) ");
        sb.append("values");

        for(UserInfoModel m : list) {
            sb.append("(?,?,?),", m.user_id, m.mobile, m.icon);
        }

        sb.removeLast();
        sb.append(";");

        db.sql(sb);
    }

    public void demo2_update_batch() throws SQLException{
        List<UserInfoModel> list  = new ArrayList<>();

        SQLBuilder sb = new SQLBuilder();

        for(UserInfoModel m : list) {
            sb.append("update user_info set mobile=?,icon=? where user_id=?;", m.mobile, m.icon, m.user_id);
        }

        db.sql(sb);
    }

    public void demo3_delete_batch() throws SQLException{
        List<UserInfoModel> list  = new ArrayList<>();

        SQLBuilder sb = new SQLBuilder();

        for(UserInfoModel m : list) {
            sb.append("delete from user_info where user_id=?;", m.user_id);
        }

        db.sql(sb);
    }
}
