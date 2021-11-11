package weed3rdb._bak;

import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import weed3rdb.DbUtil;
import weed3rdb.dso.SqlMapper;
import webapp.model.AppxModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapperTest2 {
    DbContext db2 = DbUtil.db;
    BaseMapper<AppxModel> mapper = db2.mapperBase(AppxModel.class);

    @Test
    public void test1() {
        BaseMapper<AppxModel> mapper = db2.mapperBase(AppxModel.class);
        tast_select(mapper);
        test_select_list(mapper);
        test_select_page(mapper);
        test_select_top(mapper);
    }

    @Test
    public void test2() throws Exception{
        SqlMapper mapper = db2.mapper(SqlMapper.class);
        tast_select(mapper);
        test_select_list(mapper);
        test_select_page(mapper);
        test_select_top(mapper);

        //assert  mapper.appx_get2(22).app_id == 22;
    }


    public void test3() {
        SqlMapper mapper = db2.mapper(SqlMapper.class);

        String akey = mapper.appx_get_key(48);
        System.out.println(akey);

        assert "67adce3315124d6e93605d00ff0f11ee".equals(akey);

        akey = mapper.appx_get_key2(48);
        System.out.println(akey);

        assert "67adce3315124d6e93605d00ff0f11ee".equals(akey);


        akey = mapper.appx_get_key3(48);
        System.out.println(akey);

        assert "67adce3315124d6e93605d00ff0f11ee".equals(akey);
    }

    @Test
    public void test32() {
        SqlMapper mapper = db2.mapper(SqlMapper.class);
        int agroup_id =1;

        List<Integer> ids = new ArrayList<>();
        ids.add(12);
        ids.add(48);

        List<String> akeyAry = mapper.appx_get_key_list(agroup_id, ids);

        assert akeyAry.size()==2;

        akeyAry = mapper.appx_get_key_list2(agroup_id, ids);

        assert akeyAry.size()==2;
    }

    @Test
    public void test4() {
        SqlMapper mapper = db2.mapper(SqlMapper.class);

        System.out.println(mapper.toString());
        System.out.println(mapper.hashCode());
    }

    @Test
    public void test5() throws Exception{
        SqlMapper mapper = db2.mapper(SqlMapper.class);

        System.out.println(mapper.test());
    }

    @Test
    public void test6() throws Exception {
        SqlMapper mapper = db2.mapper(SqlMapper.class);

        DbUtil.cache.getBy(12, "test", (cu) -> mapper.test());

        System.out.println(mapper.test());
    }

    @Test
    public void test7() throws Exception {
        Object temp = db2.table("appx").whereEq("app_id",48).select("*").getMap();

        assert temp instanceof Map;
    }

    @Test
    public void testx() {
        SqlMapper mapper = db2.mapper(SqlMapper.class);

        try {
            mapper.appx_get_error();
        }catch (Exception ex){
           assert ex instanceof RuntimeException;
        }
    }




    private void tast_select(BaseMapper<AppxModel> mapper) {
        List<Object> ary = new ArrayList<>();
        ary.add(12);
        ary.add(21);
        ary.add(48);

        Map<String, Object> arg = new HashMap<>();
        arg.put("agroup_id", 1);
        arg.put("ar_is_setting", 1);

        AppxModel ent = new AppxModel();
        ent.app_id = 48;

        AppxModel ent2 = new AppxModel();
        ent2.agroup_id = 1;

        //selectById
        AppxModel m1 = mapper.selectById(48);
        System.out.println("m1: " + m1);
        assert m1.app_id == 48;

        //selectByIds
        List<AppxModel> m2 = mapper.selectByIds(ary);
        System.out.println("m2: " + m2);
        assert m2.size() == 3;

        //selectByMap
        List<AppxModel> m3 = mapper.selectByMap(arg);
        System.out.println("m3: " + m3);
        assert m3.size() == 6;


        //selectOne
        AppxModel m4 = mapper.selectItem(ent2);
        System.out.println("m4: " + m4);
        assert m4.agroup_id == ent2.agroup_id;

        //selectOne
        AppxModel m5 = mapper.selectItem(m -> m.whereEq(AppxModel::getApp_id,21));
        System.out.println("m5: " + m5);
        assert m5.app_id == 21;

        //selectObj
        Object m6 = mapper.selectValue("app_id", m-> m.whereEq(AppxModel::getApp_id,21));
        System.out.println("m6: " + m6);
        assert m6.equals(21);

        //selectMap
        Map m7 = mapper.selectMap(m -> m.where("app_id=21"));
        System.out.println("m7: " + m7);
        assert m7.size() > 10;

        Long m8 = mapper.selectCount(m -> m.where("agroup_id=1"));
        System.out.println("m8: " + m8);
        assert m8 > 20;
    }


    public void test_select_list(BaseMapper<AppxModel> mapper){
        //selectList
        List<AppxModel> m9 = mapper.selectList(m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m9: " + m9);
        assert m9.size() > 20;

        //selectMaps
        List<Map<String, Object>> m10 = mapper.selectMapList(m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m10: " + m10);
        assert m9.size() > 20;

        //selectObjs
        List<Object> m11 = mapper.selectArray("app_key", m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m11: " + m11);
        assert m11.size() > 20;
    }


    public void  test_select_page(BaseMapper<AppxModel> mapper){
        //selectPage
        List<AppxModel> m12 = mapper.selectList(1, 10, m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m12: " + m12);
        assert m12.size() == 10;

        //selectMapsPage
        List<Map<String, Object>> m13 = mapper.selectMapList(1, 10, m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m13: " + m13);
        assert m13.size() == 10;
    }

    public void  test_select_top(BaseMapper<AppxModel> mapper){
        //selectPage
        List<AppxModel> m12 = mapper.selectTop(5, m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m12: " + m12);
        assert m12.size() == 5;

        //selectMapsPage
        List<Map<String, Object>> m13 = mapper.selectMapTop(5, m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m13: " + m13);
        assert m13.size() == 5;
    }
}
