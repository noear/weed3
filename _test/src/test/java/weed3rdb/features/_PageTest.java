package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import weed3rdb.DbUtil;
import webapp.model.AppxModel;

import java.util.List;

public class _PageTest {
    DbContext db2 = DbUtil.db;

    BaseMapper<AppxModel> mapper = db2.mapperBase(AppxModel.class);

    @Test
    public void test_top(){
        assert  mapper.selectById(22).app_id == 22;
        System.out.println(db2.lastCommand.text);
    }

    @Test
    public void test_page() throws Exception {

        List<AppxModel> list = mapper.selectList(0, 10, q->q.orderByAsc(AppxModel::getApp_id));
        assert list.size() == 10;
        assert list.get(0).app_id == 1;

        System.out.println(db2.lastCommand.text);
    }

    @Test
    public void test_page2() throws Exception{
        List<AppxModel> list =  mapper.selectList(1,10,q->q.orderByAsc(AppxModel::getApp_id));
        assert  list.size() == 10;
        assert list.get(0).app_id == 2;

        System.out.println(db2.lastCommand.text);
    }

    @Test
    public void test_page3() throws Exception {
        List<AppxModel> list = db2.table("appx a")
                .leftJoin("appx_agroup b").onEq("a.agroup_id", "b.agroup_id")
                .orderByAsc("a.app_id")
                .limit(1, 10)
                .selectList("a.*,b.name agroup_name", AppxModel.class);

        assert list.size() == 10;
        assert list.get(0).app_id == 2;

        System.out.println(db2.lastCommand.text);
    }
}
