using Noear.Weed;
using System.Data;

namespace Weed3Demo.demo_ex {
    public class user_get : DbStoredProcedure {
        public user_get() : base(DbConfig.test) {
            call("user_get");
            set("_userID", () => userID);
        }

        public long userID;
    }
}
