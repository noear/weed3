package weed3demo.demo_plus.table;

import noear.weed.DbTable;
import weed3demo.config.DbConfig;

/**
 * Created by yuety on 2017/7/22.
 */
public class user extends DbTable {
    public user() {
        super(DbConfig.test);

        table("users u");
        set("UserID", () -> UserID);
        set("Nickname", () -> Nickname);
        set("Sex", () -> Sex);
        set("Icon", () -> Icon);
        set("City", () -> City);
    }

    public Long UserID;
    public String Nickname;
    public Integer Sex;
    public String Icon;
    public String City;
}
