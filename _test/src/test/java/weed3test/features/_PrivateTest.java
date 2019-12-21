package weed3test.features;

import lombok.Data;
import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import org.noear.weed.annotation.Column;
import org.noear.weed.annotation.Exclude;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;
import weed3test.DbUtil;

import java.util.Date;

public class _PrivateTest {

    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception{
        Appx2Model tmp =  db.table("appx").whereEq("app_id",1).select("*").getItem(Appx2Model.class);

        assert tmp.app_id == 1;
    }

    @Test
    public void test2() throws Exception{
        BaseMapper<Appx2Model> mapper = db.mapperBase(Appx2Model.class);

        Appx2Model tmp =  mapper.selectById(1);

        assert tmp.app_id == 1;
    }

    @Data
    @Table("appx")
    public static class Appx2Model {
        /** 应用ID */
        @PrimaryKey
        @Column("app_id")
        private Integer app_id;
        /** 应用访问KEY */
        private String app_key;
        /** （用于取代app id 形成的唯一key） //一般用于推广注册之类 */
        private String akey;

    }
}
