
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
    public class BCF_Group : QEntity
    {
        /// <summary>
        /// 组PGID
        /// </summary>
        public int PGID { get; set; }
        /// <summary>
        /// 组父级PGID
        /// </summary>
        public int P_PGID { get; set; }
        /// <summary>
        /// 组根级PGID
        /// </summary>
        public int R_PGID { get; set; }
        /// <summary>
        /// 组自定义Code
        /// </summary>
        public string PG_Code { get; set; }
        /// <summary>
        /// 组中文名称
        /// </summary>
        public string CN_Name { get; set; }
        /// <summary>
        /// 组英文名称
        /// </summary>
        public string EN_Name { get; set; }
        /// <summary>
        /// 组级别
        /// </summary>
        public int In_Level { get; set; }
        /// <summary>
        /// 组标签
        /// </summary>
        public string Tags { get; set; }
        /// <summary>
        /// 组排序
        /// </summary>
        public int Order_Index { get; set; }
        /// <summary>
        /// 是否禁用(不可用)
        /// </summary>
        public bool Is_Disabled { get; set; }
        /// <summary>
        /// 是否可见（管理是可见）
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
    
        public override void Bind(GetHandler get, SourceType src)
        {
            //1.get:获取数据的方法指针；src：当前提供get的源类型[DataBase|Entity|Collection]
            //
            PGID = Eval<int>(get("PGID"));
            P_PGID = Eval<int>(get("P_PGID"));
            R_PGID = Eval<int>(get("R_PGID"));
            PG_Code = Eval<string>(get("PG_Code"));
            CN_Name = Eval<string>(get("CN_Name"));
            EN_Name = Eval<string>(get("EN_Name"));
            In_Level = Eval<int>(get("In_Level"));
            Tags = Eval<string>(get("Tags"));
            Order_Index = Eval<int>(get("Order_Index"));
            Is_Disabled = Tran<bool>(get("Is_Disabled"));
            Is_Visibled = Tran<bool>(get("Is_Visibled"));
            Create_Time = Eval<DateTime>(get("Create_Time"));
            Last_Update = Eval<DateTime>(get("Last_Update"));
        }
        
        public override IQBinder Clone()
        {
            return new BCF_Group();
        }
    }
}
    