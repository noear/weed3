using System;
using System.Collections.Generic;

using System.Text;
using System.Data;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 资源包服务
    /// </summary>
    public class PackageService : GroupServiceBase<Package>
    {
        #region 基础部份

        /// <summary>
        /// 静态实例
        /// </summary>
        public static PackageService G = new PackageService();

        /// <summary>
        /// 在组里的定义[R_PGID]
        /// </summary>
        public override int DEFINE_VALUE
        {
            get
            {
                return Package.DEFINE;
            }
        }

        #endregion

        /// <summary>
        /// 添加一个资源包
        /// </summary>
        /// <param name="package"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public int AppendPackage(Package package, QDbTran tran)
        {
            package.R_PGID = Package.DEFINE;

            return DoAppendGroup(package, tran);
        }

        /// <summary>
        /// 移除[删除]一个资源包
        /// </summary>
        /// <param name="pgids"></param>
        /// <returns></returns>
        public bool RemovePackage(params int[] pgids)
        {
            return DoRemoveGroup(null, pgids);
        }

        public bool RemovePackage(int p_pgid, QDbTran tran)
        {
            return DoRemoveGroup(p_pgid, tran);
        }

        /// <summary>
        /// 修改一个资源包
        /// </summary>
        /// <param name="package"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public bool ModifyPackage(Package package, QDbTran tran)
        {
            return DoModifyGroup(package, tran);
        }

        

        //---------------------------------------

        /// <summary>
        /// 获取一个资源包
        /// </summary>
        /// <param name="pgid"></param>
        /// <returns></returns>
        public Package GetPackage(int pgid)
        {
            return DoGetGroup(pgid);
        }

        /// <summary>
        /// 获取所有资源包
        /// </summary>
        /// <returns></returns>
        public List<Package> GetPackages()
        {
            return DoGetGroups();
        }

        /// <summary>
        /// 获取资源包与关联表的组合信息
        /// </summary>
        /// <param name="joinTable">关联表</param>
        /// <param name="p_pgids">资源包的父级PGID组</param>
        /// <returns></returns>
        public DataTable GetPackages(QTable joinTable, params int[] p_pgids)
        {
            return DoGetGroups(joinTable, false, false, p_pgids);
        }

        /// <summary>
        /// 获取资源包与关联表的组合信息
        /// </summary>
        /// <param name="joinTable">关联表</param>
        /// <param name="incDisabled">是否包括被禁用的</param>
        /// <param name="p_pgids">资源包的父级PGID组</param>
        /// <returns></returns>
        public DataTable GetPackages(QTable joinTable, bool incDisabled, params int[] p_pgids)
        {
            return DoGetGroups(joinTable, incDisabled, false, p_pgids);
        }
    }
}
