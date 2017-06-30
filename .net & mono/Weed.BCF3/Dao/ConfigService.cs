using System;
using System.Collections.Generic;

using System.Text;

namespace Weed.BCF
{
    /// <summary>
    /// 配置服务
    /// </summary>
    public class ConfigService : BCF_PARAMS
    {
        /// <summary>
        /// 获取数据表的链接对象定义(数据表简写)
        /// </summary>
        internal static int Get_LK_OBJT(string tableName)
        {
            return int.Parse(GetValue(tableName + ".LK_OBJT"));
        }

        /// <summary>
        /// 获取虚拟对象的定义
        /// </summary>
        internal static int Get_GROUP_DEFINE(string defineObj)
        {
            string key = "DEFINE." + defineObj + "=" + Group.LK_OBJT + "." + Group.R_PGID_NAME;

            int temp = GetValue<int>(key, -1);

            if (temp <= 0)
                throw new BCFException("BCF配置[" + key + "]项未定义");
            else
                return temp;
        }

        /// <summary>
        /// 设置虚拟对象的定义
        /// </summary>
        internal static void Set_DEFINE(string defineObj, int value)
        {
            string key = "DEFINE." + defineObj + "=" + Group.LK_OBJT + "." + Group.R_PGID_NAME;

            SetValue(key, value.ToString());
        }


        #region 配置表 GET,SET

        public static string GetValue(string name)
        {
            return GetValue<string>(name, "");
        }

        public static T GetValue<T>(string name, T defT)
        {
            ConfigM cfg = new ConfigM();

            return cfg.Where(cfg.Name == name)
                      .SelectValue<T>(cfg.Value, defT);
        }

        public static void SetValue(string name, string value)
        {
            ConfigM cfg = new ConfigM();

            cfg.Value.Value = value;

            if (cfg.Where(cfg.Name == name).Update() == false)
            {                
                cfg.Name.Value  = name;
                cfg.Value.Value = value;
                cfg.Note.Value = "";

                cfg.Insert();
            }
        }

        public static void SetValues(string nameLike, string value)
        {
            ConfigM cfg = new ConfigM();

            cfg.Value.Value = value;

            cfg.Where(cfg.Name.Like(nameLike)).Update();
        }

        #endregion
    }
}
