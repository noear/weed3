using System;
using System.Collections.Generic;
using System.Text;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 支持"层级"关系的组
    /// </summary>
    [Serializable]
    public class Unit : Group
    {
        private static int _DEFINE;
        /// <summary>
        /// 在组里的定义[R_PGID]
        /// </summary>
        public static int DEFINE
        {
            get
            {
                if (_DEFINE == 0)
                    _DEFINE = ConfigService.Get_GROUP_DEFINE("BCF_Unit");

                return _DEFINE;
            }
        }

        public override IQBinder Clone()
        {
            return new Unit();
        }
    }
}
