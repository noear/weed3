package weed3demo.demo_plus.queryProcedure_lazyload;

import org.noear.weed.DbQueryProcedure;
import weed3demo.config.DbConfig;

/**
 * Created by noear on 2017/7/22.
 */

//如果原来是存储过程的代码，可以通过[DbQueryProcedure]快速切换过来

public class user_get_list2 extends DbQueryProcedure {
    public user_get_list2() {
        super(DbConfig.test);

        lazyload(()->{
            sql("select * from user where userID=@userID and sex=@sex");
            set("userID",  userID);
            set("sex", sex);
        });
    }

    public long userID;
    public int sex;
}
