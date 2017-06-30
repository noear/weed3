using System;
using System.Collections.Generic;

using System.Text;
using System.Web;
using System.Web.SessionState;
using System.Web.UI;

using Weed.SDQ;
using Weed.BCF.Web;

namespace Weed.BCF
{
    /// <summary>
    /// WebClient
    /// </summary>
    public class WebClient : BCF_PARAMS
    {
        #region 基础部份

        /// <summary>
        /// 静态实例
        /// </summary>
        public static WebClient G = new WebClient();

        #endregion

        #region Login
        /// <summary>
        /// 登录
        /// </summary>
        /// <param name="userId">用户ID</param>
        /// <param name="passWd">密码</param>
        /// <returns>是否成功</returns>
        public bool Login(string userId, string passWd)
        {
            return Login(userId, passWd, 0);
        }

        /// <summary>
        /// 登录
        /// </summary>
        /// <param name="userId">用户ID</param>
        /// <param name="passWd">密码</param>
        /// <param name="groupId">限定的组ID</param>
        /// <returns>是否成功</returns>
        public bool Login(string userId, string passWd, int groupId)
        {
            if (userId == null || passWd == null)
                throw new BCFException("!!!(userId == null || passWd == null)");

            if (userId.Length < 1 || passWd.Length < 1)
                throw new BCFException("!!!(userId.Length < 1 || passWd.Length < 1)");

            string secretPassWd = User.EncryptPassWd(userId, passWd);

            //-------------------------------
            UserM u = new UserM();
            

            //-------------------------------------
            SQuery SQ = NewSQ();

            SQ.From(u);

            if (groupId > 0)
            {
                User_LinkedM ul = new User_LinkedM();

                ul.IncludeAll(false);

                SQ.InnerJoin(ul)
                  .On(ul.PUID == u.PUID)
                  .And(ul.LK_OBJT == Group.LK_OBJT)
                  .And(ul.LK_OBJT_ID == groupId);
            }

            SQ.Where(u.User_Id == userId)
              .And(u.Pass_Wd == secretPassWd)
              .And(u.Is_Disabled == false);

            User user = SQ.SelectOne<User>();

            if (user.User_Id == null)
                return false;
            else
            {
                if (_CurrentUser.Bind(user) == false)
                    return false;

                if (groupId>0)
                {
                    if (_CurrentGroup.Bind(GroupService.G.GetGroup(groupId)) == false)
                        return false;
                }

                //------------------------->>成功登录后,执行插件调用
                //				PluginApply.OnLogin();
                //-------------------------<<

                return true;
            }
        }

        /// <summary>
        /// 核对密码
        /// </summary>
        public static bool CheckPassWd(string userId, string passWd)
        {
            string secretPassWd = User.EncryptPassWd(userId, passWd);

            UserM u = new UserM();
            
            u.Where(u.User_Id == userId)
             .And(u.Pass_Wd == secretPassWd);

            return u.Exists();
        }

        public static bool IsLogin()
        {
            return Session["BCF_USER_PUID"] != null;
        }

        #endregion

        #region Current Info

        private static GroupInfor _CurrentGroup = new GroupInfor(Dept.DEFINE);
        /// <summary>
        /// 当前用户组信息
        /// 
        /// 需要在登录时,输入groupId
        /// </summary>
        public static GroupInfor CurrentGroup
        {
            get
            {
                return _CurrentGroup;
            }
        }

        private static UserInfor _CurrentUser = new UserInfor();
        /// <summary>
        /// 当前用户
        /// </summary>
        public static UserInfor CurrentUser
        {
            get
            {
                return _CurrentUser;
            }
        }

        /// <summary>
        /// Session
        /// </summary>
        public static HttpSessionState Session
        {
            get
            {
                return HttpContext.Current.Session;
            }
        }

        /// <summary>
        /// Request
        /// </summary>
        public static HttpRequest Request
        {
            get
            {
                return HttpContext.Current.Request;
            }
        }

        /// <summary>
        /// IP
        /// </summary>
        public static string IP
        {
            get
            {
                return Request.UserHostAddress;
            }
        }

        /// <summary>
        /// 当前Page
        /// </summary>
        public static Page CurrentPage
        {
            get
            {
                Page temp = HttpContext.Current.Handler as Page;

                if (temp == null)
                    throw new BCFException("当前上下文代理不是Page类型!");
                else
                    return temp;
            }
        }
        private static string _AppPath;
        /// <summary>
        /// 应用程序路径
        /// </summary>
        public static string AppPath
        {
            get
            {
                if (_AppPath == null)
                {
                    _AppPath = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;

                    if (_AppPath.Length > 0)
                    {
                        char ch1 = _AppPath[_AppPath.Length - 1];
                        if ((ch1 != '/') && (ch1 != '\\'))
                        {
                            _AppPath = _AppPath + '/';
                        }
                    }
                }

                return _AppPath;
            }
        }

        #endregion
    }
}
