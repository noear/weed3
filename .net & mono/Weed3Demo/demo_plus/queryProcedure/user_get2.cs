using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

//如果原来是存储过程的代码，可以通过[DbQueryProcedure]快速切换过来

namespace Weed3Demo.demo_plus.queryProcedure {
    class user_get2 : DbQueryProcedure {
        public user_get2() : base(DbConfig.test) {
            sql("select * from user where userID=@userID");

            set("@userID", () => userID);
        }

        public long userID;
    }
}
