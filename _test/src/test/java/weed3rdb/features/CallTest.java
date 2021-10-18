package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.wrap.DbType;
import weed3rdb.DbUtil;
import webapp.model.AppxModel;

public class CallTest {
    DbContext db2 = DbUtil.db;

    @Test
    public void test11() throws Exception {
        String code = null;
        if (db2.getType() == DbType.Oracle) {
            code = "select * from \"$\".\"APPX\" where \"app_id\"=@{id}";
        } else {
            code = "select * from $.appx where app_id=@{id}";
        }

        AppxModel m = db2.call(code)
                .set("id", 48)
                .getItem(AppxModel.class);

        assert m.app_id == 48;
    }

//    @Test
//    public void test21() throws Exception {
//        if (db2.dbType() == DbType.MySQL) {
//            AppxModel app = db2.call("appx_get_byid").set("_app_id", 2)
//                    .getItem(AppxModel.class);
//
//            assert app.app_id == 22;
//        }
//    }
}
