using System;
using System.Collections.Generic;

using System.Text;

namespace Weed.BCF.Win
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
        public int PUID;

        /// <summary>
        /// 用户ID
        /// </summary>
        public string User_Id;

        /// <summary>
        /// 用户中文名称
        /// </summary>
        public string CN_Name;

        /// <summary>
        /// 用户英文名称
        /// </summary>
        public string EN_Name;

        /// <summary>
        /// 密码邮箱
        /// </summary>
        public string PW_Mail;

        /// <summary>
        /// 标签
        /// </summary>
        public string Tags;

        /// <summary>
        /// 
        /// </summary>
        public int OUT_OBJT;

        /// <summary>
        /// 
        /// </summary>
        public int OUT_OBJT_ID;

        #endregion

        /// <summary>
        /// 绑定
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
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

        /// <summary>
        /// 
        /// </summary>
        /// <param name="packageIds"></param>
        /// <returns></returns>
        public bool HasResources(params int[] packageIds)
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
