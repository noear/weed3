using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Weed3Demo.demo.model;

namespace Weed3Demo.demo.table {
    class demo_table_inserlist {
        static DbContext db = DbConfig.pc_user;

        public void demo_insertlist() {
            List<UserInfoModel> list = new List<UserInfoModel>();

            db.table("user").insertList(list, (d, m) => {
                m.set("city_id", d.city_id);
                m.set("name", d.name);
                m.set("mobile", d.mobile);
                m.set("icon", d.icon);
                m.set("role", d.role);
            });
        }

        public void demo_insertlist2() {
            DataList list = new DataList();

            db.table("user").insertList(list.getRows());
        }
    }
}
