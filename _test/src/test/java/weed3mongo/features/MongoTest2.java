package weed3mongo.features;

import org.junit.Test;
import org.noear.solon.Utils;
import org.noear.weed.mongo.MgContext;
import weed3mongo.model.UserModel;

import java.util.List;
import java.util.Map;

/**
 * @author noear 2021/2/5 created
 */
public class MongoTest2 {
    String serverIp = "172.168.0.162";
    int serverPort = 27017;

    MgContext db = new MgContext(serverIp, serverPort, "demo");

    @Test
    public void init() {
        for (int i = 0; i < 100; i++) {
            db.table("user")
                    .set("id", i)
                    .set("type", 1)
                    .set("name", "noear")
                    .set("nickname", "xidao")
                    .insert();


            UserModel userDo = new UserModel();
            userDo.id = i;
            userDo.type = 2;
            userDo.name = "noear";
            userDo.nickname = "xidao";

            db.table("user").setEntity(userDo)
                    .insert();
        }
    }

    @Test
    public void test1() {
        db.table("user")
                .set("_id", Utils.guid())
                .set("id", System.currentTimeMillis())
                .set("name", "noear")
                .insert();
    }

    @Test
    public void test2() {
        db.table("user")
                .set("_id", Utils.guid())
                .set("id", 3)
                .set("type",1)
                .set("name", "noear")
                .insert();

        db.table("user")
                .set("_id", Utils.guid())
                .set("id", 5)
                .set("type",1)
                .set("name", "noear")
                .insert();
    }

    @Test
    public void test3() {
        assert db.table("user")
                .set("name", "noear-update" + System.currentTimeMillis())
                .whereEq("id", 5).andLk("name", "^no")
                .update() > 0;
    }

    @Test
    public void test4() {
        Map map =  db.table("user")
                .whereEq("id", 5).andEq("type",1)
                .selectMap();

        System.out.println(map);
        assert (Integer) map.get("id") == 5;
    }

    @Test
    public void test42() {
        UserModel user = db.table("user")
                .whereEq("id", 5).andEq("type", 1)
                .selectItem(UserModel.class);

        System.out.println(user);
        assert user.id == 5;
    }

    @Test
    public void test5() {
        List<Map<String,Object>> mapList =  db.table("user")
                .whereEq("type",1)
                .orderByAsc("id")
                .limit(10)
                .selectMapList();

        System.out.println(mapList);

        assert mapList.size() == 10;
    }

    @Test
    public void test52() {
        List<UserModel> mapList =  db.table("user")
                .whereEq("type",1)
                .orderByAsc("id")
                .limit(10,10)
                .selectList(UserModel.class);

        System.out.println(mapList);

        assert mapList.size() == 10;
    }
}
