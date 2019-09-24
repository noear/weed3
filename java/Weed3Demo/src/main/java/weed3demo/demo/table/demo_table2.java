package weed3demo.demo.table;

import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import weed3demo.config.DbConfig;
import weed3demo.demo.model.UserInfoModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by noear on 2017/7/22.
 */
public class demo_table2 {

    static DbContext db = DbConfig.pc_user;

    public static void demo_expr1() throws SQLException {
          //连式处理::对不确定字段的插入
          db.table("test")
            .expre(tb -> {
                tb.set("name", "xxx");

                if (1 == 2) {
                    tb.set("mobile", "xxxx");
                } else {
                    tb.set("icon", "xxxx");
                }
            }).insert();
    }

    public static void demo_expr2() throws SQLException {
          //连式处理::对不确定的条件拼装
          db.table("test")
            .expre(tb -> {
                tb.where("1=1");

                if (1 == 2) {
                    tb.and("mobile=?", "xxxx");
                } else {
                    tb.and("icon=?", "xxxx");
                }
            }).select("*");
    }

}
