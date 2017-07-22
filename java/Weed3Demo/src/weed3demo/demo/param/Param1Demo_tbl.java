package weed3demo.demo.param;

import noear.weed.IQuery;
import weed3demo.config.DbConfig;
import weed3demo.demo.model.UserInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//
// $.    :schema.
// $fun  :sql fun
// ?     :val
// ?...  :[]
public class Param1Demo_tbl {
    public static void demo_value() throws SQLException{

        UserInfoModel m = DbConfig.pc_user.table("$.user_info")
                .where("user_id=?",1)
                .select("*")
                .getItem(new UserInfoModel());
    }

    public static List<UserInfoModel> demo_params() throws SQLException{
        return _demo_params("15968868040", "15968868040");
    }

    private static List<UserInfoModel> _demo_params(Object... mobiles) throws SQLException{
        IQuery sp = DbConfig.pc_user.table("users")
                .where("mobile IN (?...)", mobiles)
                .select("*");

        return sp.getList(new UserInfoModel());
    }

    public static List<UserInfoModel> demo_list() throws SQLException{

        List<String> mobiles = new ArrayList<>();
        mobiles.add("15968868040");
        mobiles.add("15968868041");

        IQuery sp = DbConfig.pc_user.table("users")
                .where("mobile IN (?...)", mobiles)
                .select("*");

        return sp.getList(new UserInfoModel());
    }
}
