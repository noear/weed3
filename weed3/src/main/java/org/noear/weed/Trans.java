package org.noear.weed;

import org.noear.weed.ext.Act0Ex;
import org.noear.weed.utils.ThrowableUtils;

import java.sql.SQLException;

/**
 * @author noear 2020/12/27 created
 */

public class Trans {
    /**
     * 开始事务（如果当前有，则加入；否则新起事务）
     */
    public static DbTran tran(Act0Ex<Throwable> handler) throws SQLException {
        DbTran tran = DbTranUtil.current();

        if (tran == null) {
            return new DbTran().execute(handler);
        } else {
            try {
                handler.run();
            } catch (RuntimeException ex) {
                throw ex;
            } catch (SQLException ex) {
                throw ex;
            } catch (Throwable ex) {
                ex = ThrowableUtils.throwableUnwrap(ex);
                throw ThrowableUtils.throwableWrap(ex);
            }

            return tran;
        }
    }

    /**
     * 开始一个新的事务
     */
    public static DbTran tranNew(Act0Ex<Throwable> handler) throws SQLException {
        return new DbTran().execute(handler);
    }

    /**
     * 以非事务方式运行（如果当有事务，则挂起）
     */
    public static void tranNot(Act0Ex<Throwable> handler) throws SQLException {
        DbTran tran = DbTranUtil.current();
        DbTranUtil.currentRemove();

        try {
            handler.run();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw ex;
        } catch (Throwable ex) {
            ex = ThrowableUtils.throwableUnwrap(ex);
            throw ThrowableUtils.throwableWrap(ex);
        } finally {
            if (tran != null) {
                DbTranUtil.currentSet(tran);
            }
        }
    }
}