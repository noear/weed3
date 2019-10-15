package weed3demo.demo_plus.storeProcedure;

import org.noear.weed.DbStoredProcedure;
import weed3demo.config.DbConfig;

/**
 * Created by noear on 2017/7/22.
 */
public class user_get extends DbStoredProcedure {
    public user_get() {
        super(DbConfig.test);

        call("user_get");
        set("_userID", () -> userID);
    }

    public long userID;
}
