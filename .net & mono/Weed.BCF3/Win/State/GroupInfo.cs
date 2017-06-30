using System;
using System.Collections.Generic;

using System.Text;

namespace Weed.BCF.Win
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
        public int PGID;

        /// <summary>
        /// 组父级PGID
        /// </summary>
        public int P_PGID;

        /// <summary>
        /// 组自定义Code
        /// </summary>
        public string PG_Code;

        /// <summary>
        /// 组中文名称
        /// </summary>
        public string CN_Name;

        /// <summary>
        /// 组英文名称
        /// </summary>
        public string EN_Name;

        /// <summary>
        /// 级别
        /// </summary>
        public int In_Level;

        /// <summary>
        /// 标签
        /// </summary>
        public string Tags;

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
