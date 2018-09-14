using Noear.Weed;
using System;
using System.Data;

namespace Weed3Demo.demo_ex {
    public class user_update : DbStoredProcedure {
        public user_update() : base(DbConfig.test) {
            call("user_update");
            set("_userID",() => userID);
            set("_city", () => city);
            set("_vipTime", () => vipTime);
        }

        public long userID;
        public string city;
        public DateTime vipTime;
    }
}
