package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3rdb.DbUtil;

public class _MetaTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception{

        db.dbTables().forEach(tw->{
            System.out.println("Table: "+tw.getName());
            tw.getColumns().forEach(cw->{
                System.out.print(cw.getName()+";");
            });
            System.out.println("");
        });

        System.out.println(db.dbTables().size());
        assert  db.dbTables().size() > 0;
    }
}
