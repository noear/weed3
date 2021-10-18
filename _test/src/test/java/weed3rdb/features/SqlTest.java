package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.wrap.DbType;
import weed3rdb.DbUtil;
import webapp.model.AppxModel;

import java.util.HashMap;
import java.util.Map;

public class SqlTest {
    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception {
        String code = null;
        if(db.getType() == DbType.Oracle){
            code = "select * from \"$\".\"APPX\" where \"app_id\"=?";
        }else{
            code = "select * from $.appx where app_id=?";
        }
        try {
            assert db.sql(code, 32)
                    .getItem(AppxModel.class)
                    .app_id == 32;
        }finally {
            System.out.println(db.lastCommand.text);
        }
        
    }

    public void test2() throws Exception {
        Map<String, Object> map = new HashMap<>();

        db.table("appx").setMap(map).upsert("akey");
    }
}
