package org.noear.weed;

import org.noear.weed.ext.Act1Ex;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by noear on 14-9-16.
 */
public class DbTran {
    protected Connection connection;
    private DbTranQueue queue;

    private Act1Ex<DbTran,Throwable> _handler = null;

    //当前为master事务时，才会用到这个字段;(用于记录这个队列里，最后一个事务；方便下一个事务设置before)
    private DbContext _context = null;/*数据访问上下文*/

    public Object result;
    private boolean _isSucceed = false;
    public boolean isSucceed(){
        return _isSucceed;
    }

    public DbContext db()
    {
        return _context;
    }


    /*加盟（到某一个事务当中）*/
    public DbTran join(DbTranQueue queue) {
        if (queue != null) {
           this.queue =queue;
            queue.add(this);
        }

        return this;
    }

    public DbTran(DbContext context)  {
        _context = context;
    }

    /*开始连接（用于单连接操作）*/
    public DbTran connect() throws SQLException{
        if (connection == null) {
            connection = _context.getConnection();
        }

        return this;
    }

    /*执行事务过程 = action(...) + excute() */
    public DbTran execute(Act1Ex<DbTran,Throwable> handler) throws SQLException {
        DbTran savepoint = DbTranUtil.current();

        try {
            //连接
            connect();

            //开始事务
            begin(-1);

            DbTranUtil.currentSet(this);
            handler.run(this);

            commit(false);

            _isSucceed = true;
        } catch (Throwable ex) {
            _isSucceed = false;

            if (queue == null)
                rollback(false);
            else
                queue.rollback(false);

            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else if (ex instanceof SQLException) {
                throw (SQLException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        } finally {
            DbTranUtil.currentRemove();
            close(false);

            if (savepoint != null) {
                DbTranUtil.currentSet(savepoint);
            }
        }

        return this;
    }

    /*执行事务过程*/
    public DbTran execute() throws Throwable {
        return execute(_handler);
    }


    public DbTran action(Act1Ex<DbTran,Throwable> handler){
        _handler = handler;
        return this;
    }



    //isQueue:是否由Queue调用的
    protected void begin(int isolationLevel)  throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(false);

            if (isolationLevel > 0) {
                connection.setTransactionIsolation(isolationLevel);
            }
        }
    }

    //isQueue:是否由Queue调用的
    protected void rollback(boolean isQueue) throws SQLException {
        if(queue == null || isQueue) {
            if (connection != null) {
                connection.rollback();
            }
        }
    }

    //isQueue:是否由Queue调用的
    protected void commit(boolean isQueue) throws SQLException {
        if(queue == null || isQueue) {
            if (connection != null) {
                connection.commit();
            }
        }
    }

    //isQueue:是否由Queue调用的
    protected void close(boolean isQueue) throws SQLException {
        if(queue == null || isQueue) {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
                connection = null;
            }
        }
    }
}
