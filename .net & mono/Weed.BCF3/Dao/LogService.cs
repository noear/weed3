using System.Collections.Generic;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 日志服务
    /// </summary>
    public class LogService : BCF_PARAMS
    {
        /// <summary>
        /// 静态实例
        /// </summary>
        public static LogService G = new LogService();

        #region  管理
        /// <summary>
        /// 写入日志
        /// </summary>
        /// <param name="log">日志对象</param>
        /// <returns>LogID</returns>
        public int WriteLog(Log log)
        {
            return this.WriteLog(log, null);
        }

        /// <summary>
        /// 写入日志
        /// </summary>
        /// <param name="log">日志对象</param>
        /// <param name="tran">事务[可以为null]</param>
        /// <returns>LogID</returns>
        public int WriteLog(Log log, QDbTran tran)
        {
            if (log == null)
                return 0;
            
            LogM l = new LogM();
            l.Bind(log);

            l.UsingFilter(false).Insert(true, tran);

            return l.INSERTED_ID;
        }

        /// <summary>
        /// 清空所有日志
        /// </summary>
        public void ClearLogs()
        {
            LogM.G.Truncate();
        }

        //---------------------------------------------

        #endregion

        #region Select,Count

        /// <summary>
        /// 分页形式获取日志记录
        /// </summary>
        /// <param name="pageSize">分页长度</param>
        /// <param name="pageIndex">分页索引</param>
        /// <returns></returns>
        public List<Log> Select(int pageSize, int pageIndex)
        {
            return Select(0, pageSize, pageIndex);
        }

        /// <summary>
        /// 根据日志类型，分页形式获取日志记录
        /// </summary>
        /// <param name="lg_type">日志类型</param>
        /// <param name="pageSize">分页长度</param>
        /// <param name="pageIndex">分页索引</param>
        /// <returns></returns>
        public List<Log> Select(int lg_type, int pageSize, int pageIndex)
        {
            LogM l = new LogM();

            if (lg_type>0)
                l.Where(l.LG_Type == lg_type);

            l.OrderBy(l.LGID, OrderType.DESC);

            return l.Select<Log>(pageSize, pageIndex);
        }

        /// <summary>
        /// 根据条件对象，分页形式获取日志记录
        /// </summary>
        /// <param name="where">条件对象</param>
        /// <param name="pageSize">分页长度</param>
        /// <param name="pageIndex">分页索引</param>
        /// <returns></returns>
        public List<Log> Select(SQuery where, int pageSize, int pageIndex)
        {
            LogM l = new LogM();

            l.OrderBy(l.LGID, OrderType.DESC);

            return l.Where(where).Select<Log>(pageSize, pageIndex);

        }

        /// <summary>
        /// 获取日志总数
        /// </summary>
        /// <returns></returns>
        public int Count()
        {
            return Count(0);
        }

        /// <summary>
        /// 获取某类型的日志总数
        /// </summary>
        /// <param name="lg_type">日志类型</param>
        /// <returns></returns>
        public int Count(int lg_type)
        {
            LogM l = new LogM();

            if (lg_type > 0)
                l.Where(l.LG_Type == lg_type);

            return l.Count();
        }

        /// <summary>
        /// 获取某条件的日志总数
        /// </summary>
        /// <param name="where"></param>
        /// <returns></returns>
        public int Count(SQuery where)
        {
            LogM l = new LogM();

            return l.Where(where).Count();
        }

        #endregion
    }
}
