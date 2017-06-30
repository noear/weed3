using System;
using System.Collections.Generic;

using System.Text;

namespace Weed.BCF.Web
{
    /// <summary>
    /// 用户信息
    /// </summary>
    public class UserInfor : BCF_PARAMS
    {
        #region 属性
        /// <summary>
        /// 用户PUID
        /// </summary>
        public int PUID
        {
            get { return Convert.ToInt32(WebClient.Session["BCF_USER_PUID"]); }
            set { WebClient.Session["BCF_USER_PUID"] = value; }
        }
        /// <summary>
        /// 用户ID
        /// </summary>
        public string User_Id
        {
            get { return WebClient.Session["BCF_USER_USERID"].ToString(); }
            set { WebClient.Session["BCF_USER_USERID"] = value; }
        }
        public string CN_Name
        {
            get { return WebClient.Session["BCF_USER_CNNAME"].ToString(); }
            set { WebClient.Session["BCF_USER_CNNAME"] = value; }
        }
        public string EN_Name
        {
            get { return WebClient.Session["BCF_USER_ENNAME"].ToString(); }
            set { WebClient.Session["BCF_USER_ENNAME"] = value; }
        }
        public string PW_Mail
        {
            get { return WebClient.Session["BCF_USER_PWMAIL"].ToString(); }
            set { WebClient.Session["BCF_USER_PWMAIL"] = value; }
        }

        public string Tags
        {
            get { return WebClient.Session["BCF_USER_TAGS"].ToString(); }
            set { WebClient.Session["BCF_USER_TAGS"] = value; }
        }

        public int OUT_OBJT
        {
            get { return Convert.ToInt32(WebClient.Session["BCF_USER_OUT_OBJT"]); }
            set { WebClient.Session["BCF_USER_OUT_OBJT"] = value; }
        }

        public int OUT_OBJT_ID
        {
            get { return Convert.ToInt32(WebClient.Session["BCF_USER_OUT_OBJT_ID"]); }
            set { WebClient.Session["BCF_USER_OUT_OBJT_ID"] = value; }
        }

        #endregion

        public bool Bind(User user)
        {
            if (user.User_Id == null)
                return false;

            this.PUID = user.PUID;
            this.User_Id = user.User_Id;
            this.CN_Name = user.CN_Name;
            this.EN_Name = user.EN_Name;
            this.PW_Mail = user.PW_Mail;
            this.Tags = user.Tags;
            this.OUT_OBJT = user.OUT_OBJT;
            this.OUT_OBJT_ID = user.OUT_OBJT_ID;

            return true;
        }

        public bool HasResources(params string[] packageIds)
        {
            return false;
        }

        /// <summary>
        /// 关于[模块=@rsid]的权限表达式
        /// </summary>
        //public PExpress OfResource(string rsid)
        //{
        //    return Resource.ExpressOfUser(rsid, this.PUID);
        //}

        public List<ResourceEx> MyResources(params int[] packageIds)
        {
            return ResourceService.G.GetInUserResources(this.PUID, packageIds);
        }
    }
}
