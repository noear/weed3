package weed3demo.demo_plus.table;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuety on 2017/7/22.
 */
public class demo_table {
    public void demo_insert() throws SQLException{
        user_info tb = new user_info();
        tb.userID = 12;
        tb.sex = 1;//男的

        tb.insert();
    }

    public void demo_insert2() throws SQLException {
        Map<String, String> data = new HashMap<>();//或其它字段类

        user_info tb = new user_info();
        tb.insert((key) -> {
            if (data.containsKey(key))
                return data.get(key);
            else
                return null;
        });
    }

    public void demo_update() throws SQLException{
        user_info tb = new user_info();
        tb.sex = 1;//男的

        tb.where("id=?", 22).update();
    }

    public void demo_update2() throws SQLException{
        Map<String, Object> data = new HashMap<>();

        user_info tb = new user_info();

        tb.where("id=?", 22).update((key) -> {
            if (data.containsKey(key))
                return data.get(key);
            else
                return null;
        });
    }

    public void demo_update3() throws SQLException{
        Map<String, Object> data = new HashMap<>();
        user_info tb = new user_info();

        tb.where("id=?", 22).update((key) -> {
            switch (key) {
                case "sex":
                    return 1;
                case "name":
                    return "cc";
                default:
                    return null;
            }
        });
    }

    public void demo_delete() throws SQLException {
        user_info tb = new user_info();

        tb.where("id=?", 22).delete();
    }

    public void demo_select() throws SQLException{
        user_info tb = new user_info();

        tb.where("id=?", 22).select("*");
    }

    public void demo_select_join() throws SQLException{
        user_info tb = new user_info();

        tb.innerJoin("$.user_link l").on("user_id = l.user_id")
                .where("id=?", 22)
                .select("*");
    }
}
