package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.*;
import weed3demo.render.AppxModel;
import weed3rdb.DbUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear 2021/4/2 created
 */
public class WhereTest {
    DbContext db2 = DbUtil.db;

    public void demo1(int type) throws SQLException {
        DbTableQuery qr = db2.table("appx");

        qr.whereTrue();

        if(type == 1){
            qr.andEq("app_id",1);
        }else{
            qr.andEq("type",2);
        }

        List<AppxModel> list = qr.selectList("*", AppxModel.class);
    }

    @Test
    public void demo2(){
        WhereQ whereQ = new WhereQ(db2);

        whereQ.whereTrue().andEq("type",1);

        System.out.println(whereQ.toSql());
    }

    public class WhereQ extends WhereBase<WhereQ>{
        public WhereQ(DbContext db){
            super(db);
        }

        public String toSql() {
            return _builder.toString();
        }
    }

}
