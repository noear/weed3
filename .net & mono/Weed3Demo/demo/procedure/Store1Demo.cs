using Noear.Weed;
using Weed3Demo.demo.model;

namespace Weed3Demo.demo.store {
    //
    //
    public class Store1Demo {
        //1.存储过程::
        public static void demo_select() {
            DbContext db = DbConfig.pc_bcf;

            db.call("user_get")
              .set("user_id", 1).getItem(new UserInfoModel());
        }

        public static void demo_update() {
            DbContext db = DbConfig.pc_bcf;

            db.call("user_update")
                .set("useer_id", 11)
                .set("age", 12).execute();
        }

        //2.查询过程::
        public static void demo_select2() {
            DbContext db = DbConfig.pc_bcf;

            db.call("select * from user user_id=@user_id") //查询过程的变量，须使用@号开头
                .set("@user_id", 1)
                .getItem(new UserInfoModel());
        }

        public static void demo_update2() {
            DbContext db = DbConfig.pc_bcf;

            db.call("update user set age=@age where user_id=@user_id")
                    .set("@useer_id", 11)
                    .set("@age", 12).execute();
        }

    }
}
