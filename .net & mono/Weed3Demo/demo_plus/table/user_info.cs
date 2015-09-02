using Noear.Weed;
using Weed3Demo.demo;

namespace Weed3Demo.demo_ex {
    //此类，可由工具根据数据表生成
    //
    public class user_info : DbTable {
        public user_info() : base(DbConfig.test) {
            table("$.user_info u");
            set("userID", () => userID);
            set("sex", () => sex); 
        }

        public long? userID;
        public int? sex;
    }
}
