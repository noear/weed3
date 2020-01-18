package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;

public class _ExeTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception{
        //db.exe("val::SELECT COUNT(*) num FROM water_msg_message WHERE log_date=20200101");
    }
}
