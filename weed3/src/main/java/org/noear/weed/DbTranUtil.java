package org.noear.weed;

public class DbTranUtil {
    private final static ThreadLocal<DbTran> _tl_tran = new ThreadLocal<>();

    /**
     * 设置线程的当前事务
     */
    public static void currentSet(DbTran tran) {
        _tl_tran.set(tran);
    }

    /**
     * 获取线程的当前事务
     */
    public static DbTran current() {
        return _tl_tran.get();
    }

    /**
     * 移除线程的当前事务
     */
    public static void currentRemove() {
        _tl_tran.remove();
    }
}
