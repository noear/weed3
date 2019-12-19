package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.ext.DatabaseType;
import weed3test.DbUtil;
import weed3test.model.AppxModel;

public class CallTest {
    DbContext db2 = DbUtil.db;

    @Test
    public void test11() throws Exception {
        AppxModel m = db2.call("select * from $.appx where app_id=@{id}")
                .set("id", 48)
                .getItem(AppxModel.class);
        assert m.app_id == 48;
    }

    @Test
    public void test21() throws Exception {
        if(db2.databaseType() == DatabaseType.MySQL) {
            assert db2.call("appx_get_byid").set("_app_id", 22)
                    .getItem(AppxModel.class).app_id == 22;
        }
    }

}
