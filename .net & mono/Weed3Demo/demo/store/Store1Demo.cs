using Noear.Weed;
using Weed3Demo.demo.model;

namespace Weed3Demo.demo.store {
    //
    //
    public class Store1Demo {
        public static void demo_select() {
            DbContext db = DbConfig.pc_bcf;
            
            db.call("user_get")
              .set("user_id",1).getItem(new UserInfoModel());
        }

        public static void demo_updateOrInsert() {
            DbContext db = DbConfig.pc_bcf;
            
            db.call("user_update")
                .set("useer_id",11)
                .set("age", 12).execute();
        }
    }
}
