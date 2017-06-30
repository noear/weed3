using System;
using System.Collections.Generic;

using System.Text;

namespace Weed.BCF.Web
{
    /// <summary>
    /// 用于在用户登陆系统之后的数据存储单位
    /// </summary>
    public class GroupInfor
    {
        /// <summary>
        /// 构造函数
        /// </summary>
        /// <param name="define">组定义</param>
        public GroupInfor(int define)
        {
            this.type = define.ToString();
        }

        private string type;
        /// <summary>
        /// 组PGID
        /// </summary>
        public int PGID
        {
            get { return Convert.ToInt32(WebClient.Session["BCF_G" + type + "_PGID"]); }
            set { WebClient.Session["BCF_G" + type + "_PGID"] = value; }
        }

        /// <summary>
        /// 组父级PGID
        /// </summary>
        public int P_PGID
        {
            get { return Convert.ToInt32(WebClient.Session["BCF_G" + type + "_P_PGID"]); }
            set { WebClient.Session["BCF_G" + type + "_P_PGID"] = value; }
        }

        /// <summary>
        /// 组自定义Code
        /// </summary>
        public string PG_Code
        {
            get { return WebClient.Session["BCF_G" + type + "_PG_Code"].ToString(); }
            set { WebClient.Session["BCF_G" + type + "_PG_Code"] = value; }
        }

        /// <summary>
        /// 组中文名称
        /// </summary>
        public string CN_Name
        {
            get { return WebClient.Session["BCF_G" + type + "_CN_Name"].ToString(); }
            set { WebClient.Session["BCF_G" + type + "_CN_Name"] = value; }
        }

        /// <summary>
        /// 组英文名称
        /// </summary>
        public string EN_Name
        {
            get { return WebClient.Session["BCF_G" + type + "_EN_Name"].ToString(); }
            set { WebClient.Session["BCF_G" + type + "_EN_Name"] = value; }
        }

        /// <summary>
        /// 级别
        /// </summary>
        public int In_Level
        {
            get { return Convert.ToInt32(WebClient.Session["BCF_G" + type + "_In_Level"]); }
            set { WebClient.Session["BCF_G" + type + "_In_Level"] = value; }
        }

        /// <summary>
        /// 标签
        /// </summary>
        public string Tags
        {
            get { return WebClient.Session["BCF_" + type + "_Tags"].ToString(); }
            set { WebClient.Session["BCF_" + type + "_Tags"] = value; }
        }

        /// <summary>
        /// 绑定
        /// </summary>
        /// <param name="group"></param>
        /// <returns></returns>
        public bool Bind(Group group)
        {
            if (group.PGID == null)
                return false;

            this.PGID = group.PGID;
            this.P_PGID = group.P_PGID;
            this.CN_Name = group.CN_Name;
            this.EN_Name = group.EN_Name;
            this.PG_Code = group.PG_Code;
            this.In_Level = group.In_Level;
            this.Tags = group.Tags;

            return true;
        }
    }
}
