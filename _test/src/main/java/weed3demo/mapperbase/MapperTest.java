package weed3demo.mapperbase;

import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.BaseMapperWrap;
import org.noear.weed.DbContext;
import org.noear.weed.utils.TypeRef;
import weed3demo.DbUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapperTest {
    DbContext db2 = DbUtil.db;

    @Test
    public void test1() {
        BaseMapper<AppxModel> mapper = db2.mapperBase(AppxModel.class);
        tast_select(mapper);
    }

    @Test
    public void test2() {
        SqlMapper mapper = db2.mapper(SqlMapper.class);
        tast_select(mapper);
    }

    @Test
    public void test3() {
        SqlMapper mapper = db2.mapper(SqlMapper.class);

        String akey = mapper.appx_get_key(48);
        System.out.println(akey);

        assert "67adce3315124d6e93605d00ff0f11ee".equals(akey);
    }

    @Test
    public void test4() {
        SqlMapper mapper = db2.mapper(SqlMapper.class);

        System.out.println(mapper.toString());
        System.out.println(mapper.hashCode());
    }

    @Test
    public void testx() {
        SqlMapper mapper = db2.mapper(SqlMapper.class);

        mapper.appx_get_error();
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
        AppxModel m4 = mapper.selectOne(ent2);
        System.out.println("m4: " + m4);
        assert m4.agroup_id == ent2.agroup_id;

        //selectOne
        AppxModel m5 = mapper.selectOne(m -> m.where("app_id=21"));
        System.out.println("m5: " + m5);
        assert m5.app_id == 21;

        //selectObj
        Object m6 = mapper.selectObj("app_id", m -> m.where("app_id=21"));
        System.out.println("m6: " + m6);
        assert m6.equals(21);

        //selectMap
        Map m7 = mapper.selectMap(m -> m.where("app_id=21"));
        System.out.println("m7: " + m7);
        assert m7.size() > 10;

        Long m8 = mapper.selectCount(m -> m.where("agroup_id=1"));
        System.out.println("m8: " + m8);
        assert m8 > 20;


        //selectList
        List<AppxModel> m9 = mapper.selectList(m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m9: " + m9);
        assert m9.size() > 20;

        //selectMaps
        List<Map<String, Object>> m10 = mapper.selectMaps(m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m10: " + m10);
        assert m9.size() > 20;

        //selectObjs
        List<Object> m11 = mapper.selectObjs("app_key", m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m11: " + m11);
        assert m11.size() > 20;


        //selectPage
        List<AppxModel> m12 = mapper.selectPage(1, 10, m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m12: " + m12);
        assert m12.size() == 10;

        //selectMapsPage
        List<Map<String, Object>> m13 = mapper.selectMapsPage(1, 10, m -> m.whereEq("agroup_id", 1).andLt("app_id", 40));
        System.out.println("m13: " + m13);
        assert m13.size() == 10;
    }
}
