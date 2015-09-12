using System;
using System.Text;
using System.Data;

using System.Collections.Generic;

namespace Weed.SDQ
{
    /// <summary>
    /// 数据[表/视图].列基类（查询列）
    /// -----------------------------
    /// 所有函数、QView都通过封装为QColumn，进而归属到查询系统
    /// </summary>
    public partial class QColumn : AQVar
    {
        public QColumn()
        {
            this.IsInc = true;
        }

        public QColumn(string name, QDbType type, bool isKey):this()
        {
            this.Name = name;
            this.Type = type;
            this.IsKey = isKey;
        }

        #region Column

        /// <summary>
        /// if(isTryBracket = false)
        /// </summary>
        /// <param name="asName"></param>
        /// <returns></returns>
        public QColumn As(string asName)
        {
            return As(asName, false);
        }

        public QColumn As(string asName, bool isTryBracket)
        {
            if (isTryBracket && string.IsNullOrEmpty(Express) == false && Express[0] != '(')
                this.Express = "(" + this.Express + ")";

            this.AsName = asName;

            return this;
        }
        

        public string AsName { get; set; }

        /// <summary>
        /// 是否为关键字
        /// </summary>
        public bool IsKey { get; set; }
        /// <summary>
        /// 是否为索引
        /// </summary>
        public bool IsIndex { get; set; }

        /// <summary>
        /// 数据库的数据类型
        /// </summary>
        public DbType DbType { get; internal set; }

        /// <summary>
        /// 是否为动态数据(只对插入的更新有效)（目前还没有提供友好支持）
        /// </summary>
        [NonSerialized]
        internal bool IsDData = false;

        [NonSerialized]
        internal QColumn _DData;
        /// <summary>
        /// 设置动态数据（包括运算数据，引用数据，函数数据等）
        /// </summary>
        /// <example>
        /// 1. Count = Count+1;
        /// 2. Count = MAX(UserId);
        /// 3. Count = b.Count;
        /// </example>
        /// <param name="dData">动态数据</param>
        /// <returns></returns>
        protected bool DData(QColumn dData)
        {
            _DData = dData;
            this.IsNull = false;
            this.IsDData = true;
            return true;
        }

        /// <summary>
        /// 当执行 m.HasChange(m.Column) == true,时附带的旧值
        /// </summary>
        public virtual object _OLD { get; internal set; }

        public object _NEW { get { return Data; } }

        public string _NEW_TEXT()
        {
            return GetText(_NEW, null);
        }

        public string _OLD_TEXT()
        {
            return GetText(_OLD, null);
        }

        /// <summary>
        /// SQL字段类型（为生成品预留的属性）
        /// </summary>
        public string SqlType { get; set; }
        
        /// <summary>
        /// 是否包括（..在操作范围内[insert, select, update]）
        /// </summary>
        public bool IsInc { get; set; }

        

        /// <summary>
        /// 注释或说明
        /// </summary>
        public string Note { get; set; }


        

        /// <summary>
        /// 表达式
        /// =============================
        /// 如果列由函数创建,则使用此变量
        /// </summary>
        internal string Express = null;        
        

        /// <summary>
        /// 被调用时的名字(Where...Order...)
        /// </summary>
        //internal string CallName
        //{
        //    get
        //    {
        //        if (string.IsNullOrEmpty(AsName))
        //            return FullName;
        //        else
        //            return AsName;
        //    }
        //}

        #endregion   
     
        [NonSerialized]
        private Dictionary<string, string> __Meta;

        /// <summary>
        /// 获取或设置元信息
        /// </summary>
        /// <param name="key">键</param>
        /// <returns>值</returns>
        public string this[string key]
        {
            get
            {
                if (__Meta == null)
                    return null;

                if (__Meta.ContainsKey(key))
                    return __Meta[key];
                else
                    return null;
            }
            set
            {
                if (__Meta == null)
                {
                    __Meta = new Dictionary<string, string>();
                    __Meta.Add(key, value);
                }
                else
                {
                    if (__Meta.ContainsKey(key))
                        __Meta[key] = value;
                    else
                        __Meta.Add(key, value);
                }
            }
        }

    }
}
