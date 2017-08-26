using System;
using System.Collections.Generic;

namespace Noear.Weed {

    /**
     * Created by noear on 14/10/30.
     */
    public class DbTranQueue {
        private List<DbTran> queue = new List<DbTran>();

        public Object result;//用于存放中间结果
        private bool _isSucceed = false;
        public bool isSucceed() {
            return _isSucceed;
        }

        internal void add(DbTran tran) {
            queue.Add(tran);
        }

        internal DbTran get(int index) {
            return queue[index];
        }

        private void commit() {
            foreach (DbTran tran in queue) //从头到尾提交
                tran.commit(true);
        }

        //isQueue:是否由Queue调用的
        internal void rollback(bool isQueue) {
            doRollback();

            if (isQueue == false)
                close();
        }

        private void doRollback() //从尾向头回滚
        {
            int len = queue.Count;
            for (int i = len - 1; i > -1; i--) {
                DbTran tran = get(i);

                try {
                    tran.rollback(true);
                } catch (Exception ex) {
                    WeedConfig.logException(null, ex);
                }
            }
        }

        private void close() {
            foreach (DbTran tran in queue) //从头到关闭（关闭时，不能影响其它事务）
            {
                try {
                    tran.close(true);
                } catch (Exception ex) {
                    WeedConfig.logException(null, ex);
                }
            }
        }

        //执行并结束事务
        public DbTranQueue execute(Action<DbTranQueue> handler) {
            try {
                handler(this);

                commit();
                _isSucceed = true;
            } catch (Exception ex) {
                _isSucceed = false;

                rollback(true);
                throw ex;
            } finally {
                close();
            }

            return this;
        }

        /*结束事务
        * */
        public void complete() {
            try {
                commit();
                _isSucceed = true;
            } catch (Exception ex) {
                _isSucceed = false;

                rollback(true);
                throw ex;
            } finally {
                close();
            }
        }

    }
}
