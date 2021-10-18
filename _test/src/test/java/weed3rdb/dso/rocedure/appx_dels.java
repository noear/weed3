package weed3rdb.dso.rocedure;

import org.noear.weed.DbContext;
import org.noear.weed.DbQueryProcedure;
import org.noear.weed.wrap.DbType;

public class appx_dels extends DbQueryProcedure {
    public appx_dels(DbContext context) {
        super(context);
        lazyload(()->{
            if(context.getType() == DbType.Oracle){
                sql("delete from \"$\".\"APPX_COPY\" where \"app_id\"=@{id1}; " +
                        "delete from \"$\".\"APPX_COPY\" where \"app_id\"=@{id2}; " +
                        "delete from \"$\".\"APPX_COPY\" where \"app_id\"=@{id3}");
            }else{
                sql("delete from $.appx_copy where app_id=@{id1}; " +
                        "delete from $.appx_copy where app_id=@{id2}; " +
                        "delete from $.appx_copy where app_id=@{id3}");
            }

            set("id1",app_id1);
            set("id2",app_id2);
            set("id3",app_id3);
        });
    }

    public int app_id1;
    public int app_id2;
    public int app_id3;
}
