package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.wrap.ColumnWrap;
import org.noear.weed.wrap.TableWrap;
import weed3test.DbUtil;
import weed3test.dso.JavaDbType;

import java.sql.Timestamp;
import java.util.Date;

public class _MetaTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() {

        for (TableWrap tw : db.dbTables()) {
            String code = EntityBuilder.buildByTable(tw);
            System.out.println(code);
            //break;
        }
    }

    public void test2(){

    }
}
