using System;
using System.Collections.Generic;

using System.Text;
using System.Security.Cryptography;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// BCF 参数
    /// </summary>
    [Serializable]
    public class BCF_PARAMS
    {
        private static string _DEFAULT_SDQ_NAME;
        /// <summary>
        /// 默认SDQ配置节名称
        /// </summary>
        public static string DEFAULT_SDQ_NAME
        {
            set
            {
                _DEFAULT_SDQ_NAME = value;
            }
            get
            {
                return _DEFAULT_SDQ_NAME;
            }
        }

        /// <summary>
        /// 实例化一个SQuery
        /// </summary>
        /// <returns></returns>
        public static SQuery NewSQ()
        {
            return new SQuery(DEFAULT_SDQ_NAME);
        }

        /// <summary>
        /// 实例化一个事务对象
        /// </summary>
        /// <returns></returns>
        public static QDbTran NewTran()
        {
            return NewSQ().SQL.BeginTran();
        }

        //以变量形式存在,具有一定的冲突风险
        //
        //private  SQuery _SQ;
        ///// <summary>
        ///// 默认的 SQuery
        ///// </summary>
        //public  SQuery SQ
        //{
        //    get 
        //    {
        //        if (_SQ == null)
        //            _SQ = NewSQ();

        //        return _SQ;
        //    }
        //}

        /// <summary>
        /// 创建一个ID
        /// </summary>
        /// <returns></returns>
        //public static string NewID()
        //{
        //    return Guid.NewGuid().ToString("N");
        //}

        /// <summary>
        /// 是否为空或Null字符
        /// </summary>
        /// <param name="value"></param>
        /// <returns></returns>
        //public static bool IsNullOrEmpty(string value)
        //{
        //    return string.IsNullOrEmpty(value);
        //}

        /// <summary>
        /// 使用SHA1加密
        /// </summary>
        /// <param name="clearText">文本</param>
        /// <returns></returns>
        public static string ToHSA1(string clearText)
        {
            return Hash.ToSHA1(clearText);
        }

        /// <summary>
        /// 初始化BCF框架（危险动作，删除数据库里相关的表并重建）
        /// </summary>
        public static void InitializeFrame()
        {
            SQuery sq = NewSQ();

            if (sq.AcType == AccessType.Mysql)
                sq.SQL.ExeNonQuery(XmlResource.BCF_MySQL_Build, null);
            else
                sq.SQL.ExeNonQuery(XmlResource.BCF_Mssql_Build, null);
        }

        /// <summary>
        /// 初始化BCF框架数据（危险动作，清空数据库里相关的表并插入初始化数据）
        /// </summary>
        public static void InitializeData()
        {
            SQuery sq = NewSQ();

            if (sq.AcType != AccessType.Mysql)
                sq.SQL.ExeNonQuery(XmlResource.BCF_InitData, null);
        }
    }
}
