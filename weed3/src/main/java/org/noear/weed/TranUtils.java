package org.noear.weed;

import org.noear.weed.ext.Act0Ex;

import java.sql.SQLException;

/**
 * 事务管理工具
 * */
public class TranUtils {
    /**
     * 开始事务（如果当前有，则加入；否则新起事务）
     */
    public void tran(Act0Ex<Throwable> handler) throws SQLException {
        DbTran tran = DbTranUtil.current();

        if (tran == null) {
            new DbTran().execute(t -> handler.run());
        } else {
            try {
                handler.run();
            } catch (RuntimeException ex) {
                throw ex;
            } catch (SQLException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * 开始一个新的事务
     */
    public void tranNew(Act0Ex<Throwable> handler) throws SQLException {
        new DbTran().execute(t -> handler.run());
    }

    /**
     * 排除事务
     */
    public void tranNot(Act0Ex<Throwable> handler) throws SQLException {
        DbTran tran = DbTranUtil.current();
        DbTranUtil.currentRemove();

        try {
            handler.run();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        } finally {
            if (tran != null) {
                DbTranUtil.currentSet(tran);
            }
        }
    }
}
