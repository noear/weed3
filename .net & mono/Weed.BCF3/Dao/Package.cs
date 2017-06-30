using System;
using System.Collections.Generic;
using System.Text;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 资源包料体
    /// </summary>
    public class Package : Group
    {
        private static int _DEFINE = -1;
        /// <summary>
        /// 在Group里的定义值[R_PGID]
        /// </summary>
        public static int DEFINE
        {
            get
            {
                if (_DEFINE < 0)
                    _DEFINE = ConfigService.Get_GROUP_DEFINE("BCF_Package");

                return _DEFINE;
            }
        }

        public override IQBinder Clone()
        {
            return new Package();
        }
    }
}
