using Noear.Weed;
using System;

namespace Weed3Demo.demo.tran {
    public class Tran2Demo {

        //不同函数串一起，跨多个数据库（分布式）
        public static void tast_tran() {
            DbTranQueue queue = new DbTranQueue();//空事务，只提供最后的complete服务；

            try {
                //以下操作在同一个事务队列里执行（各事务独立）
                tast_db1_tran(queue);
                tast_db2_tran(queue);
                tast_db3_tran(queue);
            }
            finally {
                //确保事务有完成的执行
                queue.complete();
            }
        }

        //------------------

        private static void tast_db1_tran(DbTranQueue queue) {
            //使用了 .await(true) 将不提交事务（交由上一层控制）
            //
            DbTran tran = new DbTran(DbConfig.pc_user);

            tran.join(queue).execute((t) => {
                t.db().sql("insert into $.test(txt) values(?)", "cc").tran(t).execute();
                t.db().sql("insert into $.test(txt) values(?)", "dd").tran(t).execute();
                t.db().sql("insert into $.test(txt) values(?)", "ee").tran(t).execute();

                t.result = t.db().sql("select name from $.user_info where user_id=3").tran(t).getValue("");
            });
        }

        private static void tast_db2_tran(DbTranQueue queue) {
            //使用了 .await(true) 将不提交事务（交由上一层控制）
            //
            DbTran tran = DbConfig.pc_base.tran();

            tran.join(queue).execute((t) => {
                t.db().sql("insert into $.test(txt) values(?)", "gg").tran(t).execute();
            });
        }

        private static void tast_db3_tran(DbTranQueue queue) {
            //使用了 .await(true) 将不提交事务（交由上一层控制）
            //
            DbTran tran = new DbTran(DbConfig.pc_live);

            tran.join(queue).execute((t) => {
                t.db().sql("insert into $.test(txt) values(?)", "xx").tran(t).execute();

                throw new Exception("xxxx");
            });
        }
    }
}
