using System;
using System.Collections.Generic;
using System.Text;

namespace Weed.BCF
{
    /// <summary>
    /// OPSx 信息扩展接口(OPS内部对象在BCF_OPSx表里的扩展信息)
    /// </summary>
    public interface IOPSxExtend
    {
        /// <summary>
        /// 获取OPSx扩展信息
        /// </summary>
        /// <returns></returns>
        string OPSx();

        /// <summary>
        /// 设置OPSx扩展信息
        /// </summary>
        /// <param name="opsXml">ops-xml</param>
        void OPSx(string opsXml);
    }
}
