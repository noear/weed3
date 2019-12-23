package weed3test;

import org.noear.weed.DbContext;
import weed3test.render.TestMapper;

public class Test2App {
    public static void main(String[] args){
        DbContext db = DbUtil.db;

        TestMapper mapper = db.mapper(TestMapper.class);



        System.out.println(mapper.appx_get_ftl(5));

        System.out.println(mapper.appx_get_beetl(5));

        System.out.println(mapper.appx_get_enjoy(5));

        System.out.println(mapper.appx_get_velocity(5));
    }
}
