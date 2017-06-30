using System;
using System.Collections.Generic;
using System.Text;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 用户扩展对象
    /// </summary>
    [Serializable]
    public class UserEx : BCF_User ,IOPSxExtend
    {
        /// <summary>
        /// 组PGID
        /// </summary>
        public int PGID { get; set; }

        /// <summary>
        /// 绑定委托数据源
        /// </summary>
        /// <param name="get"></param>
        /// <param name="src"></param>
        public override void Bind(GetHandler get, SourceType src)
        {
            PGID = Eval<int>(get("PGID"));

            base.Bind(get, src);
        }

        public override IQBinder Clone()
        {
            return new UserEx();
        }

        #region IOPSxExtend 成员

        /// <summary>
        /// 获取OPSx扩展信息
        /// </summary>
        /// <returns></returns>
        public string OPSx()
        {
            return OPSxService.G.GetOPSx(User.LK_OBJT, PUID);
        }

        /// <summary>
        /// 设置OPSx扩展信息
        /// </summary>
        /// <param name="opsXml">ops-xml</param>
        public void OPSx(string opsXml)
        {
            OPSxService.G.SetOPSx(User.LK_OBJT, PUID, opsXml);
        }

        #endregion
    }

    /// <summary>
    /// 用户对象
    /// </summary>
    [Serializable]
    public class User : BCF_User, IOPSxExtend
    {
        #region IOPSxExtend 成员

        /// <summary>
        /// 获取OPSx扩展信息
        /// </summary>
        /// <returns></returns>
        public string OPSx()
        {
            return OPSxService.G.GetOPSx(User.LK_OBJT, PUID);
        }

        /// <summary>
        /// 设置OPSx扩展信息
        /// </summary>
        /// <param name="opsXml">ops-xml</param>
        public void OPSx(string opsXml)
        {
            OPSxService.G.SetOPSx(User.LK_OBJT, PUID, opsXml);
        }

        #endregion

        public override IQBinder Clone()
        {
            return new User();
        }


        private static int _LK_OBJT = -1;
        /// <summary>
        /// 被关联时的对象标识
        /// </summary>
        public static int LK_OBJT
        {
            get
            {
                if (_LK_OBJT < 0)
                    _LK_OBJT = ConfigService.Get_LK_OBJT(UserM.G.TableName);

                return _LK_OBJT;
            }
        }

        #region EncryptPassWd

        /// <summary>
        /// 对密码进行加码
        /// </summary>
        /// <param name="userId">用户登录帐号</param>
        /// <param name="passWd">未加密的代码</param>
        /// <returns>加密后的代码</returns>
        public static string EncryptPassWd(string userId, string passWd)
        {
            return BCF_PARAMS.ToHSA1(userId + "#" + passWd.Trim());
        }
        
        /// <summary>
        /// 修改一个用户的密码
        /// </summary>
        /// <param name="userId">用户登录帐号</param>
        /// <param name="passWd">未加密的代码</param>
        /// <returns>是否成功</returns>
        public static bool ModifyPassWd(string userId, string passWd)
        {
            return UserService.G.ModifyUserPassWd(userId, passWd);
        }

        /// <summary>
        /// 如果Pass_Wd 的宽度没有40位,则进行加密(不管是否为空密码)(加密后为40位长)仅负责进行密码加密
        /// </summary>
        public bool EncryptPassWd()
        {
            if (string.IsNullOrEmpty(this.Pass_Wd))
                return false;

            if (this.Pass_Wd.Length != 40)
            {
                this.Pass_Wd = User.EncryptPassWd(this.User_Id, this.Pass_Wd);

                return true;
            }
            else
                return false;
        }

        #endregion

      
        /// <summary>
        /// 是否在一个组里
        /// </summary>
        /// <param name="pgid">组ID</param>
        /// <returns>是否有加入</returns>
        public bool IsInGroup(int pgid)
        {
            return IsInGroup(this.PUID, pgid);
        }

        /// <summary>
        /// 是否在一个组里
        /// </summary>
        /// <param name="puid">用户ID</param>
        /// <param name="pgid">组ID</param>
        /// <returns>是否有加入</returns>
        public static bool IsInGroup(int puid, int pgid)
        {
            User_LinkedM ul = new User_LinkedM();

            return
            ul.NoLock()
              .Where(ul.PUID == puid)
                .And(ul.LK_OBJT == Group.LK_OBJT)
                .And(ul.LK_OBJT_ID == pgid)
              .Exists();
        }

        /// <summary>
        /// 加入一个组
        /// </summary>
        /// <param name="pgid">组ID</param>
        /// <param name="tran">事务</param>
        /// <returns>是否成功</returns>
        public bool JoinToGroup(int pgid, QDbTran tran)
        {
            return JoinToGroup(this.PUID, pgid, tran);            
        }

        /// <summary>
        /// 脱离与一个组的关联
        /// </summary>
        /// <param name="pgid">组ID</param>
        /// <param name="tran">事务</param>
        /// <returns>是否成功</returns>
        public bool LeaveGroup(int pgid, QDbTran tran)
        {
            return LeaveGroup(this.PUID, pgid, tran);
        }

        /// <summary>
        /// 清理与某一类组的关系
        /// </summary>
        /// <param name="groupDefine"></param>
        /// <param name="tran"></param>
        /// <returns></returns>
        public bool ClearGroupLinked(int groupDefine,QDbTran tran)
        {
            return ClearGroupLinked(this.PUID, groupDefine, tran);
        }

        /// <summary>
        /// 加入一个组
        /// </summary>
        /// <param name="puid">用户ID</param>
        /// <param name="pgid">组ID</param>
        /// <param name="tran">事务</param>
        /// <returns>是否成功</returns>
        public static bool JoinToGroup(int puid, int pgid, QDbTran tran)
        {
            if (pgid==0 || puid==0)
                return false;

            User_LinkedM ul = new User_LinkedM();

            ul.PUID.Value = puid;
            ul.LK_OBJT.Value = Group.LK_OBJT;
            ul.LK_OBJT_ID.Value = pgid;
            ul.LK_Operate.Value = "+";

            if (ul.Where(ul.PUID == puid).And(ul.LK_OBJT == Group.LK_OBJT).And(ul.LK_OBJT_ID == pgid).Exists(tran))
                return false;
            else
                return ul.Insert(tran);
        }

        /// <summary>
        /// 脱离与一个组的关联
        /// </summary>
        /// <param name="puid">用户ID</param>
        /// <param name="pgid">组ID</param>
        /// <param name="tran">事务</param>
        /// <returns>是否成功</returns>
        public static bool LeaveGroup(int puid, int pgid, QDbTran tran)
        {
            if (pgid==0 || puid== 0)
                return false;

            User_LinkedM ul = new User_LinkedM();

            return ul.Where(ul.LK_OBJT == Group.LK_OBJT)
                       .And(ul.LK_OBJT_ID == pgid)
                       .And(ul.PUID == puid)
                     .Delete(tran);
        }

        /// <summary>
        /// 清除与某一类组之间的关联
        /// </summary>
        /// <param name="puid">用户ID</param>
        /// <param name="groupDefine">组定义</param>
        /// <param name="tran">事务</param>
        /// <returns>是否成功</returns>
        public static bool ClearGroupLinked(int puid, int groupDefine, QDbTran tran)
        {
            GroupM g = new GroupM();
            QView pgidS = g.Where(g.R_PGID == groupDefine).SubSelect(g.PGID);

            User_LinkedM ul = new User_LinkedM();

            //清理,与用户之间的关联
            //
            return ul.Where(ul.LK_OBJT_ID.In(pgidS))
                     .And(ul.LK_OBJT == Group.LK_OBJT)
                     .And(ul.PUID == puid)
                     .Delete(tran);
        }
    }
}
