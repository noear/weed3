
using System;


using Noear.Weed;

namespace Weed.BCF
{
    /// <summary>
    /// 生成:2008/06/04 08:34:07
    /// 作者:自动生成
    ///	
    /// 备注:请不要轻易手动更改类的实现代码
    /// </summary>
    [Serializable]
    public class BCF_User : QEntity
    {
        /// <summary>
        /// 用户PUID
        /// </summary>
        public int PUID { get; set; }
        /// <summary>
        /// 用户ID
        /// </summary>
        public string User_Id { get; set; }
        /// <summary>
        /// 密码（加密后的）
        /// </summary>
        public string Pass_Wd { get; set; }
        /// <summary>
        /// 中文名称
        /// </summary>
        public string CN_Name { get; set; }
        /// <summary>
        /// 英文名称
        /// </summary>
        public string EN_Name { get; set; }
        /// <summary>
        /// 密码邮箱
        /// </summary>
        public string PW_Mail { get; set; }

        /// <summary>
        /// 外部关系对象
        /// </summary>
        public int OUT_OBJT { get; set; }

        /// <summary>
        /// 外部关系对象ID
        /// </summary>
        public int OUT_OBJT_ID { get; set; }
        /// <summary>
        /// 标签
        /// </summary>
        public string Tags { get; set; }
        /// <summary>
        /// 是否被禁用了(不可登录)
        /// </summary>
        public bool Is_Disabled { get; set; }
        /// <summary>
        /// 是否可见（在管理时不可见）
        /// </summary>
        public bool Is_Visibled { get; set; }
        /// <summary>
        /// 创建时间
        /// </summary>
        public DateTime Create_Time { get; set; }
        /// <summary>
        /// 最后更新时间
        /// </summary>
        public DateTime Last_Update { get; set; }
    
        /// <summary>
        /// 绑定委托数据
        /// </summary>
        /// <param name="get">委托数据源</param>
        /// <param name="src">源类型</param>
        public override void Bind(GetHandler get, SourceType src)
        {
            //1.get:获取数据的方法指针；src：当前提供get的源类型[DataBase|Entity|Collection]
            //
            PUID = Eval<int>(get("PUID"));
            User_Id = Eval<string>(get("User_Id"));
            Pass_Wd = Eval<string>(get("Pass_Wd"));
            CN_Name = Eval<string>(get("CN_Name"));
            EN_Name = Eval<string>(get("EN_Name"));
            PW_Mail = Eval<string>(get("PW_Mail"));
            OUT_OBJT = Eval<int>(get("OUT_OBJT"));
            OUT_OBJT_ID = Eval<int>(get("OUT_OBJT_ID"));
            Tags = Eval<string>(get("Tags"));
            Is_Disabled = Tran<bool>(get("Is_Disabled"));
            Is_Visibled = Tran<bool>(get("Is_Visibled"));
            Create_Time = Eval<DateTime>(get("Create_Time"));
            Last_Update = Eval<DateTime>(get("Last_Update"));
        }
        
        public override IQBinder Clone()
        {
            return new BCF_User();
        }
    }
}
    