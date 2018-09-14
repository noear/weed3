using System;
using System.Data.Common;

namespace Noear.Weed {
    /**
 * Created by noear on 14-9-16.
 */
    public class DbTran {
        internal DbConnection connection;
        internal DbTransaction transaction;

        private DbTranQueue queue;

        private Action<DbTran> _handler = null;

        //当前为master事务时，才会用到这个字段;(用于记录这个队列里，最后一个事务；方便下一个事务设置before)
        private DbContext _context = null;/*数据访问上下文*/

        public Object result;
        private bool _isSucceed = false;
        public bool isSucceed() {
            return _isSucceed;
        }

        public DbContext db() {
            return _context;
        }


        /*加盟（到某一个事务当中）*/
        public DbTran join(DbTranQueue queue) {
            if (queue != null) {
                this.queue = queue;
                queue.add(this);
            }

            return this;
        }

        public DbTran(DbContext context) {
            _context = context;
        }

        /*执行事务过程*/
        public DbTran execute(Action<DbTran> handler) {
            _handler = handler;

            try {
                connection = _context.getConnection();

                begin(false);
                _handler(this);
                commit(false);

                _isSucceed = true;
            }
            catch (Exception ex) {
                _isSucceed = false;

                if (queue == null)
                    rollback(false);
                else
                    queue.rollback(false);
                throw ex;
            }
            finally {
                close(false);
            }

            return this;
        }



        //isQueue:是否由Queue调用的
        internal void begin(bool isQueue) {
            if (connection != null) {
                transaction = connection.BeginTransaction();
                //connection.setAutoCommit(false);
            }
        }

        //isQueue:是否由Queue调用的
        internal void rollback(bool isQueue) {
            if (queue == null || isQueue) {
                if (transaction != null) {
                    transaction.Rollback();
                }
            }
        }

        //isQueue:是否由Queue调用的
        internal void commit(bool isQueue) {
            if (queue == null || isQueue) {
                if (transaction != null) {
                    transaction.Commit();
                }
            }
        }

        //isQueue:是否由Queue调用的
        internal void close(bool isQueue) {
            if (queue == null || isQueue) {
                if (connection != null) {
                    //connection.setAutoCommit(true);
                    connection.Close();
                    connection = null;
                }
            }
        }
    }
}
