
using System;


using Noear.Weed;

namespace Weed.BCF
{
    /// <summary>
    /// 资源关联...
    /// </summary>
    [Serializable]
    public class Resource_Linked : QEntity
    {
        /// <summary>
        /// 资源RSID
        /// </summary>
        public int RSID { get; set; }
        
        /// <summary>
        /// LK_OBJT
        /// </summary>
        public int LK_OBJT { get; set; }

        /// <summary>
        /// 关联的对象ID
        /// </summary>
        public int LK_OBJT_ID { get; set; }

        /// <summary>
        /// 关联操作符
        /// </summary>
        public string LK_Operate { get; set; }

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
            //1.get:获取数据的方法指针；src：当前提供get的源类型[DataBase|Entity|Collection]
            //
            RSID = Eval<int>(get("RSID"));
            LK_OBJT = Eval<int>(get("LK_OBJT"));
            LK_OBJT_ID = Eval<int>(get("LK_OBJT_ID"));
            LK_Operate = Eval<string>(get("LK_Operate"));
            P_Express = Eval<PExpress>(get("P_Express"));
        }
        
        public override IQBinder Clone()
        {
            return new Resource_Linked();
        }
    }
}
    