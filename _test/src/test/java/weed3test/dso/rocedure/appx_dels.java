package weed3test.dso.rocedure;

import org.noear.weed.DbContext;
import org.noear.weed.DbQueryProcedure;

public class appx_dels extends DbQueryProcedure {
    public appx_dels(DbContext context) {
        super(context);
        lazyload(()->{
            sql("delete from $.appx_copy where app_id=@{id1}; " +
                    "delete from $.appx_copy where app_id=@{id2}; " +
                    "delete from $.appx_copy where app_id=@{id3}");
            set("id1",app_id1);
            set("id2",app_id2);
            set("id3",app_id3);
        });
    }

    public int app_id1;
    public int app_id2;
    public int app_id3;
}
