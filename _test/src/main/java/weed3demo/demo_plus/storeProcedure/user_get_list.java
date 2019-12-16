package weed3demo.demo_plus.storeProcedure;

import org.noear.weed.DbStoredProcedure;
import weed3demo.config.DbConfig;

/**
 * Created by noear on 2017/7/22.
 */
public class user_get_list extends DbStoredProcedure {
    public user_get_list() {
        super(DbConfig.test);

        lazyload(()->{
            set("_userID",  userID);
            set("_sex", sex);
        });
    }

    public long userID;
    public int sex;
}
