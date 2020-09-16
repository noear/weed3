package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.dso.rocedure.appx_dels;
import weed3test.dso.rocedure.appx_get;
import weed3test.dso.rocedure.appx_get_byid;
import webapp.model.AppxModel;

public class ProcedureTest {
    DbContext db2 = DbUtil.db;

    @Test
    public void test12() throws Exception {
        appx_get sp = new appx_get(db2);
        sp.app_id = 48;
        AppxModel m = sp.getItem(AppxModel.class);
        assert m.app_id == 48;
    }

    @Test
    public void test13() throws Exception {
        appx_dels sp = new appx_dels(db2);
        sp.app_id1 = 1;
        sp.app_id2 = 2;
        sp.app_id3 = 3;

        sp.execute();
    }

    public void test22() throws Exception {
        appx_get_byid sp = new appx_get_byid(db2);
        sp.app_id = 22;

        assert  sp.getItem(AppxModel.class).app_id == 22;
    }
}
