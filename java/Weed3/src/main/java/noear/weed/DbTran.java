package noear.weed;

import noear.weed.ext.Act1Ex;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yuety on 14-9-16.
 */
public class DbTran {
    protected Connection connection;
    private DbTranQueue queue;

    private Act1Ex<DbTran,SQLException> _handler = null;

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
    public DbTran connect(){
        try {
            if(connection == null) {
                connection = _context.getConnection();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return this;
    }

    /*执行事务过程 = action(...) + excute() */
    public DbTran execute(Act1Ex<DbTran,SQLException> handler) throws SQLException {
        try {
            if(connection==null) {
                connection = _context.getConnection();
            }

            begin(false);
            handler.run(this);
            commit(false);

            _isSucceed = true;
        } catch (SQLException ex) {
            _isSucceed = false;

            if (queue == null)
                rollback(false);
            else
                queue.rollback(false);
            throw ex;
        } finally {
            close(false);
        }

        return this;
    }

    /*执行事务过程*/
    public DbTran execute() throws SQLException {
        return execute(_handler);
    }


    public DbTran action(Act1Ex<DbTran,SQLException> handler){
        _handler = handler;
        return this;
    }



    //isQueue:是否由Queue调用的
    protected void begin(boolean isQueue)  throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(false);
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
