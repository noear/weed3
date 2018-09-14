using Noear.Weed;

namespace Weed3Demo.demo.tran {
    public class Tran1Demo {
        /*所有的执行在一个事务控制范围内*/
        private static void test_db1_tran() {
            //1.简单处理
            DbConfig.pc_user.tran((t) => {
                //以下操作在同一个事务里执行
                t.db().sql("insert into test(txt) values(?)", "cc").tran(t).execute();
                t.db().sql("insert into test(txt) values(?)", "dd").tran(t).execute();
                t.db().sql("insert into test(txt) values(?)", "ee").tran(t).execute();

                t.db().sql("update test set txt='1' where id=1").tran(t).execute();
            });
        }

        /*2个数据库的事务（在同一个函数内，分布式）*/
        public static void test_db2_tran() {
            DbContext db = DbConfig.pc_user;
            DbContext db2 = DbConfig.pc_base;

            //1.建立主事务，并执于
            DbTranQueue queue = new DbTranQueue();

            db.tran().join(queue).execute((t) => {
                db.sql("insert into test(txt) values(?)", "cc").tran(t).execute();
                db.sql("insert into test(txt) values(?)", "dd").tran(t).execute();
                db.sql("insert into test(txt) values(?)", "ee").tran(t).execute();
            });

            //2.执行第二个事务
            db2.tran().join(queue).execute((t) => {
                db2.sql("insert into test(txt) values(?)", "gg").tran(t).execute();
            });

            queue.complete();
        }

        /*2个数据库的事务，后面的根据前面的执行结果再决定要不要跟进（在同一个函数内，分布式）*/
        public static void test_db2_tran2() {
            DbContext db = DbConfig.pc_user;
            DbContext db2 = DbConfig.pc_base;

            //1.建立主事务，并执于
            DbTranQueue queue = new DbTranQueue();

            //1.建立主事务，并执于
            db.tran().join(queue).execute((t) => {
                db.sql("insert into test(txt) values(?)", "cc").tran(t).execute();
                db.sql("insert into test(txt) values(?)", "dd").tran(t).execute();
                db.sql("insert into test(txt) values(?)", "ee").tran(t).execute();

                queue.result = 1;
            });


        //2.根据执行结果判断
        if ((int) queue.result == 1) {
                //3.执行第二个事务
                db2.tran().join(queue).execute((t)=> {
                    db2.sql("insert into test(txt) values(?)", "gg").tran(t).execute();
                });//json(tran) 时，会自动调用 await(true);  当 await(true)时，需要之后的事务来触发或手动触发
            }

            //4.统一触发事务
            queue.complete();
        }
    }
}
