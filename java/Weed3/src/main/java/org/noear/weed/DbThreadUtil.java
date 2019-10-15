package org.noear.weed;

public class DbThreadUtil {
    private final static ThreadLocal<DbTran> _tl_tran = new ThreadLocal<>();
    private final static ThreadLocal<DbTranQueue> _tl_tran_queue = new ThreadLocal<>();

    /** 设置线程的当前事务 */
    public static void tranStart(DbTran tran){
        _tl_tran.set(tran);
    }
    /** 获取线程的当前事务 */
    public static DbTran tranGet(){
        return _tl_tran.get();
    }
    /** 移除线程的当前事务 */
    public static void tranEnd(){
        _tl_tran.remove();
    }

    /** 设置线程的当前事务队列 */
    public static void tranQueueStart(DbTranQueue tran){
        _tl_tran_queue.set(tran);
    }
    /** 获取线程的当前事务队列 */
    public static DbTranQueue tranQueueGet(){
        return _tl_tran_queue.get();
    }
    /** 移除线程的当前事务队列 */
    public static void tranQueueEnd(){
        _tl_tran_queue.remove();
    }


}
