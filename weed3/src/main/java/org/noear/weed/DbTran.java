package org.noear.weed;

import org.noear.weed.ext.Act0Ex;
import org.noear.weed.utils.ThrowableUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据事务：支持事务模式和单链接模式（同时，可替代之前的DbTranQueue）
 *
 * @author noear
 * @since 14-9-16
 */
public class DbTran {
    private final Map<DataSource, Connection> conMap = new HashMap<>();

    //是否自动提交
    private boolean _autoCommit = false;


    public Connection getConnection(DbContext db) throws SQLException {
        return getConnection(db.getMetaData().getDataSource());
    }

    public Connection getConnection(DataSource ds) throws SQLException {
        if (conMap.containsKey(ds)) {
            return conMap.get(ds);
        } else {
            Connection con = ds.getConnection();
            if (_autoCommit == false) {
                con.setAutoCommit(false);
            }

            conMap.putIfAbsent(ds, con);
            return con;
        }
    }


    /**
     * 是否自动提交
     */
    public DbTran autoCommit(boolean autoCommit) {
        _autoCommit = autoCommit;
        return this;
    }

    /*执行事务过程 = action(...) + excute() */
    public DbTran execute(Act0Ex<Throwable> handler) throws SQLException {
        //挂起之前的事务
        DbTran tranTmp = DbTranUtil.current();

        try {
            //开始事务
            DbTranUtil.currentSet(this);
            handler.run();

            if (!_autoCommit) {
                commit();
            }
        } catch (Throwable ex) {
            if (!_autoCommit) {
                rollback();
            }

            ex = ThrowableUtils.throwableUnwrap(ex);
            if (ex instanceof SQLException) {
                throw (SQLException) ex;
            } else if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        } finally {
            DbTranUtil.currentRemove();

            close();

            if (tranTmp != null) {
                //恢复之前的事务
                DbTranUtil.currentSet(tranTmp);
            }
        }

        return this;
    }


    protected void rollback() throws SQLException {
        for (Map.Entry<DataSource, Connection> kv : conMap.entrySet()) {
            kv.getValue().rollback();
        }
    }

    protected void commit() throws SQLException {
        for (Map.Entry<DataSource, Connection> kv : conMap.entrySet()) {
            kv.getValue().commit();
        }
    }

    protected void close() throws SQLException {
        for (Map.Entry<DataSource, Connection> kv : conMap.entrySet()) {
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
