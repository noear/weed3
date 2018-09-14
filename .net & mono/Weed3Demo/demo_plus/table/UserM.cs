using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Weed3Demo.test.table {
    public class UserM : DbTable {
        public UserM() : base(DbConfig.test) {
            table("users u");
            set("UserID", () => UserID);
            set("Nickname", () => Nickname);
            set("Sex", () => Sex);
            set("Icon", () => Icon);
            set("City", () => City);
        }

        public long? UserID;
        public String Nickname;
        public int? Sex;
        public String Icon;
        public String City;
    }
}
