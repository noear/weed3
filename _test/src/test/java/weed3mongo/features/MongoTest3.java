package weed3mongo.features;

import org.junit.BeforeClass;
import org.junit.Test;
import org.noear.weed.WeedConfig;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.mongo.MgContext;
import weed3mongo.model.UserModel;

import java.util.Arrays;
import java.util.List;

/**
 * @author noear 2021/2/6 created
 */
public class MongoTest3 {
    String url = "mongodb://admin:admin@localhost";
    MgContext db = new MgContext(url, "demo");

    @BeforeClass
    public static void bef(){
        WeedConfig.isUsingUnderlineColumnName = false;
    }

    @Test
    public void test1(){
        List<UserModel> mapList =  db.table("user")
                .whereBtw("userId", 10,20)
                .orderByAsc("userId")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() == 10;
        assert mapList.get(0).userId == 10;
    }

    @Test
    public void test12(){
        ICacheServiceEx cache = new LocalCache();

        for(int i=0 ; i< 3; i++) {
            List<UserModel> mapList = db.table("user")
                    .whereBtw("userId", 10, 20)
                    .orderByAsc("userId")
                    .limit(10)
                    .caching(cache)
                    .selectList(UserModel.class);

            assert mapList.size() == 10;
            assert mapList.get(0).userId == 10;
        }
    }

    @Test
    public void test2(){
        List<UserModel> mapList =  db.table("user")
                .whereIn("userId", Arrays.asList(3,4))
                .orderByAsc("userId")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() > 2;
        assert mapList.get(0).userId == 3;
    }

    @Test
    public void test22(){
        List<UserModel> mapList =  db.table("user")
                .whereNlk("name", "^no")
                .orderByAsc("userId")
                .limit(10)
                .selectList(UserModel.class);

       System.out.println(mapList);
       assert mapList.size() == 0;
    }

    //@Test
    public void test3(){
        //需要服务器开启脚本能力
        List<UserModel> mapList =  db.table("user")
                .whereScript("this.userId==3")
                .orderByAsc("userId")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() > 2;
        assert mapList.get(0).userId == 3;
    }

    @Test
    public void test4(){
        List<UserModel> mapList =  db.table("user")
                .whereMod("userId", 3,1)
                .orderByAsc("userId")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() > 2;
        assert mapList.get(0).userId == 1;
    }
}
