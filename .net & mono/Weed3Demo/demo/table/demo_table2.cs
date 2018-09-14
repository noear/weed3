using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Weed3Demo.demo.table {
    class demo_table2 {
        static DbContext db = DbConfig.pc_user;

        public static void demo_expr1() {
            //连式处理::对不确定字段的插入
            db.table("test")
              .expre(tb => {
                tb.set("name", "xxx");

                if (1 == 2) {
                    tb.set("mobile", "xxxx");
                } else {
                    tb.set("icon", "xxxx");
                }
              }).insert();
        }

        public static void demo_expr2() {
               //连式处理::对不确定的条件拼装
              db.table("test")
                .expre(tb => {
                    tb.where("1=1");

                    if (1 == 2) {
                        tb.and("mobile=?", "xxxx");
                    } else {
                        tb.and("icon=?", "xxxx");
                    }
                }).select("*");
        }
    }
}
