using System;
using System.Collections.Generic;

using System.Text;
using System.Data;

using Weed;
using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 组服务基类
    /// </summary>
    /// <typeparam name="T">实体T</typeparam>
    public class GroupServiceBase<T> : BCF_PARAMS where T : BCF_Group
    {
        /// <summary>
        /// 虚拟组的定义值
        /// </summary>
        public virtual int DEFINE_VALUE { get { return _DEFINE_VALUE; } }
        /// <summary>
        /// 
        /// </summary>
        protected int _DEFINE_VALUE = 0;

        #region  管理
        /// <summary>
        /// 
        /// </summary>
        /// <param name="group"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        protected int DoAppendGroup(T group, QDbTran tran)
        {
            if (group == null)
                return 0;

            //是否存在@P_PGID的组
            //
            if (group.P_PGID > 0)
            {
                if (this.Exists(group.P_PGID) == false)
                    return 0;
            }
            else if (DEFINE_VALUE > 0)
                group.P_PGID = DEFINE_VALUE;


            // 如果有定义值,则根据定义值修改R_PGID
            //
            if (DEFINE_VALUE > 0)
                group.R_PGID = DEFINE_VALUE;

            group.Create_Time = DateTime.Now;
            group.Last_Update = group.Create_Time;


            GroupM g = new GroupM();
            g.Bind(group);

            g.Insert(true, tran);

            return g.INSERTED_ID;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pgid"></param>
        /// <returns></returns>
        public bool Exists(int pgid)
        {
            if (pgid == 0)
                return false;

            GroupM g = new GroupM();

            return g.NoLock().Where(g.PGID == pgid).Exists();
        }

        protected bool DoRemoveGroup(int p_pgid, QDbTran tran)
        {
            if (p_pgid ==0)
                throw new BCFException("参数异常:(p_pgid == 0)");

            //-------------------------------------
            //删除圈子
            //
            GroupM g = new GroupM();

            QView lkidView = g.Where(g.P_PGID == p_pgid).SubSelect(g.PGID);            

            //1. 清除与Resource这间的连接
            //
            ResourceService.G.RemoveLink(lkidView, tran);

            //2. 清除与User这间的连接
            //
            UserService.G.RemoveLink(lkidView, tran);

            //3. 删除圈子
            //
            g.Where(g.P_PGID == p_pgid).Delete(tran);

            return true;
        }

        /// <summary>
        /// 移除Group,同时移除Group关联的信息
        /// </summary>
        /// <param name="tran"></param>
        /// <param name="pgids"></param>
        protected bool DoRemoveGroup(QDbTran tran, params int[] pgids)
        {
            if (pgids == null || pgids.Length < 1)
                throw new BCFException("参数异常:(pgids == null || pgids.Length < 1)");
            
            //-------------------------------------
            //删除圈子
            //
            
            //清除与Resource这间的连接
            //
            ResourceService.G.RemoveLink(tran, pgids);

            //清除与User这间的连接
            //
            UserService.G.RemoveLink(tran, pgids);

            GroupM g = new GroupM();
            g.Where(g.PGID.In(pgids)).Delete(tran);

            return true;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="disable"></param>
        /// <param name="pgids"></param>
        /// <returns></returns>
        protected bool DoDisableGroup(bool disable, params int[] pgids)
        {
            if (pgids == null || pgids.Length < 1)
                throw new BCFException("参数异常:(pgids == null || pgids.Length < 1)");

            //-------------------------------------
            GroupM g = new GroupM();

            g.Is_Disabled.Value = disable;
            g.Last_Update.Value = DateTime.Now;

            g.Where(g.PGID.In(pgids));

            if (DEFINE_VALUE > 0)
                g.And(g.R_PGID == DEFINE_VALUE);

            //------------------------------------------------
            return g.Update();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="group"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        protected bool DoModifyGroup(T group, QDbTran tran)
        {
            GroupM g = new GroupM();

            g.Bind(group);
            g.PGID.IsInc = false;
            
            if (group.R_PGID < 1)
                g.R_PGID.IsInc = false;

            if (group.P_PGID == 0)
                g.P_PGID.IsInc = false;

            if (group.Order_Index == 0)
                g.Order_Index.IsInc = false;

            g.Create_Time.IsInc = false;
            g.Last_Update.Value = DateTime.Now;

            return g.Where(g.PGID == group.PGID).Update(tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="defines"></param>
        /// <returns></returns>
        protected bool DoClearGroups(params int[] defines)
        {
            if (defines.Length < 1)
                throw new BCFException("defines 参数不能为空");

            GroupM g = new GroupM();
            Resource_LinkedM rl = new Resource_LinkedM();
            User_LinkedM ul = new User_LinkedM();

            QView pgidS = g.Where(g.R_PGID.In(defines)).SubSelect(g.PGID);

            bool temp = true;
            //清理,与功能间的关联
            //			
            temp = rl.Where(rl.LK_OBJT_ID.In(pgidS))
                     .And(rl.LK_OBJT == Group.LK_OBJT)
                     .Delete() && temp;

            //清理,与用户之间的关联
            //
            temp = ul.Where(ul.LK_OBJT_ID.In(pgidS))
                     .And(ul.LK_OBJT == Group.LK_OBJT)
                     .Delete() && temp;

            //清空组
            //
            temp = g.Where(g.R_PGID.In(defines)).Delete() && temp;

            return temp;
        }
      

        //-----------------------------------------------
        /// <summary>
        /// 
        /// </summary>
        /// <param name="pgid"></param>
        /// <returns></returns>
        protected T DoGetGroup(int pgid)
        {
            GroupM g = new GroupM();

            return g.Where(g.PGID == pgid).SelectOne<T>();
        }
        
        /// <summary>
        /// 
        /// </summary>
        /// <param name="pgCode"></param>
        /// <returns></returns>
        protected T DoGetGroup(string pgCode)
        {
            GroupM g = new GroupM();

            return g.Where(g.PG_Code == pgCode).SelectOne<T>();
        }


        /// <summary>
        /// 
        /// </summary>
        /// <param name="pgidS"></param>
        /// <returns></returns>
        protected List<T> DoGetGroups(params int[] pgidS)
        {
            GroupM g = new GroupM();

            g.Where(g.Is_Visibled != 0);

            if (pgidS.Length > 0)
                g.And(g.PGID.In(pgidS));

            return g.Select<T>();
        }

        /// <summary>
        /// 获取User所在的组
        /// </summary>
        protected List<T> DoGetInGroups(int puid, bool incDisabled)
        {
            return GetInGroupsSQ(puid, incDisabled, false).Select<T>();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="puid"></param>
        /// <param name="incDisabled"></param>
        /// <param name="forView"></param>
        /// <returns></returns>
        public SQuery GetInGroupsSQ(int puid, bool incDisabled, bool forView)
        {
            GroupM g = new GroupM();
            User_LinkedM ul = new User_LinkedM();

            SQuery SQ = new SQuery();

            ul.IncludeAll(false);

            if (forView == true)
            {
                g.IncludeAll(false);
                g.PGID.IsInc = true;
            }

            SQ.From(g.NoLock())
              .InnerJoin(ul).On(g.PGID == ul.LK_OBJT_ID)
              .Where(ul.PUID == puid);

            if (incDisabled == false)
                SQ.And(g.Is_Disabled != 1);

            if (DEFINE_VALUE > 0)
                SQ.And(g.R_PGID == DEFINE_VALUE.ToString());

            if (forView == false)
                SQ.OrderBy(g.Order_Index);

            return SQ;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="incDisabled"></param>
        /// <param name="incUnVisible"></param>
        /// <param name="p_pgids"></param>
        /// <returns></returns>
        protected List<Group> DoGetGroups(bool incDisabled, bool incUnVisible, params int[] p_pgids)
        {
            return QEntity.GetList<Group>(DoGetGroups(null, incDisabled, incUnVisible, p_pgids));
        }

        /// <summary>
        /// 获取组
        /// </summary>
        /// <param name="joinTable"></param>
        /// <param name="incDisabled"></param>
        /// <param name="incUnVisible"></param>
        /// <param name="p_pgids"></param>
        /// <returns></returns>
        protected DataTable DoGetGroups(QTable joinTable, bool incDisabled, bool incUnVisible, params int[] p_pgids)
        {
            if (p_pgids == null || p_pgids.Length < 1)
                return null;

            //-------------------------------------
            GroupM g = new GroupM();
            SQuery SQ = new SQuery();

            SQ.From(g.NoLock());

            if (joinTable != null)
                SQ.InnerJoin(joinTable.NoLock()).On(g.PGID == joinTable["PGID"]);

            SQ.Where(g.P_PGID.In(p_pgids));

            if (incDisabled == false)
                SQ.And(g.Is_Disabled != 1);

            if (incUnVisible == false)
                SQ.And(g.Is_Visibled != 0);

            return SQ.OrderBy(g.Order_Index)
                     .Select();
        }

        /// <summary>
        /// 获取用户相关的组
        /// </summary>
        /// <param name="puid">用户PUID</param>
        /// <param name="p_pgids">目标组的P_PGID组</param>
        /// <returns></returns>
        protected List<Group> DoGetGroupsInUser(int puid, params int[] p_pgids)
        {
            GroupM g = new GroupM();
            SQuery SQ = new SQuery();

            User_LinkedM ul = new User_LinkedM();
            ul.IncludeAll(false);


            SQ.From(g)
              .InnerJoin(ul).On(g.PGID == ul.LK_OBJT_ID).And(ul.LK_OBJT == Group.LK_OBJT);

            SQ.Where(QCondition.TRUE);

            if (p_pgids.Length > 0)
                SQ.And(g.P_PGID.In(p_pgids));

            return
            SQ  .And(ul.PUID == puid)
                .And(g.Is_Disabled != true)
                .And(g.Is_Visibled != false)
              .OrderBy(g.Order_Index, OrderType.ASC)
              .Select<Group>();
        }
        
        /// <summary>
        /// 获取所有组
        /// </summary>
        /// <returns></returns>
        protected List<T> DoGetGroups()
        {
            GroupM g = new GroupM();
            
            g.Where(g.Is_Visibled != 0);

            if (DEFINE_VALUE > 0)
                g.And(g.R_PGID == DEFINE_VALUE);

            return g.NoLock()
                    .OrderBy(g.Order_Index)
                    .Select<T>();
        }

        //---------------------------------------------

        #endregion

        /// <summary>
        /// 通过PGID获取PG_Code
        /// </summary>
        /// <param name="pgid">PGID</param>
        /// <returns>PG_Code</returns>
        public string PGIDToCode(int pgid)
        {
            GroupM g = new GroupM();

            return g.NoLock()
                    .Where(g.PGID == pgid)
                    .SelectValue<string>(g.PG_Code, null);


        }

        /// <summary>
        /// 通过PG_Code获取PGID
        /// </summary>
        /// <param name="code">PG_Code</param>
        /// <returns>PGID</returns>
        public int CodeToPGID(string code)
        {
            GroupM g = new GroupM();

            g.Where(g.PG_Code == code);

            if (DEFINE_VALUE > 0)
                g.And(g.R_PGID == DEFINE_VALUE);

            return g.NoLock().SelectValue<int>(g.PGID, 0);

        }

        /// <summary>
        /// 是否存有组内用户
        /// </summary>
        /// <param name="pgid">组PGID</param>
        /// <returns></returns>
        public bool ExistsUsers(int pgid)
        {
            return ExistsUsers(pgid, true);
        }

        /// <summary>
        /// 是否存有组内用户
        /// </summary>
        /// <param name="pgid">组PGID</param>
        /// <param name="incDisabled">是否包括禁用的</param>
        /// <returns></returns>
        public bool ExistsUsers(int pgid, bool incDisabled)
        {
            User_LinkedM ul = new User_LinkedM();
            UserM u = new UserM();
            SQuery SQ = new SQuery();

            return SQ.From(ul.NoLock())
                     .InnerJoin(u.NoLock())
                       .On(ul.PUID == u.PUID)
                     .Where(ul.LK_OBJT == Group.LK_OBJT)
                     .And(ul.LK_OBJT_ID == pgid)
                     .Exists();
        }

        /// <summary>
        /// 是否存有子级
        /// </summary>
        /// <param name="pgid">组PGID</param>
        /// <param name="incDisabled">是否包括禁用的</param>
        /// <param name="incUnVisible">是否包括不可见的</param>
        /// <returns></returns>
        public bool ExistsChildern(int pgid, bool incDisabled, bool incUnVisible)
        {
            GroupM g = new GroupM();

            g.Where(g.P_PGID == pgid);

            if (incDisabled == false)
                g.And(g.Is_Disabled != 1);

            if (incUnVisible == false)
                g.And(g.Is_Visibled == 1);

            return g.NoLock().Exists();
        }

        /// <summary>
        /// 是否存有关联的资源
        /// </summary>
        /// <param name="pgid">组PGID</param>
        /// <returns></returns>
        public bool ExistsResources(int pgid)
        {
            Resource_LinkedM rl = new Resource_LinkedM();

            return rl.NoLock()
                     .Where(rl.LK_OBJT == Group.LK_OBJT)
                     .And(rl.LK_OBJT_ID == pgid)
                     .Exists();
        }

        /// <summary>
        /// 是否存有组(@列=值)
        /// </summary>
        /// <param name="col">列</param>
        /// <param name="val">值</param>
        /// <returns></returns>
        public bool Exists(QColumn col, int val)
        {
            GroupM g = new GroupM();
            
            g.Where(col == val);

            if (DEFINE_VALUE > 0)
                g.And(g.R_PGID == DEFINE_VALUE);

            return g.NoLock().Exists();
        }
    }
}
