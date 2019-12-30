package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.tool.EntityGenerator;
import org.noear.weed.tool.MapperGenerator;
import org.noear.weed.wrap.TableWrap;
import weed3test.DbUtil;

public class _MetaTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() {
        for (TableWrap tw : db.dbTables()) {
            String code = EntityGenerator.buildByTable("demo.model", tw, tw.getName());
            System.out.println(code);
            break;
        }
    }

    @Test
    public void test2() {
        for (TableWrap tw : db.dbTables()) {
            String code = MapperGenerator.buildByTable("demo.model", "demo.dso.mapper", tw, tw.getName());
            System.out.println(code);
            break;
        }
    }

    @Test
    public void test11() throws Exception {
//        EntityGenerator.createByDb("demo.model",db);
//        MapperGenerator.createByDb("demo.model", "demo.dso.mapper",db);
    }
}
