package weed3demo.demo_plus.table;

import noear.weed.DataList;
import noear.weed.DbContext;
import weed3demo.config.DbConfig;
import weed3demo.demo.model.UserInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuety on 2017/7/22.
 */
public class demo_table_inserlist {
    static DbContext db = DbConfig.pc_user;

    public void demo_insertlist10() throws SQLException {
        List<UserInfoModel> list = new ArrayList<>();

        UserM tb = new UserM();

        tb.insertList(list, (d, m) -> {
            m.set("city_id", d.city_id);
            m.set("name", d.name);
            m.set("mobile", d.mobile);
            m.set("icon", d.icon);
            m.set("role", d.role);
        });
    }

    public void demo_insertlist12() throws SQLException {
        DataList list = new DataList();

        UserM tb = new UserM();

        tb.insertList(list.getRows());
    }
}
