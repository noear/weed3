using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Weed3Demo.demo.model;
using Weed3Demo.test.table;

namespace Weed3Demo.demo_plus.table {
    class demo_table_inserlist {
        static DbContext db = DbConfig.pc_user;

        public void demo_insertlist10() {
            List<UserInfoModel> list = new List<UserInfoModel>();

            UserM tb = new UserM();

            tb.insertList(list, (d, m) => {
                m.set("city_id", d.city_id);
                m.set("name", d.name);
                m.set("mobile", d.mobile);
                m.set("icon", d.icon);
                m.set("role", d.role);
            });
        }

        public void demo_insertlist12() {
            DataList list = new DataList();

            UserM tb = new UserM();

            tb.insertList(list.getRows());
        }
    }
}
