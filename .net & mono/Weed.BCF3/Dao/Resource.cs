using System;
using System.Collections.Generic;
using System.Text;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 权限表达式
    /// </summary>
    [Serializable]
    public enum PExpress : byte
    {
        /// <summary>
        /// exp = 0
        /// </summary>
        Null = 0,
        /// <summary>
        /// exp = 1
        /// </summary>
        ReadOnly = 1,
        /// <summary>
        /// exp = 2
        /// </summary>
        Write = 2,
        /// <summary>
        /// exp = 4
        /// </summary>
        Control = 4,
    }

    /// <summary>
    /// 资源
    /// </summary>
    [Serializable]
    public class Resource : BCF_Resource, IOPSxExtend
    {
        #region IOPSxExtend 成员

        /// <summary>
        /// 获取OPSx扩展信息
        /// </summary>
        /// <returns></returns>
        public string OPSx()
        {
            return OPSxService.G.GetOPSx(Resource.LK_OBJT, RSID);
        }

        /// <summary>
        /// 设置OPSx扩展信息
        /// </summary>
        /// <param name="opsXml">ops-xml</param>
        public void OPSx(string opsXml)
        {
            OPSxService.G.SetOPSx(Resource.LK_OBJT, RSID, opsXml);
        }

        #endregion

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public override IQBinder Clone()
        {
            return new Resource();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pgid"></param>
        /// <returns></returns>
        public bool IsInGroup(string pgid)
        {
            return false;
        }

        #region JoinToGroup & LeaveGroup

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pgid"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public bool JoinToGroup(int pgid, QDbTran tran)
        {
            return JoinToGroup(pgid, PExpress.Write, tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pgid"></param>
        /// <param name="exp"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public bool JoinToGroup(int pgid, PExpress exp, QDbTran tran)
        {
            return JoinToGroup(this.RSID, pgid, exp, tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pgid"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public bool LeaveGroup(int pgid, QDbTran tran)
        {
            return LeaveGroup(this.RSID, pgid, tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="rsid"></param>
        /// <param name="pgid"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public static bool JoinToGroup(int rsid, int pgid, QDbTran tran)
        {
            return JoinToGroup(rsid, pgid, PExpress.Write, tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="rsid"></param>
        /// <param name="pgid"></param>
        /// <param name="exp"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public static bool JoinToGroup(int rsid, int pgid, PExpress exp, QDbTran tran)
        {
            if (pgid==0 || rsid==0)
                return false;

            Resource_LinkedM rl = new Resource_LinkedM();

            rl.RSID.Value = rsid;
            rl.LK_OBJT.Value = Group.LK_OBJT;
            rl.LK_OBJT_ID.Value = pgid;
            rl.LK_Operate.Value = "+";
            rl.P_Express.Value = (byte)exp;


            if (rl.Where(rl.RSID == rsid)
                .And(rl.LK_OBJT == Group.LK_OBJT)
                .And(rl.LK_OBJT_ID == pgid).NoLock().Exists())
                return false;
            else
                return rl.Insert(tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="rsid"></param>
        /// <param name="pgid"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public static bool LeaveGroup(int rsid, int pgid, QDbTran tran)
        {
            if (pgid==0 || rsid==0)
                return false;

            Resource_LinkedM rl = new Resource_LinkedM();

            return
            rl.Where(rl.LK_OBJT == Group.LK_OBJT)
                .And(rl.LK_OBJT_ID == pgid)
                .And(rl.RSID == rsid).Delete(tran);
        }

        #endregion

        #region JoinToUser & LeaveUser

        /// <summary>
        /// 
        /// </summary>
        /// <param name="puid"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public bool JoinToUser(int puid, QDbTran tran)
        {
            return JoinToUser(puid, PExpress.Write, tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="puid"></param>
        /// <param name="exp"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public bool JoinToUser(int puid, PExpress exp, QDbTran tran)
        {
            return JoinToUser(this.RSID, puid, exp, tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="puid"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public bool LeaveUser(int puid, QDbTran tran)
        {
            return LeaveUser(this.RSID, puid, tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="rsid"></param>
        /// <param name="puid"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public static bool JoinToUser(int rsid, int puid, QDbTran tran)
        {
            return JoinToUser(rsid, puid, PExpress.Write, tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="rsid"></param>
        /// <param name="puid"></param>
        /// <param name="exp"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public static bool JoinToUser(int rsid, int puid, PExpress exp, QDbTran tran)
        {
            if (rsid==0 || rsid==0)
                return false;

            Resource_LinkedM rl = new Resource_LinkedM();

            rl.RSID.Value = rsid;
            rl.LK_OBJT.Value = User.LK_OBJT;
            rl.LK_OBJT_ID.Value = puid;
            rl.LK_Operate.Value = "+";
            rl.P_Express.Value = (byte)exp;

            if (rl.Where(rl.RSID == rsid)
                .And(rl.LK_OBJT == User.LK_OBJT)
                .And(rl.LK_OBJT_ID == puid).NoLock().Exists())
                return false;
            else
                return rl.Insert(tran);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="rsid"></param>
        /// <param name="puid"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public static bool LeaveUser(int rsid, int puid, QDbTran tran)
        {
            if (rsid==0 || puid==0)
                return false;

            Resource_LinkedM rl = new Resource_LinkedM();

            return
            rl.Where(rl.LK_OBJT == User.LK_OBJT)
                .And(rl.LK_OBJT_ID == puid)
                .And(rl.RSID == rsid)
              .Delete(tran);
        }


        #endregion

        /// <returns>null or RSID</returns>
        public static int CodeToRSID(string code)
        {
            return ResourceService.G.CodeToRSID(code);
        }

        /// <returns>null or Code</returns>
        public static string RSIDToCode(int rsid)
        {
            return ResourceService.G.RSIDToCode(rsid);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="rsid"></param>
        /// <param name="puid"></param>
        /// <returns></returns>
        public static PExpress ExpressOfUser(int rsid, int puid)
        {
            if (rsid < 1 || puid < 1)
                return PExpress.Null;

            SQuery SQ = BCF_PARAMS.NewSQ();
            ResourceExView rv = new ResourceExView(true);
            Resource_LinkedM rl = new Resource_LinkedM();

            //------>>找出属于用户所在组(部门,角色)
            QView tempView = GroupService.G.GetInGroupsSQ(puid, false, true).SubSelect().As("A");
            //-------<<

            //------>>找出属于用户个人所有的RSID
            tempView = SQ.From(rl)
                         .Where(rl.RSID == rsid)
                         .BeginAnd()
                            .Begin(rl.LK_OBJT == User.LK_OBJT)
                                .And(rl.LK_OBJT_ID == puid)
                            .End()
                            .BeginOr(rl.LK_OBJT == Group.LK_OBJT)
                                .And(rl.LK_OBJT_ID.In(tempView))
                            .End()
                         .End()
                         .SubSelect(rl.RSID, rl.P_Express).As(rl.AsName);
            //------<<

            int pex = SQ.From(rv)
                           .Join(tempView, JoinType.Inner)
                             .On(rv.RSID == rl.RSID)
                           .Where(rv.Is_Disabled != 1).And(rv.PKG_Is_Disabled != true)
                           .SelectValue<int>(rl.P_Express.Max(), 0);
            //------<<


            //--------------------------------------------------------------------------

            switch (pex)
            {
                case 1:
                    return PExpress.ReadOnly;

                case 2:
                    return PExpress.Write;

                case 4:
                    return PExpress.Control;

                default:
                    return PExpress.Null;
            }
        }

        private static int _LK_OBJT = 0;
        /// <summary>
        /// LK_OBJT
        /// </summary>
        public static int LK_OBJT
        {
            get
            {
                if (_LK_OBJT == 0)
                    _LK_OBJT = ConfigService.Get_LK_OBJT(ResourceM.G.TableName);

                return _LK_OBJT;
            }
        }
    }    
}
