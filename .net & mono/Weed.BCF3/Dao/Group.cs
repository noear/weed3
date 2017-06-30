using System;
using System.Collections.Generic;
using System.Text;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 组实体
    /// </summary>
    public class Group : BCF_Group , IOPSxExtend
    {
        #region IOPSxExtend 成员

        /// <summary>
        /// 获取OPSx扩展信息
        /// </summary>
        /// <returns></returns>
        public string OPSx()
        {
            return OPSxService.G.GetOPSx(Group.LK_OBJT, PGID);
        }

        /// <summary>
        /// 设置OPSx扩展信息
        /// </summary>
        /// <param name="opsXml">ops-xml</param>
        public void OPSx(string opsXml)
        {
            OPSxService.G.SetOPSx(Group.LK_OBJT, PGID, opsXml);
        }

        #endregion

        /// <summary>
        /// 组的[R_PGID]字段组名称
        /// </summary>
        internal static string R_PGID_NAME
        {
            get
            {
                return GroupM.G.R_PGID.Name;
            }
        }

        public override IQBinder Clone()
        {
            return new Group();
        }

        private static int _LK_OBJT = -1;
        /// <summary>
        /// LK_OBJT
        /// </summary>
        public static int LK_OBJT
        {
            get
            {
                if (_LK_OBJT < 0)
                    _LK_OBJT = ConfigService.Get_LK_OBJT(GroupM.G.TableName);

                return _LK_OBJT;
            }
        }

        /// <summary>
        /// 移除与用户之间的关联
        /// </summary>
        /// <param name="tran">事务对象</param>
        /// <param name="puidS">用户PUID组</param>
        /// <returns></returns>
        public bool RemoveUserLink(QDbTran tran, params string[] puidS)
        {
            if (this.PGID == 0 || puidS.Length == 0)
                return false;

            User_LinkedM ul = new User_LinkedM();

            return ul.Where(ul.LK_OBJT == Group.LK_OBJT)
                       .And(ul.LK_OBJT_ID == this.PGID)
                       .And(ul.PUID.In(puidS))
                     .Delete(tran);
        }
        /// <summary>
        /// 移除与资源之间的关联
        /// </summary>
        /// <param name="tran">事务对象</param>
        /// <param name="rsidS">资源RSID组</param>
        /// <returns></returns>
        public bool RemoveResourceLink(QDbTran tran, params int[] rsidS)
        {
            if (this.PGID == 0 || rsidS.Length == 0)
                return false;

            Resource_LinkedM rl = new Resource_LinkedM();

            return rl.Where(rl.LK_OBJT == Group.LK_OBJT)
                       .And(rl.LK_OBJT_ID == this.PGID)
                       .And(rl.RSID.In(rsidS))
                     .Delete(tran);
        }

        /// <summary>
        /// 存在多少具有相同PG_Code的Group
        /// </summary>
        /// <param name="pgCode">PG_Code值</param>
        /// <returns>多少</returns>
        public static int ExistsPGCode(string pgCode)
        {
            GroupM g = new GroupM();

            return g.Where(g.PG_Code == pgCode).Count();
        }

        /// <summary>
        /// 根据PGID获取对应的PG_Code
        /// </summary>
        /// <param name="pgid">PGID</param>
        /// <returns>null or Code</returns>
        public static string PGIDToCode(int pgid)
        {
            return GroupService.G.PGIDToCode(pgid);
        }

        /// <summary>
        /// 根据PG_Code获取对应的PGID
        /// </summary>
        /// <param name="code">PG_Code</param>
        /// <returns>null or PGID</returns>
        public static int CodeToPGID(string code)
        {
            return GroupService.G.CodeToPGID(code);
        }
    }
}
