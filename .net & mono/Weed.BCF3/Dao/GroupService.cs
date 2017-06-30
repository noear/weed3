using System;
using System.Collections.Generic;

using System.Text;
using System.Data;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 组管理服务
    /// </summary>
    public class GroupService : GroupServiceBase<Group>
    {
        /// <summary>
        /// 静态实例
        /// </summary>
        public static GroupService G = new GroupService();

        #region 构造
        /// <summary>
        /// 构造函数
        /// </summary>
        public GroupService()
        {
            ;
        }

        /// <summary>
        /// 构造函数
        /// </summary>
        /// <param name="define">组定义</param>
        public GroupService(int define)
        {
            base._DEFINE_VALUE = define;
        }

        #endregion

        /// <summary>
        /// 切换组定义
        /// </summary>
        /// <param name="define">在组里的定义[R_PGID]</param>
        /// <returns></returns>
        public GroupService ShiftDefine(int define)
        {
            _DEFINE_VALUE = define;

            return this;
        }

        /// <summary>
        /// 添加一个组
        /// </summary>
        /// <param name="group"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public int AppendGroup(Group group, QDbTran tran)
        {
            return DoAppendGroup(group, tran);
        }

        /// <summary>
        /// 移除[删除]组
        /// </summary>
        /// <param name="tran"></param>
        /// <param name="pgids"></param>
        /// <returns></returns>
        public bool RemoveGroup(QDbTran tran, params int[] pgids)
        {
            return DoRemoveGroup(tran, pgids);
        }

        /// <summary>
        /// 禁止组
        /// </summary>
        /// <param name="disable"></param>
        /// <param name="pgids"></param>
        /// <returns></returns>
        public bool DisableGroup(bool disable, params int[] pgids)
        {
            return DoDisableGroup(disable, pgids);
        }

        /// <summary>
        /// 修改组
        /// </summary>
        /// <param name="group"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public bool ModifyGroup(Group group, QDbTran tran)
        {
            return DoModifyGroup(group, tran);
        }

        /// <summary>
        /// 清空某定义的组
        /// </summary>
        /// <param name="defines"></param>
        /// <returns></returns>
        public bool ClearGroups(params int[] defines)
        {
            return DoClearGroups(defines);
        }

        /// <summary>
        /// 获取组
        /// </summary>
        /// <param name="pgid"></param>
        /// <returns></returns>
        public Group GetGroup(int pgid)
        {
            return DoGetGroup(pgid);
        }

        /// <summary>
        /// 获取组
        /// </summary>
        /// <param name="pgCode"></param>
        /// <returns></returns>
        public Group GetGroup(string pgCode)
        {
            return DoGetGroup(pgCode);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pgidS"></param>
        /// <returns></returns>
        public List<Group> GetGroups(params int[] pgidS)
        {
            return DoGetGroups(pgidS);
        }

        public List<Group> GetInGroups(int puid, bool incDisabled)
        {
            return DoGetInGroups(puid, incDisabled);
        }

        public List<Group> GetGroups( bool incDisabled, bool incUnVisible, params int[] p_pgids)
        {
            return DoGetGroups(incDisabled, incUnVisible, p_pgids);
        }

        public List<Group> GetGroupsInUser(int puid, params int[] p_pgids)
        {
            return DoGetGroupsInUser(puid, p_pgids);
        }

        public DataTable GetGroups(QTable joinTable, bool incDisabled, bool incUnVisible, params int[] p_pgids)
        {
            return DoGetGroups(joinTable, incDisabled, incUnVisible, p_pgids);
        }

        public List<Group> GetGroups()
        {
            return DoGetGroups();
        }
    }
}
