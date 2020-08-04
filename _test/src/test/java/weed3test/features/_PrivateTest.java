package weed3test.features;

import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.model.AgroupModelEx;
import weed3test.model.Appx2Model;


public class _PrivateTest {

    DbContext db = DbUtil.db;

    @Test
    public void test1() throws Exception{
        Appx2Model tmp =  db.table("appx").whereEq("app_id",1).select("*").getItem(Appx2Model.class);

        assert tmp.getApp_id() == 1;
    }

    @Test
    public void test2() throws Exception{
        BaseMapper<Appx2Model> mapper = db.mapperBase(Appx2Model.class);

        Appx2Model tmp =  mapper.selectById(1);

        assert tmp.getApp_id() == 1;
    }

    @Test
    public void test3() throws Exception{
        BaseMapper<AgroupModelEx> mapper = db.mapperBase(AgroupModelEx.class);

        AgroupModelEx tmp =  mapper.selectById(1);

        assert tmp.getAgroup_id() == 1;
        assert tmp.getTag() != null;
    }
}
