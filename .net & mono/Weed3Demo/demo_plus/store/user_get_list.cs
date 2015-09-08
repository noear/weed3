using Noear.Weed;

namespace Weed3Demo.demo_ex {
    //此类，可由工具根据存储过程生成
    //
    public class user_get_list : DbStoredProcedure {
        public user_get_list() : base(DbConfig.test) {
            call("$.user_get_list");
            set("_userID", () => userID);
            set("_sex",() => sex);
        }

        public long userID;
        public int sex;
    }
}
