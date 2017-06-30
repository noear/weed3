using System;
using System.Collections.Generic;

using System.Text;

namespace Weed.BCF
{
    /// <summary>
    /// BCF异常
    /// </summary>
    public class BCFException : WeedException
    {
        /// <summary>
        /// 构造函数
        /// </summary>
        /// <param name="message">异常消息</param>
        public BCFException(string message) : base(message)
        {

        }
    }
}
