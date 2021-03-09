package weed3mongo.features;

import org.junit.Test;
import org.noear.weed.mongo.MgContext;
import weed3mongo.model.UserModel;

/**
 * @author noear 2021/2/6 created
 */
public class MongoTest1 {
    String url = "mongodb://172.168.0.162:27017";
    MgContext db = new MgContext(url, "demo");

//    @Test
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
}
