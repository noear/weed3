package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.dso.rocedure.appx_get;
import weed3test.dso.rocedure.appx_get_byid;
import weed3test.model.AppxModel;

public class ProcedureTest {
    DbContext db2 = DbUtil.db;

    @Test
    public void test11() throws Exception {
        AppxModel m = db2.call("select * from appx where app_id=@{id}").set("id", 48).getItem(AppxModel.class);
        assert m.app_id == 48;
    }

    @Test
    public void test12() throws Exception {
        appx_get sp = new appx_get(db2);
        sp.app_id = 48;
        AppxModel m = sp.getItem(AppxModel.class);
        assert m.app_id == 48;
    }

    @Test
    public void test21() throws Exception {
        assert db2.call("appx_get_byid").set("_app_id", 22)
                .getItem(AppxModel.class).app_id == 22;
    }

    @Test
    public void test22() throws Exception {
        appx_get_byid sp = new appx_get_byid(db2);
        sp.app_id = 22;

        assert  sp.getItem(AppxModel.class).app_id == 22;
    }

    @Test
    public void test3() throws Exception {

    }

    @Test
    public void test4() throws Exception {

    }

    @Test
    public void test5() throws Exception {

    }
}
