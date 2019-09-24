package weed3demo.demo.table;

import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;
import weed3demo.config.DbConfig;
import weed3demo.demo.model.UserInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by noear on 2017/7/22.
 */
public class demo_table_inserlist {
    static DbContext db = DbConfig.pc_user;

    public void demo_insertlist() throws SQLException {
        List<UserInfoModel> list = new ArrayList<>();

        db.table("user").insertList(list, (d, m) -> {
            m.set("city_id", d.city_id);
            m.set("name", d.name);
            m.set("mobile", d.mobile);
            m.set("icon", d.icon);
            m.set("role", d.role);
        });
    }

    public void demo_insertlist2() throws SQLException {
        DataList list = new DataList();

        db.table("user").insertList(list.getRows());
    }
}
