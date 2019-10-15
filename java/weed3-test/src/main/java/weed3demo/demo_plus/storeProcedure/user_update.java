package weed3demo.demo_plus.storeProcedure;

import org.noear.weed.DbQueryProcedure;
import org.noear.weed.DbStoredProcedure;
import weed3demo.config.DbConfig;

import java.util.Date;

/**
 * Created by noear on 2017/7/22.
 */
public class user_update extends DbStoredProcedure {
    public user_update() {
        super(DbConfig.test);

        call("user_update");
        set("_userID", () -> userID);
        set("_city", () -> city);
        set("_vipTime", () -> vipTime);
    }

    public long userID;
    public String city;
    public Date vipTime;
}
