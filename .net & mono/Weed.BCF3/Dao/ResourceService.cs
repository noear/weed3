using System;
using System.Collections.Generic;

using System.Text;
using System.Data;

using Weed;
using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 资源管理服务
    /// </summary>
    public class ResourceService : BCF_PARAMS
    {
        /// <summary>
        /// 静态实例
        /// </summary>
        public static ResourceService G = new ResourceService();

           
        #region 管理

        /// <summary>
        /// 添加资源
        /// </summary>
        /// <param name="resource">资源实体</param>
        /// <param name="tran">事务</param>
        /// <returns>是否成功</returns>
        public int AppendResource(Resource resource, QDbTran tran)
        {
            if (resource == null)
                return 0;

            if (string.IsNullOrEmpty(resource.Uri_Target))
                resource.Uri_Target = "_self";

            resource.Create_Time = DateTime.Now;
            resource.Last_Update = resource.Create_Time;

            ResourceM r = new ResourceM();
            r.Bind(resource);

            r.Insert(true, tran);

            return r.INSERTED_ID;
        }

        /// <summary>
        /// 移除资源，同时移除资源与其它对象的关联信息
        /// </summary>
        /// <param name="rsids">资源ID组</param>
        public bool RemoveResource(params int[] rsids) 
        { 
            return this.RemoveResource(null, rsids);
        }

        /// <summary>
        /// 移除资源，同时移除资源与其它对象的关联信息
        /// </summary>
        /// <param name="rsids">资源ID组</param>
        /// <param name="tran">事务</param>
        public bool RemoveResource(QDbTran tran, params int[] rsids)
        {
            Resource_LinkedM rl = new Resource_LinkedM();
            ResourceM r = new ResourceM();

            //删除关联表数据
            //
            bool temp = true;
            temp = temp && rl.Where(rl.RSID.In(rsids)).Delete(tran);

            //删除资源表数据
            temp = temp && r.Where(r.RSID.In(rsids)).Delete(tran);

            return temp;
        }


        /// <summary>
        /// 移除与"Group"或"User"的关联
        /// </summary>
        /// <param name="lkidS">关联对象的ID组（组ID或用户ID）</param>
        /// <returns>是否成功</returns>
        public bool RemoveLink(params int[] lkidS) 
        { 
            return this.RemoveLink(null, lkidS); 
        }

        /// <summary>
        /// 移除与"Group"或"User"的关联
        /// </summary>
        /// <param name="lkidS">关联对象的ID组（组ID或用户ID）</param>
        /// <param name="tran">事务</param>
        /// <returns>是否成功</returns>
        public bool RemoveLink(QDbTran tran, params int[] lkidS)
        {
            Resource_LinkedM rl = new Resource_LinkedM();

            return rl.Where(rl.LK_OBJT_ID.In(lkidS)).Delete(tran);
        }

        public bool RemoveLink(string rsid, int lk_objt, int lk_objt_id)
        {
            return RemoveLink(null, rsid, lk_objt, lk_objt_id);
        }

        public bool RemoveLink(QDbTran tran, string rsid, int lk_objt, int lk_objt_id)
        {
            Resource_LinkedM rl = new Resource_LinkedM();

            return rl.Where(rl.RSID == rsid)
                     .And(rl.LK_OBJT == lk_objt)
                     .And(rl.LK_OBJT_ID == lk_objt_id).Delete(tran);
        }

        internal bool RemoveLink(QView lkidView, QDbTran tran)
        {
            Resource_LinkedM rl = new Resource_LinkedM();

            return rl.Where(rl.LK_OBJT_ID.In(lkidView)).Delete(tran);
        }

        /// <summary>
        /// 修改资源信息
        /// </summary>
        /// <param name="resource">资源实体</param>
        /// <returns>是否成功</returns>
        public bool ModifyResource(Resource resource)
        {
            ResourceM r = new ResourceM();

            r.Bind(resource);

            r.RSID.IsInc = false;
            r.Create_Time.IsInc = false;

            r.Last_Update.Value = DateTime.Now;

            return r.NoLock().Where(r.RSID == resource.RSID).Update();
        }

        /// <summary>
        /// 是否存在某资源
        /// </summary>
        /// <param name="rsid">资源ID</param>
        /// <returns>是否存在</returns>
        public bool Exists(int rsid)
        {
            ResourceM r = new ResourceM();

            return r.NoLock().Where(r.RSID == rsid).Exists();
        }

        /// <summary>
        /// 获取某一资源信息
        /// </summary>
        /// <param name="rsid">资源ID</param>
        /// <returns>资源实体</returns>
        public Resource GetResource(int rsid)
        {
            ResourceM r = new ResourceM();

            return r.NoLock().Where(r.RSID == rsid).SelectOne<Resource>();
        }

        #endregion

        #region GetResources

        /// <summary>
        /// 获取用户相关的包
        /// </summary>
        /// <param name="puid"></param>
        /// <param name="packageIds"></param>
        /// <returns></returns>
        public List<Package> GetInUserPackages(int puid, params int[] packageIds)
        {
            var resList = GetInUserResources(puid, packageIds);
            List<int> IDs = new List<int>();

            foreach (var r in resList)
            {
                if (IDs.Contains(r.PGID) == false)
                    IDs.Add(r.PGID);
            }

            GroupM m = new GroupM();

            return m.Where(m.PGID.In(IDs))
                    .OrderBy(m.Order_Index, OrderType.ASC).Select<Package>();
        }

        #region 根据关链数据获取 Resources

        /// <summary>
        /// 获取某一个用户的资源列表
        /// </summary>
        /// <param name="packageIds">对用户资源的所在包,进行限定</param>
        /// <param name="puid">用户ID</param>
        /// <returns>资源扩展信息列表</returns>
        public List<ResourceEx> GetInUserResources(int puid, params int[] packageIds)
        {
            var list = _SQ_GetInUserResources(puid, packageIds).Select<ResourceEx>();
            var list2 = new List<ResourceEx>();
            var list3 = new List<int>();

            foreach (var r in list)
            {
                if (list3.Contains(r.RSID))
                    continue;
                else
                    list3.Add(r.RSID);

                list2.Add(r);
            }

            return list2;
        }

        /// <summary>
        /// 获取某一个用户的资源列表
        /// </summary>
        /// <param name="packageIds">对用户资源的所在包,进行限定</param>
        /// <param name="puid">用户ID</param>
        /// <param name="isComposePG">是否组合包的名字</param>
        /// <returns>资源扩展信息列表</returns>
        public DataTable GetInUserResources1(int puid, bool isComposePG, params int[] packageIds)
        {
            DataTable dt = _SQ_GetInUserResources(puid, packageIds).Select();

            if (isComposePG)
                return _ComposePG(dt);
            else
                return dt;
        }

        private SQuery _SQ_GetInUserResources(int puid, params int[] packageIds)
        {
            if (puid <= 0)
                throw new BCFException("GetInUserResources(puid)::@puid 必须大于0");

            ResourceExView rv = new ResourceExView(false);
            Resource_LinkedM rl = new Resource_LinkedM();
            SQuery SQ = NewSQ();

            //------>>找出属于用户所在组(部门,角色)
            QView tempView = GroupService.G.GetInGroupsSQ(puid, false, true).SubSelect();
            //-------<<

            //------>>找出属于用户个人所有的RSID
            tempView = SQ.From(rl)
                         .BeginWhere(rl.LK_OBJT == User.LK_OBJT)
                            .And(rl.LK_OBJT_ID == puid)
                         .End()
                         .BeginOr(rl.LK_OBJT == Group.LK_OBJT)
                            .And(rl.LK_OBJT_ID.In(tempView))
                         .End()
                         .SubSelect(rl.RSID, rl.LK_OBJT, rl.P_Express).As("RL");
            //------<<

            SQ.From(rv)
              .Join(tempView, JoinType.Inner)
                .On(rv.RSID == tempView.Column(rl.RSID))
              .Where(rv.Is_Disabled != 1)
              .And(rv.PKG_Is_Disabled == false);

            if (packageIds.Length > 0)
                SQ.And(rv.PGID.In(packageIds));

            SQ.Distinct();

            this._Sort(SQ, rv);

            return SQ;
        }

        private void _Sort(SQuery sq,ResourceExView rv)
        {
            sq.OrderBy(rv.PKG_Order_Index).And(rv.Order_Index);
        }

        /// <summary>
        /// 获取Group[in @pgidS]的资源
        /// </summary>
        /// <param name="pgidS">对组资源的所在包,进行限定</param>
        /// <returns>资源扩展信息列表</returns>
        public List<ResourceEx> GetInGroupResources(params int[] pgidS)
        {
            return _SQ_GetInGroupResources(pgidS).Select<ResourceEx>();
        }

        private DataTable _ComposePG(DataTable dt)
        {
            int len = dt.Rows.Count;

            for (int i = 0; i < len; i++)
            {
                DataRow row = dt.Rows[i];

                DataRow[] rs = dt.Select("RSID='" + row["RSID"].ToString() + "'");
                int rs_len = rs.Length;
                if (rs_len > 1)
                {
                    StringBuilder sb = new StringBuilder();

                    sb.Append(",");
                    _TryAdd(sb, row["PG_CN_Name"]);

                    for (int j = 1; j < rs_len; j++)
                    {
                        _TryAdd(sb, rs[j]["PG_CN_Name"]);

                        dt.Rows.Remove(rs[j]);
                        len--;
                    }

                    if (sb.Length > 0)
                        row["PG_CN_Name"] = sb.ToString().Trim(',');
                }
            }

            return dt;
        }
        private void _TryAdd(StringBuilder sb, object name)
        {
            if (name == null || name == DBNull.Value)
                return;

            if (sb.ToString().IndexOf("," + name.ToString() + ",") < 0)
                sb.Append(name.ToString()).Append(",");
        }

        /// <summary>
        /// 获取Group[in @pgidS]的资源
        /// </summary>
        /// <param name="pgidS">对组资源的所在包,进行限定</param>
        /// <returns>资源扩展信息列表</returns>
        public DataTable GetInGroupResources1(bool isComposePG, params int[] pgidS)
        {
            DataTable dt = _SQ_GetInGroupResources(pgidS).Select();

            if (isComposePG)
                return _ComposePG(dt);
            else
                return dt;
        }

        private SQuery _SQ_GetInGroupResources(params int[] pgidS)
        {
            if (pgidS.Length == 0)
                throw new BCFException("GetInGroupResources(pgdiS)::@pgidS长度必须大于0");

            Resource_LinkedM rl = new Resource_LinkedM();
            rl.IncludeAll(false);
            rl.LK_OBJT.IsInc = true;
            rl.P_Express.IsInc = true;

            ResourceExView rv = new ResourceExView(false);

            SQuery SQ = NewSQ();

            SQ.From(rv)
              .InnerJoin(rl)
                .On(rv.RSID == rl.RSID)
              .Where(rl.LK_OBJT == Group.LK_OBJT)
                .And(rl.LK_OBJT_ID.In(pgidS))
                .And(rv.Is_Disabled == false)
                .And(rv.PKG_Is_Disabled == false);

            this._Sort(SQ, rv);

            return SQ;
        }

        #endregion

        /// <summary>
        /// 获取所有的Resource(资源)
        /// </summary>
        /// <returns>资源列表</returns>
        public List<Resource> GetResources()
        {
            return new ResourceM().Select<Resource>();
        }

        /// <summary>
        /// 获取指定包里的资源(不包括忆禁用的部分)
        /// </summary>
        /// <param name="packageIds">包ID组</param>
        /// <returns>资源列表</returns>
        public List<Resource> GetResources(params int[] packageIds)
        {
            return this.GetResources(false, packageIds);
        }

        private string _OrderColumnName;
        public ResourceService OrderBy(string columnName)
        {
            _OrderColumnName = columnName;

            return this;
        }


        /// <summary>
        /// 获取指定包里的资源
        /// </summary>
        /// <param name="packageIds">包ID组</param>
        /// <param name="incDisabled">是否包括忆禁用的部分</param>
        /// <returns>资源列表</returns>
        public List<Resource> GetResources(bool incDisabled, params int[] packageIds)
        {
            ResourceM r = new ResourceM();
            Resource_LinkedM rl = new Resource_LinkedM();
            rl.IncludeAll(false);

            SQuery SQ = NewSQ();

            SQ.From(r)
              .InnerJoin(rl)
                .On(rl.RSID == r.RSID)
              .Where(r.RSID != "0")
                .And(rl.LK_OBJT == Package.LK_OBJT)
                .And(rl.LK_OBJT_ID.In(packageIds));

            if (incDisabled == false)
                SQ.And(r.Is_Disabled == false);

            if (_OrderColumnName == null)
                SQ.OrderBy(r.Order_Index);
            else
            {
                if (r.Contains(_OrderColumnName))
                    SQ.OrderBy(r[_OrderColumnName]);
                else if (rl.Contains(_OrderColumnName))
                    SQ.OrderBy(r[_OrderColumnName]);
                else if (_OrderColumnName.IndexOf(",") > 0)
                    SQ.OrderBy(r.CN_Name.Exp(_OrderColumnName));
                else
                    SQ.OrderBy(r.Order_Index);
            }

            return SQ.Select<Resource>();
        }

        #endregion

        #region ClearLinked

        /// <summary>
        /// 清除与User[@puid]之间的连接关系
        /// </summary>
        public bool ClearInUserLinked(string puid, QDbTran tran)
        {
            if (puid == null || puid.Length == 0)
                return false;

            Resource_LinkedM rl = new Resource_LinkedM();

            return rl.Where(rl.LK_OBJT == User.LK_OBJT)
                       .And(rl.LK_OBJT_ID == puid)
                     .Delete(tran);
        }

        /// <summary>
        /// 清除与Group[@pgid]之间的连接关系
        /// </summary>
        public bool ClearInGroupLinked(string pgid, QDbTran tran)
        {
            if (pgid == null || pgid.Length < 1)
                return false;

            Resource_LinkedM rl = new Resource_LinkedM();

            return rl.Where(rl.LK_OBJT == Group.LK_OBJT)
                       .And(rl.LK_OBJT_ID == pgid)
                     .Delete(tran);
        }

        #endregion

        #region GetLinked

        public DataTable GetInLinked(int rsid)
        {
            if (rsid < 1)
                return null;

            Resource_LinkedM rl = new Resource_LinkedM();
            GroupM g = new GroupM();
            UserM u = new UserM();
            g.IncludeAll(false);
            g.CN_Name.As("LK_OBJT_Name").IsInc = true;
            u.IncludeAll(false);
            u.CN_Name.As("LK_OBJT_Name").IsInc = true;

            DataTable dt1 = rl.InnerJoin(u).On(rl.LK_OBJT_ID == u.PUID)
                             .And(rl.RSID == rsid).And(rl.LK_OBJT == User.LK_OBJT)
                             .Select();

            rl = new Resource_LinkedM();

            DataTable dt2 = rl.InnerJoin(g).On(rl.LK_OBJT_ID == g.PGID)
                             .And(rl.RSID == rsid)
                             .And(rl.LK_OBJT == Group.LK_OBJT)
                             .And(g.R_PGID != Package.DEFINE)
                             .Select();

            foreach (DataRow row in dt2.Rows)
                dt1.Rows.Add(row.ItemArray);

            return dt1;
        }

        /// <summary>
        /// 获取与User[@puid]之间的连接关系
        /// </summary>
        public List<Resource_Linked> GetInUserLinked(int puid, QDbTran tran)
        {
            if (puid <1)
                return null;

            Resource_LinkedM rl = new Resource_LinkedM();

            return rl.Where(rl.LK_OBJT == User.LK_OBJT)
                       .And(rl.LK_OBJT_ID == puid)
                     .Select<Resource_Linked>();
        }

        /// <summary>
        /// 获取与Group[@pgid]之间的连接关系
        /// </summary>
        public List<Resource_Linked> GetInGroupLinked(int pgid, QDbTran tran)
        {
            if (pgid < 1)
                return null;

            Resource_LinkedM rl = new Resource_LinkedM();

            return rl.Where(rl.LK_OBJT == Group.LK_OBJT)
                       .And(rl.LK_OBJT_ID == pgid)
                     .Select<Resource_Linked>();
        }

        /// <summary>
        /// 获取用户所在组的连接
        /// </summary>
        /// <param name="puid"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public List<Resource_Linked> GetInGroupOfUserLinked(int puid, QDbTran tran)
        {
            if (puid < 1)
                return null;

            Resource_LinkedM rl = new Resource_LinkedM();
            User_LinkedM ul = new User_LinkedM();

            QView ulv = ul.Where(ul.PUID == puid).And(ul.LK_OBJT == Group.LK_OBJT).SubSelect().As("T0");

            SQuery sq = new SQuery();

            return
            sq.From(ulv)
              .InnerJoin(rl).On(ul.LK_OBJT_ID == rl.LK_OBJT_ID)
                            .And(rl.LK_OBJT == Group.LK_OBJT)
              .Select<Resource_Linked>(rl.RSID, rl.LK_OBJT_ID, rl.LK_OBJT, rl.LK_Operate, rl.P_Express);
        }

        #endregion

        /// <summary>
        /// 通过Code获取RSID
        /// </summary>
        /// <param name="code">资源Code</param>
        /// <returns>null or RSID</returns>
        public int CodeToRSID(string code)
        {
            ResourceM r = new ResourceM();

            return r.Where(r.RS_Code == code)
                    .SelectValue<int>(r.RSID, 0);
        }

        /// <summary>
        /// 通过RSID获取Code
        /// </summary>
        /// <param name="rsid">资源ID</param>
        /// <returns>null or Code</returns>
        public string RSIDToCode(int rsid)
        {
            ResourceM r = new ResourceM();

            return r.Where(r.RSID == rsid)
                    .SelectValue<string>(r.RS_Code, null);
        }
    }
}
