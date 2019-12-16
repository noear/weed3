package weed3test.dso.rocedure;

import org.noear.weed.DbContext;
import org.noear.weed.DbQueryProcedure;

public class appx_get extends DbQueryProcedure {
    public appx_get(DbContext context) {
        super(context);
        lazyload(()->{
            sql("select * from appx where app_id=@{id}");
            set("id",app_id);
        });
    }

    public int app_id;
}
