package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import webapp.model.AppxModel;
import weed3rdb.DbUtil;
import weed3rdb.dso.SqlMapper;

import java.sql.SQLException;

/**
 * @author noear 2021/8/27 created
 */
public class NullTest {
    DbContext db2 = DbUtil.db;
    BaseMapper<AppxModel> mapper = db2.mapperBase(AppxModel.class);

    @Test
    public void test() throws SQLException {
        AppxModel temp = db2.table("appx")
                .whereEq("app_id", Integer.MAX_VALUE)
                .selectItem("*", AppxModel.class);

        assert temp != null;

        WeedConfig.isSelectItemEmptyAsNull = true;

        AppxModel temp2 = db2.table("appx")
                .whereEq("app_id", Integer.MAX_VALUE)
                .selectItem("*", AppxModel.class);

        WeedConfig.isSelectItemEmptyAsNull = false;

        assert temp2 == null;
    }


    @Test
    public void test2() throws SQLException {
        AppxModel temp = mapper.selectById(Integer.MAX_VALUE);

        assert temp != null;

        WeedConfig.isSelectItemEmptyAsNull = true;

        AppxModel temp2 = mapper.selectById(Integer.MAX_VALUE);

        WeedConfig.isSelectItemEmptyAsNull = false;

        assert temp2 == null;
    }

}
