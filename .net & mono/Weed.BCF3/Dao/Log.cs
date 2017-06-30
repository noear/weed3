using System;
using System.Collections.Generic;
using System.Text;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 日志实体
    /// </summary>
    public class Log : BCF_Log
    {
        private static int _LK_OBJT;
        /// <summary>
        /// LK_OBJT
        /// </summary>
        public static int LK_OBJT
        {
            get
            {
                if (_LK_OBJT == 0)
                    _LK_OBJT = ConfigService.Get_LK_OBJT(LogM.G.TableName);

                return _LK_OBJT;
            }
        }

        /// <summary>
        /// 生成一个新的ID
        /// </summary>
        /// <returns></returns>
        //public static string NewID()
        //{
        //    return Guid.NewGuid().ToString("N");
        //}

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public override IQBinder Clone()
        {
            return new Log();
        }
    }
}
