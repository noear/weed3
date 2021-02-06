package weed3rdb.features;

import org.junit.Before;
import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import weed3rdb.DbUtil;
import webapp.model.AppxModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapperTest {
    DbContext db2 = DbUtil.db;
    BaseMapper<AppxModel> mapper;

    @Before
    public void test_bef(){
        mapper = db2.mapperBase(AppxModel.class);
    }

    @Test
    public void test1() {

        System.out.println(mapper.toString());
        System.out.println(mapper.hashCode());
    }

    @Test
    public void test2() throws Exception {
        Object temp = db2.table("appx").whereEq("app_id", 48).select("*").getMap();

        assert temp instanceof Map;
    }




    @Test
    public void tast_select_m1() {
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

    }

    @Test
    public void test_select_m4(){
        AppxModel ent2 = new AppxModel();
        ent2.agroup_id = 1;

        //selectOne
        AppxModel m4 = mapper.selectItem(ent2);
        System.out.println("m4: " + m4);
        assert m4.agroup_id == ent2.agroup_id;

        //selectOne
        AppxModel m5 = mapper.selectItem(m -> m.whereEq(AppxModel::getApp_id, 21));
        System.out.println("m5: " + m5);
        assert m5.app_id == 21;

    }

    @Test
    public void test_select_m6(){
        //selectObj
        Object m6 = mapper.selectValue("app_id", m -> m.whereEq(AppxModel::getApp_id, 21));
        System.out.println("m6: " + m6);
        assert ((Number)m6).longValue() == (21);
    }

    @Test
    public void test_select_m7(){
        //selectMap
        Map m7 = mapper.selectMap(m -> m.whereEq("app_id",21));
        System.out.println("m7: " + m7);
        assert m7.size() > 10;

        Long m8 = mapper.selectCount(m -> m.whereEq("agroup_id",1));
        System.out.println("m8: " + m8);
        assert m8 > 20;
    }

    @Test
    public void test_select_list_m9() {
        //selectList
        List<AppxModel> m9 = mapper.selectList(m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m9: " + m9);
        assert m9.size() > 20;

        //selectMaps
        List<Map<String, Object>> m10 = mapper.selectMapList(m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m10: " + m10);
        assert m9.size() > 20;
    }

    @Test
    public void test_select_list_m11() {
        //selectObjs
        List<Object> m11 = mapper.selectArray("app_key", m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m11: " + m11);
        assert m11.size() > 20;
    }

    @Test
    public void test_select_page_m12() {
        //selectPage
        List<AppxModel> m12 = mapper.selectPage(1, 10, m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m12: " + m12);
        assert m12.size() == 10;
    }

    @Test
    public void test_select_page_m13() {
        //selectMapsPage
        List<Map<String, Object>> m13 = mapper.selectMapPage(1, 10, m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m13: " + m13);
        assert m13.size() == 10;
    }

    @Test
    public void test_select_top_m14() {
        //selectPage
        List<AppxModel> m14 = mapper.selectTop(5, m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m14: " + m14);
        assert m14.size() == 5;
    }

    @Test
    public void test_select_top_m15() {
        //selectMapsPage
        List<Map<String, Object>> m15 = mapper.selectMapTop(5, m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m15: " + m15);
        assert m15.size() == 5;
    }
}
