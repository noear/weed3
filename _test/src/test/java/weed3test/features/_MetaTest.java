package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.wrap.TableWrap;
import weed3builder.EntityBuilder;
import weed3test.DbUtil;

public class _MetaTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() {
        for (TableWrap tw : db.dbTables()) {
            String code = EntityBuilder.buildByTable(null, tw);
            System.out.println(code);
            //break;
        }
    }

    @Test
    public void test2(){
        System.out.println(System.getProperty("user.dir"));
    }

    @Test
    public void test3() throws Exception{
//       EntityBuilder.createByDb("demo",db);
    }
}
