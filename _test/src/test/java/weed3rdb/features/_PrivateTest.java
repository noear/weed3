package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import weed3rdb.DbUtil;
import webapp.model.AgroupModelEx;
import webapp.model.Appx2Model;


public class _PrivateTest {

    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception{
        Appx2Model tmp =  db.table("appx").whereEq("app_id",1).select("*").getItem(Appx2Model.class);

        assert tmp.getAppId() == 1;
        assert tmp.getAppKey() != null;
    }

    @Test
    public void test2() throws Exception{
        BaseMapper<Appx2Model> mapper = db.mapperBase(Appx2Model.class);

        Appx2Model tmp =  mapper.selectById(1);

        assert tmp.getAppId() == 1;
    }

    @Test
    public void test3() throws Exception{
        BaseMapper<AgroupModelEx> mapper = db.mapperBase(AgroupModelEx.class);

        AgroupModelEx tmp =  mapper.selectById(1);

        assert tmp.getAgroup_id() == 1;
        assert tmp.getTag() != null;
    }
}
