using Noear.Weed;
using System;
using System.Collections.Generic;
using Weed3Demo.demo.model;

namespace Weed3Demo.demo.param {
    //
    // $.    :schema.
    // $fun  :sql fun 
    // ?     :val
    // ?...  :[]
    public class Param1Demo_tbl {
        public static void demo_value() {
            
           var m = DbConfig.pc_user.table("$.user_info")
                               .where("user_id=?",1)
                               .select("*")
                               .getItem(new UserInfoModel());
        }

        public static List<UserInfoModel> demo_params() {
            return _demo_params("15968868040", "15968868040");
        }

        private static List<UserInfoModel> _demo_params(params string[] mobiles) {
            var sp = DbConfig.pc_user.table("users").where("mobile IN (?...)", mobiles).select("*");

            return sp.getList(new UserInfoModel());
        }

        public static List<UserInfoModel> demo_list() {

            List<String> mobiles = new List<String>();
            mobiles.Add("15968868040");
            mobiles.Add("15968868041");
            
            var sp = DbConfig.pc_user.table("users").where("mobile IN (?...)", mobiles).select("*");

            return sp.getList(new UserInfoModel());
        }
    }
}
