
using System;

using Noear.Weed;

namespace Weed.BCF
{
    /// <summary>
    /// 用户关联...
    /// </summary>
    [Serializable]
    public class User_Linked : IBinder
    {
        /// <summary>
        /// 用户PUID
        /// </summary>
        public int PUID { get; set; }

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
    
        public override void Bind(GetHandler get, SourceType src)
        {
            //1.get:获取数据的方法指针；src：当前提供get的源类型[DataBase|Entity|Collection]
            //
            PUID = Eval<int>(get("PUID"));
            LK_OBJT = Eval<int>(get("LK_OBJT"));
            LK_OBJT_ID = Eval<int>(get("LK_OBJT_ID"));
            LK_Operate = Eval<string>(get("LK_Operate"));
        }
        
        public override IQBinder Clone()
        {
            return new User_Linked();
        }
    }
}
    