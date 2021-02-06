package weed3mongo.features;

import org.junit.Test;
import org.noear.weed.mongo.MgContext;
import weed3mongo.model.UserModel;

import java.util.Arrays;
import java.util.List;

/**
 * @author noear 2021/2/6 created
 */
public class MongoTest3 {
    String serverIp = "172.168.0.162";
    int serverPort = 27017;

    MgContext db = new MgContext(serverIp, serverPort, "demo");

    @Test
    public void test1(){
        List<UserModel> mapList =  db.table("user")
                .whereBtw("id", 10,20)
                .orderByAsc("id")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() == 10;
        assert mapList.get(0).id == 10;
    }

    @Test
    public void test2(){
        List<UserModel> mapList =  db.table("user")
                .whereIn("id", Arrays.asList(3,4))
                .orderByAsc("id")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() > 2;
        assert mapList.get(0).id == 3;
    }

    @Test
    public void test3(){
        List<UserModel> mapList =  db.table("user")
                .whereScript("id==3")
                .orderByAsc("id")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() > 2;
        assert mapList.get(0).id == 3;
    }
}
