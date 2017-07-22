package weed3demo.demo_plus.table;

import noear.weed.DbTable;
import weed3demo.config.DbConfig;

/**
 * Created by yuety on 2017/7/22.
 */
public class user_info extends DbTable {
    public user_info() {
        super(DbConfig.test);

        table("$.user_info u");
        set("userID", () -> userID);
        set("sex", () -> sex);
    }

    public Integer userID;
    public Integer sex;
}
