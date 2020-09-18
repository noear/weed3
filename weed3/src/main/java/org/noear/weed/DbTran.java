package org.noear.weed;

import org.noear.weed.ext.Act1Ex;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by noear on 14-9-16.
 */
public class DbTran {
    private final Map<DataSource, Connection> conMap = new HashMap<>();
    private DbTranQueue queue;

    private Act1Ex<DbTran, Throwable> _handler = null;

    //当前为master事务时，才会用到这个字段;(用于记录这个队列里，最后一个事务；方便下一个事务设置before)
    private DbContext _context = null;/*数据访问上下文*/

    public Object result;
    private boolean _isSucceed = false;

    public boolean isSucceed() {
        return _isSucceed;
    }

    public DbContext db() {
        return _context;
    }

    public Connection getConnection(DbContext db) throws SQLException {
        return getConnection(db.dataSource());
    }

    public Connection getConnection(DataSource ds) throws SQLException {
        if (conMap.containsKey(ds)) {
            return conMap.get(ds);
        } else {
            Connection con = ds.getConnection();
            con.setAutoCommit(false);

            conMap.putIfAbsent(ds, con);
            return con;
        }
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

    /*执行事务过程 = action(...) + excute() */
    public DbTran execute(Act1Ex<DbTran, Throwable> handler) throws SQLException {
        DbTran tranTmp = DbTranUtil.current();

        try {
            //开始事务
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

            if (tranTmp != null) {
                DbTranUtil.currentSet(tranTmp);
            }
        }

        return this;
    }

    /*执行事务过程*/
    public DbTran execute() throws Throwable {
        return execute(_handler);
    }


    public DbTran action(Act1Ex<DbTran, Throwable> handler) {
        _handler = handler;
        return this;
    }

    //isQueue:是否由Queue调用的
    protected void rollback(boolean isQueue) throws SQLException {
        if (queue == null || isQueue) {
            for (Map.Entry<DataSource, Connection> kv : conMap.entrySet()) {
                kv.getValue().rollback();
            }
        }
    }

    //isQueue:是否由Queue调用的
    protected void commit(boolean isQueue) throws SQLException {
        if (queue == null || isQueue) {
            for (Map.Entry<DataSource, Connection> kv : conMap.entrySet()) {
                kv.getValue().commit();
            }
        }
    }

    //isQueue:是否由Queue调用的
    protected void close(boolean isQueue) throws SQLException {
        if (queue == null || isQueue) {
            for (Map.Entry<DataSource, Connection> kv : conMap.entrySet()) {
                //kv.getValue().setAutoCommit(true);
                try {
                    if (kv.getValue().isClosed() == false) {
                        kv.getValue().close();
                    }
                } catch (Exception ex) {
                    WeedConfig.runExceptionEvent(null, ex);
                }
            }
        }
    }
}
