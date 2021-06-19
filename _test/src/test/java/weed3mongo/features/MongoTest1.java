package weed3mongo.features;

import org.junit.BeforeClass;
import org.junit.Test;
import org.noear.weed.WeedConfig;
import org.noear.weed.mongo.MgContext;
import weed3mongo.model.UserModel;

/**
 * @author noear 2021/2/6 created
 */
public class MongoTest1 {
    String url = "mongodb://admin:admin@localhost";
    MgContext db = new MgContext(url, "demo");

    @BeforeClass
    public static void bef(){
        WeedConfig.isUsingUnderlineColumnName = false;
    }

    @Test
    public void init() {
        db.table("user").whereGte("id",0).delete();
        db.table("user").whereGte("userId",0).delete();

        for (int i = 0; i < 100; i++) {
            db.table("user")
                    .set("userId", i)
                    .set("type", 1)
                    .set("name", "noear")
                    .set("nickName", "xidao")
                    .insert();

            UserModel userDo = new UserModel();
            userDo.userId = i;
            userDo.type = 2;
            userDo.name = "noear";
            userDo.nickName = "xidao";

            db.table("user").setEntity(userDo)
                    .insert();
        }
    }
}
