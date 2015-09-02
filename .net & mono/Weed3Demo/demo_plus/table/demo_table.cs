using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Weed3Demo.demo_ex.table {
    class demo_table {
        public void demo_insert() {
            user_info tb = new user_info();
            tb.userID = 12;
            tb.sex = 1;//男的

            tb.insert();
        }

        public void demo_insert2() {
            var data = new Dictionary<string, object>();//或其它字段类

            user_info tb = new user_info();
            tb.insert((key)=> {
                if (data.ContainsKey(key))
                    return data[key];
                else
                    return null;
            });
        }

        public void demo_update() {
            user_info tb = new user_info();
            tb.sex = 1;//男的

            tb.where("id=?", 22).update();
        }

        public void demo_update2() {
            var data = new Dictionary<string, object>();
            user_info tb = new user_info();
            
            tb.where("id=?", 22).update((key)=> {
                if (data.ContainsKey(key))
                    return data[key];
                else
                    return null;
            });
        }

        public void demo_update3() {
            var data = new Dictionary<string, object>();
            user_info tb = new user_info();

            tb.where("id=?", 22).update((key) => {
                switch (key) {
                    case "sex":return 1;
                    case "name": return "cc";
                    default:return null;
                }
            });
        }

        public void demo_delete() {
            user_info tb = new user_info();

            tb.where("id=?", 22).delete();
        }

        public void demo_select() {
            user_info tb = new user_info();

            tb.where("id=?", 22).select("*");
        }

        public void demo_select_join() {
            user_info tb = new user_info();

            tb.innerJoin("$.user_link l").on("u.user_id = l.user_id")
              .where("id=?", 22)
              .select("*");
        }
    }
}
