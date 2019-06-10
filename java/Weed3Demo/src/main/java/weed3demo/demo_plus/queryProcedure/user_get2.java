package weed3demo.demo_plus.queryProcedure;

import noear.weed.DbQueryProcedure;
import noear.weed.DbStoredProcedure;
import weed3demo.config.DbConfig;

/**
 * Created by noear on 2017/7/22.
 */

//如果原来是存储过程的代码，可以通过[DbQueryProcedure]快速切换过来

public class user_get2 extends DbQueryProcedure {
    public user_get2() {
        super(DbConfig.test);

        sql("select * from user where userID=@userID");
        set("@userID", () -> userID);
    }

    public long userID;
}
