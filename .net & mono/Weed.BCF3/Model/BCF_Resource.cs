
using System;

using Noear.Weed;

namespace Weed.BCF
{
    /// <summary>
    /// 资源扩展信息
    /// </summary>
    [Serializable]
    public class ResourceEx : BCF_Resource, IOPSxExtend
    {
        /// <summary>
        /// 所属资源包的PGID
        /// </summary>
        public int PGID { get; set; }

        /// <summary>
        /// 所属资源包的中文名称
        /// </summary>
        public string PG_CN_Name { get; set; }

        /// <summary>
        /// 所属资源包的英文名称
        /// </summary>
        public string PG_EN_Name { get; set; }

        /// <summary>
        /// 所属资源包的排序
        /// </summary>
        public int PG_Order_Index { get; set; }

        /// <summary>
        /// 所属资源包的关系操作符
        /// </summary>
        public string LK_Operate { get; set; }

        /// <summary>
        /// LK_OBJT
        /// </summary>
        public int LK_OBJT { get; set; }
        
        /// <summary>
        /// 权值表达式
        /// </summary>
        public PExpress P_Express { get; set; }

        /// <summary>
        /// 绑定委托数据
        /// </summary>
        /// <param name="get">委托数据源</param>
        /// <param name="src">源类型</param>
        public override void Bind(GetHandler get, SourceType src)
        {
            PGID = Eval<int>(get("PGID"));
            PG_CN_Name = Eval<string>(get("PG_CN_Name"));
            PG_EN_Name = Eval<string>(get("PG_EN_Name"));
            PG_Order_Index = Eval<int>(get("PG_Order_Index"));

            LK_Operate = Eval<string>(get("LK_Operate"));
            LK_OBJT = Eval<int>(get("LK_OBJT"));
            P_Express = Eval<PExpress>(get("P_Express"));

            base.Bind(get, src);
        }

        public override IQBinder Clone()
        {
            return new ResourceEx();
        }

        #region IOPSxExtend 成员

        /// <summary>
        /// 获取OPSx扩展信息
        /// </summary>
        /// <returns></returns>
        public string OPSx()
        {
            return OPSxService.G.GetOPSx(Resource.LK_OBJT, RSID);
        }

        /// <summary>
        /// 设置OPSx扩展信息
        /// </summary>
        /// <param name="opsXml">ops-xml</param>
        public void OPSx(string opsXml)
        {
            OPSxService.G.SetOPSx(Resource.LK_OBJT, RSID, opsXml);
        }

        #endregion
    }

    /// <summary>
    /// 生成:2008/06/04 08:34:07
    /// 作者:自动生成
    ///	
    /// 备注:请不要轻易手动更改类的实现代码
    /// </summary>
    [Serializable]
    public class BCF_Resource : QEntity
    {
        /// <summary>
        /// 资源RSID
        /// </summary>
        public int RSID { get; set; }

        /// <summary>
        /// 资源自定义Code
        /// </summary>
        public string RS_Code { get; set; }

        /// <summary>
        /// 资源中文名称
        /// </summary>
        public string CN_Name { get; set; }

        /// <summary>
        /// 资源英文名称
        /// </summary>
        public string EN_Name { get; set; }

                
        /// <summary>
        /// URI 路径
        /// </summary>
        public string Uri_Path { get; set; }

        /// <summary>
        /// URI 打开的目标框架名称
        /// </summary>
        public string Uri_Target { get; set; }

        /// <summary>
        /// 图标 路径
        /// </summary>
        public string Ico_Path { get; set; }

        /// <summary>
        /// 排序
        /// </summary>
        public int Order_Index { get; set; }

        /// <summary>
        /// 级别
        /// </summary>
        public int In_Level { get; set; }

        /// <summary>
        /// 说明
        /// </summary>
        public string Note { get; set; }

        /// <summary>
        /// 标签
        /// </summary>
        public string Tags { get; set; }

        /// <summary>
        /// 是否禁用（不可使用）
        /// </summary>
        public bool Is_Disabled { get; set; }

        /// <summary>
        /// 是否可见（不出现在菜单范围里）
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
            RSID = Eval<int>(get("RSID"));            
            RS_Code = Eval<string>(get("RS_Code"));
            CN_Name = Eval<string>(get("CN_Name"));
            EN_Name = Eval<string>(get("EN_Name"));
            Uri_Path = Eval<string>(get("Uri_Path"));
            Uri_Target = Eval<string>(get("Uri_Target"));
            Ico_Path = Eval<string>(get("Ico_Path"));
            Order_Index = Eval<int>(get("Order_Index"));
            In_Level = Eval<int>(get("In_Level"));
            Note = Eval<string>(get("Note"));
            Tags = Eval<string>(get("Tags"));
            Is_Disabled = Tran<bool>(get("Is_Disabled"));
            Is_Visibled = Tran<bool>(get("Is_Visibled"));
            Create_Time = Eval<DateTime>(get("Create_Time"));
            Last_Update = Eval<DateTime>(get("Last_Update"));
        }
        
        public override IQBinder Clone()
        {
            return new BCF_Resource();
        }
    }
}
    