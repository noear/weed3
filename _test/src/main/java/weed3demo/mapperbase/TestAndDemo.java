package weed3demo.mapperbase;

import org.junit.Test;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import org.noear.weed.utils.TypeRef;
import weed3demo.DbUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAndDemo {
    DbContext db2 = DbUtil.db;

    @Test
    public void test1() {
        BaseMapper<AppxModel> mapper = db2.mapper(new TypeRef<AppxModel>() {
        });
        tast_do(mapper);
    }

    @Test
    public void test2() {
        SqlMapper mapper = db2.mapper(SqlMapper.class);
        tast_do(mapper);
    }

    private void tast_do(BaseMapper<AppxModel> mapper) {
        List<Object> ary = new ArrayList<>();
        ary.add(12);
        ary.add(21);
        ary.add(48);

        Map<String, Object> arg = new HashMap<>();
        arg.put("agroup_id", 1);
        arg.put("ar_is_setting", 1);

        AppxModel m1 = mapper.selectById(48);
        System.out.println("m1: " + m1);
        assert m1.app_id == 48;

        List<AppxModel> m2 = mapper.selectByIds(ary);
        System.out.println("m2: " + m2);
        assert m2.size() == 3;

        List<AppxModel> m3 = mapper.selectByMap(arg);
        System.out.println("m3: " + m3);
        assert m3.size() == 6;
    }
}
