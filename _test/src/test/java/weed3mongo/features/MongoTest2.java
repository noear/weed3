package weed3mongo.features;

import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.noear.solon.Utils;
import org.noear.weed.WeedConfig;
import org.noear.weed.mongo.MgContext;
import weed3mongo.model.UserModel;

import java.util.List;
import java.util.Map;

/**
 * @author noear 2021/2/5 created
 */
public class MongoTest2 {
    String url = "mongodb://admin:admin@localhost";
    MgContext db = new MgContext(url, "demo");

    @BeforeClass
    public static void bef(){
        WeedConfig.isUsingUnderlineColumnName = false;
    }

    @Test
    public void test1() {
        db.table("user")
                .set("_id", Utils.guid())
                .set("userId", System.currentTimeMillis())
                .set("name", "noear")
                .insert();
    }

    @Test
    public void test2() {
        db.table("user")
                .set("_id", Utils.guid())
                .set("userId", 3)
                .set("type",1)
                .set("name", "noear")
                .insert();

        db.table("user")
                .set("_id", Utils.guid())
                .set("userId", 5)
                .set("type",1)
                .set("name", "noear")
                .insert();
    }

    @Test
    public void test3() {
        assert db.table("user")
                .set("name", "noear-update" + System.currentTimeMillis())
                .whereEq("userId", 5).andLk("name", "^no")
                .update() > 0;
    }

    @Test
    public void test4() {
        Map map =  db.table("user")
                .whereEq("userId", 5).andEq("type",1)
                .selectMap();

        System.out.println(map);
        assert (Integer) map.get("userId") == 5;
    }

    @Test
    public void test42() {
        UserModel user = db.table("user")
                .whereEq("userId", 5).andEq("type", 1)
                .selectItem(UserModel.class);

        System.out.println(user);
        assert user.userId == 5;
    }

    @Test
    public void test5() {
        List<Document> mapList =  db.table("user")
                .whereEq("type",1)
                .orderByAsc("userId")
                .limit(10)
                .selectMapList();

        System.out.println(mapList);

        assert mapList.size() == 10;

        System.out.println(mapList.get(1));
        assert mapList.get(1).get("userId",0) > 0;
    }

    @Test
    public void test52() {
        List<UserModel> mapList =  db.table("user")
                .whereEq("type",1)
                .orderByAsc("userId")
                .limit(10,10)
                .selectList(UserModel.class);

        System.out.println(mapList);

        assert mapList.size() == 10;

        System.out.println(mapList.get(1));
        assert mapList.get(1).userId > 0;
    }
}
