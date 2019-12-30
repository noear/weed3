package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.tool.EntityBuilder;
import org.noear.weed.wrap.TableWrap;
import weed3test.DbUtil;

public class _MetaTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() {
        for (TableWrap tw : db.dbTables()) {
            String code = EntityBuilder.buildByTable("demo", tw, tw.getName());
            System.out.println(code);
            break;
        }
    }

    @Test
    public void test2() throws Exception {
//       EntityBuilder.createByDb("demo",db);
    }
}
