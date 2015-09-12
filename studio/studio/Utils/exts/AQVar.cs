using System;


namespace Weed.SDQ
{
    /// <summary>
    /// 
    /// </summary>
    [Serializable]
    public class AQVar 
    {
        /// <summary>
        /// 变量名称
        /// </summary>
        public string Name { get; internal set; }

        /// <summary>
        /// 是否需要核查（如过滤时...）
        /// </summary>
        public bool IsCheck { get { return _IsCheck; } set { _IsCheck = value; } }
        [NonSerialized]
        private bool _IsCheck = true;

        /// <summary>
        /// 变量数据
        /// </summary>
        public virtual object Data { get; set; }

        /// <summary>
        /// 数据类型
        /// </summary>
        public QDbType Type { get; internal set; }

        internal string TypeName
        {
            get
            {
                if (this.Type == QDbType.Bool)
                    return "Bool";
                else if (this.Type == QDbType.NUM)
                    return "NUM";
                else if (this.Type == QDbType.Text)
                    return "Text";
                else
                    return "Time";
            }
        }

        [NonSerialized]
        private bool _IsNull = true;
        public bool IsNull
        {
            get
            {
                return _IsNull;
            }
            protected set { _IsNull = value; }
        }

        #region SetText,GetText

        /// <summary>
        /// value类型
        /// </summary>
        protected virtual Type _DateType()
        {
            return Data.GetType();
        }

        /// <summary>
        /// 不允许通过SetText设置Null值 
        /// ==============================
        /// 由于SetText主要用于绑定;避免大量null值产生
        /// </summary>
        /// <param name="text"></param>
        public void SetText(string text)
        {
            if (text == null)
                return;

            if (Type == QDbType.Text)
                this.Data = text;
            else
            {
                if (text == null || text.Length == 0)
                    return;

                if (Type == QDbType.Bool)
                    this.Data = (text == "1" || text.ToLower() == "true");
                else if (Type == QDbType.Text)
                    this.Data = text;
                else if (Type == QDbType.Time)
                    this.Data = DateTime.Parse(text);
                else
                {
                    try
                    {
                        this.Data = Convert.ChangeType(text, _DateType());
                    }
                    catch
                    {
                        throw new InvalidCastException(this.Name + "转换时，[" + text + "（String）]=>（" + this.Type.ToString() + "）失败！");
                    }
                }
            }
        }

        public string GetText()
        {
            return GetText(this.Data, null);
        }

        //public string GetText(object data) 与 GetText(string) 有从属不明的可能
        //{
        //    return GetText(data, null);
        //}

        public string GetText(string timeFormat)
        {
            return GetText(this.Data, timeFormat);
        }

        public string GetText(object data, string timeFormat)
        {
            if (Type == QDbType.Bool)
                return (true.Equals(data) ? "1" : "0");

            if (data == null)
                return "";

            if (Type == QDbType.Time)
            {
                DateTime temp = (DateTime)data;

                if (temp.Year == 1)
                    return "";

                if (timeFormat == null)
                    return temp.ToString();
                else
                    return temp.ToString(timeFormat);
            }
            else
                return data.ToString();
        }

        #endregion
    }
}
