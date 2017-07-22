using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Weed3Demo.demo.model;

namespace Weed3Demo.demo.sqlbuilder {
    class demo {
        //
        // mysql批量操作需要数据库连接配置allowMultiQueries=true
        //
        static DbContext db = DbConfig.pc_user;

        public void demo1_insert_batch() {
            List<UserInfoModel> list = new List<UserInfoModel>();
            SQLBuilder sb = new SQLBuilder();

            foreach (UserInfoModel m in list) {
                sb.append("insert into user_info(user_id,mobile,icon) values(?,?,?);", m.user_id, m.mobile, m.icon);
            }

            db.sql(sb);
        }

        public void demo1_insert_batch2() { //另一种批量插入的写法
            List<UserInfoModel> list = new List<UserInfoModel>();
            SQLBuilder sb = new SQLBuilder();

            sb.append("insert into user_info(user_id,mobile,icon) ");
            sb.append("values");

            foreach (UserInfoModel m in list) {
                sb.append("(?,?,?),", m.user_id, m.mobile, m.icon);
            }

            sb.removeLast();
            sb.append(";");

            db.sql(sb);
        }

        public void demo2_update_batch() {
            List<UserInfoModel> list = new List<UserInfoModel>();

            SQLBuilder sb = new SQLBuilder();

            foreach (UserInfoModel m in list) {
                sb.append("update user_info set mobile=?,icon=? where user_id=?;", m.mobile, m.icon, m.user_id);
            }

            db.sql(sb);
        }

        public void demo3_delete_batch() {
            List<UserInfoModel> list = new List<UserInfoModel>();

            SQLBuilder sb = new SQLBuilder();

            foreach (UserInfoModel m in list) {
                sb.append("delete from user_info where user_id=?;", m.user_id);
            }

            db.sql(sb);
        }
    }
}
