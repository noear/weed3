using System;
using System.Collections.Generic;

using System.Text;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 用户管理服务
    /// </summary>
    [Serializable]
    public class UserService : BCF_PARAMS
    {
        /// <summary>
        /// 静态实例
        /// </summary>
        public static UserService G = new UserService();

        //----------------------------------

        /// <summary>
        /// 添加时,已实现自动[Pass_Wd]加密
        /// </summary>
        public int AppendUser(User user, QDbTran tran)
        {
            return AppendUser(user, true, tran);
        }

        public bool ExistsUser(string userID)
        {
            UserM u = new UserM();

            return u.Where(u.User_Id == userID).Exists();
        }

        public int AppendUser(User user, bool checkUserId, QDbTran tran)
        {
            if (user == null)
                return 0;

            user.Create_Time = DateTime.Now;
            user.Last_Update = user.Create_Time;
            user.EncryptPassWd();

            UserM u = new UserM();
            u.Bind(user);

            u.Pass_Wd.IsInc = (!string.IsNullOrEmpty(u.Pass_Wd.Value));


            if (checkUserId == true) //验证用户名是否存在
            {
                if (u.Where(u.User_Id == user.User_Id).Exists())
                    return 0;
            }

            u.Insert(true, tran);

            return u.INSERTED_ID;
        }

        /// <summary>
        /// 移除User,同时移除User 与其它对象的关联
        /// </summary>
        public bool RemoveUser(params int[] puidS) { return this.RemoveUser(null, puidS); }
        public bool RemoveUser(QDbTran tran, params int[] puidS)
        {
            if (puidS == null || puidS.Length < 1)
                throw new BCFException("参数异常:(puidS == null || puidS.Length < 1)");

            //删除User
            //
            UserM u = new UserM();

            u.Where(u.PUID.In(puidS)).Delete(tran);

            //删除User关联信息
            //
            User_LinkedM ul = new User_LinkedM();

            ul.Where(ul.PUID.In(puidS)).Delete(tran);

            //删除User与Resource
            //
            ResourceService.G.RemoveLink(tran, puidS);

            return true;

        }


        /// <summary>
        /// 删除组与用户的关联
        /// </summary>
        public bool RemoveLink(params int[] pgidS)
        {
            return this.RemoveLink(null, pgidS);
        }

        /// <summary>
        /// 删除组与用户的关联
        /// </summary>
        public bool RemoveLink(QDbTran tran, params int[] pgidS)
        {
            User_LinkedM ul = new User_LinkedM();

            return ul.Where(ul.LK_OBJT == Group.LK_OBJT)
                     .And(ul.LK_OBJT_ID.In(pgidS))
                     .Delete(tran);
        }

        public bool RemoveLink(QView lkidView, QDbTran tran)
        {
            User_LinkedM ul = new User_LinkedM();

            return ul.Where(ul.LK_OBJT == Group.LK_OBJT)
                     .And(ul.LK_OBJT_ID.In(lkidView))
                     .Delete(tran);
        }

        /// <summary>
        /// 修改用户必码
        /// </summary>
        /// <param name="userId">用户ID</param>
        /// <param name="passWd">未加密的代码</param>
        public bool ModifyUserPassWd(string userId, string passWd)
        {
            UserM u = new UserM();


            u.Pass_Wd.Value = User.EncryptPassWd(userId, passWd);

            if (string.IsNullOrEmpty(u.Pass_Wd.Value))
                return false;

            return u.Where(u.User_Id == userId).Update();
        }

        /// <summary>
        /// 修改时,已实现自动[Pass_Wd]加密
        /// </summary>
        public bool ModifyUser(User user, QDbTran tran)
        {
            UserM u = new UserM();

            u.Pass_Wd.IsInc = user.EncryptPassWd();

            u.Bind(user);

            u.PUID.IsInc = false;
            u.Create_Time.IsInc = false;
            u.Last_Update.Value = DateTime.Now;

            if (string.IsNullOrEmpty(u.Pass_Wd.Value))
                u.Pass_Wd.IsInc = false;

            return u.Where(u.PUID == user.PUID).Update(tran);
        }

        public bool DisableUser(int puid, bool isDisabled)
        {
            if (puid < 1)
                return false;


            UserM u = new UserM();

            u.Is_Disabled.Value = isDisabled;

            return u.Where(u.PUID == puid).Update();
        }

        public bool VisibleUser(int puid, bool isVisibled)
        {
            if (puid < 1)
                return false;

            UserM u = new UserM();

            u.Is_Visibled.Value = isVisibled;

            return u.Where(u.PUID == puid).Update();
        }

        public bool ClearUsers()
        {
            string[] unClearPUIDs = new string[] { "0", "1" };

            //清理,与功能间的关联
            //		
            Resource_LinkedM rl = new Resource_LinkedM();
            rl.Where(rl.LK_OBJT == User.LK_OBJT)
              .And(rl.LK_OBJT_ID.Unin(unClearPUIDs))
              .Delete();
            
            //清理,与组之音的关联
            //
            User_LinkedM ul = new User_LinkedM();
            ul.Where(ul.PUID.Unin(unClearPUIDs)).Delete();
            
            //清理用户数据
            //
            UserM u = new UserM();
            u.Where(u.PUID.Unin(unClearPUIDs)).Delete();

            return true;
        }

        /// <summary>
        /// 是否存在一个用户
        /// </summary>
        public bool Exists(int orPuid, string orUser_id)
        {
            if (orUser_id == null)
                orUser_id = "";

            if (orUser_id.Length == 0 && orPuid < 1)
                return false;

            UserM u = new UserM();

            return u.Where(u.PUID == orPuid)
                      .Or(u.User_Id == orUser_id)
                    .Exists();
        }

        /// <summary>
        /// 获取一个User[@orPuid 或者 @orUser_id]对象
        /// </summary>
        public User GetUser(int orPuid, string orUser_id)
        {

            if (orPuid == 0 && orUser_id == null)
                throw new BCFException("PUID 与 User_Id不能同时为NULL");


            UserM u = new UserM();

            return u.Where(u.PUID == orPuid)
                      .Or(u.User_Id == orUser_id)
                    .SelectOne<User>();
        }


        //-----------------------------------------------------------

        #region GetUsers

        public List<UserEx> GetInGroupUsers(params int[] pgids)
        {
            return GetInGroupUsers(false, false, pgids);
        }

        public List<UserEx> GetInGroupUsers(bool incDisabled, bool incUnVisible, params int[] pgids)
        {
            return GetUsersQuery(incDisabled, incUnVisible, pgids).Select<UserEx>();
        }

        public List<UserEx> GetInGroupUsers(string pgCode, bool incDisabled, bool incUnVisible)
        {
            return GetUsersQuery(pgCode, incDisabled, incUnVisible).Select<UserEx>();
        }

        public List<User> GetUsers(params int[] puids)
        {
            UserM u = new UserM();

            return u.Where(u.PUID.In(puids)).Select<User>();
        }

        public List<User> GetUsers()
        {
            return (new UserM()).Select<User>();
        }

        #endregion

        #region GetUsersCount

        public int GetCount(params int[] pgids)
        {
            return GetCount(false, false, pgids);
        }

        public int GetCount(bool incDisabled, bool incUnVisible, params int[] pgids)
        {
            return GetUsersQuery(incDisabled, incUnVisible, pgids).Count();
        }

        private SQuery GetUsersQuery(bool incDisabled, bool incUnVisible, params int[] pgids)
        {
            UserM u = new UserM();
            User_LinkedM ul = new User_LinkedM();

            ul.IncludeAll(false);
            ul.LK_OBJT_ID.IsInc = true;
            ul.LK_OBJT_ID.AsName = "PGID";

            SQuery SQ = NewSQ();

            SQ.From(u)
              .InnerJoin(ul)
                .On(u.PUID == ul.PUID)
              .Where(ul.LK_OBJT_ID.In(pgids))
                .And(ul.LK_OBJT == Group.LK_OBJT);

            if (incDisabled == false)
                SQ.And(u.Is_Disabled != true);

            if (incUnVisible == false)
                SQ.And(u.Is_Visibled != false);

            SQ.OrderBy(u.CN_Name);

            return SQ;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pgCode">代码模式：3301|33%|33__01%</param>
        /// <param name="incDisabled"></param>
        /// <param name="incUnVisible"></param>
        /// <returns></returns>
        private SQuery GetUsersQuery(string pgCode, bool incDisabled, bool incUnVisible)
        {
            UserM u = new UserM();
            User_LinkedM ul = new User_LinkedM();
            GroupM g = new GroupM();

            ul.IncludeAll(false);
            ul.LK_OBJT_ID.IsInc = true;
            ul.LK_OBJT_ID.AsName = "PGID";

            SQuery SQ = NewSQ();

            SQ.From(u)
              .InnerJoin(ul)
                .On(u.PUID == ul.PUID).And(ul.LK_OBJT == Group.LK_OBJT)
              .InnerJoin(g)
                .On(ul.LK_OBJT_ID == g.PGID)
              .Where(g.PG_Code.Like(pgCode));

            if (incDisabled == false)
                SQ.And(u.Is_Disabled != true);

            if (incUnVisible == false)
                SQ.And(u.Is_Visibled != false);

            return SQ;
        }

        #endregion
    }
}
