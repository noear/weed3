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
        StringBuilder sb = new StringBuilder();

        for (TableWrap tw : db.dbTables()) {
            sb.setLength(0);

            System.out.println(tw.getName());
            System.out.println("::pk:" + tw.getPk1());
            for(ColumnWrap cw : tw.getColumns()){
                System.out.println("::::col:" + cw.getName() + "-" + cw.getTypeName());

            }
        }
    }

    public void test2(){

    }
}
