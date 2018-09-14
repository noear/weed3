using System;
using System.Collections.Generic;
using System.Text;

using Weed.SDQ;

namespace Weed.OPS
{
    public class XItem : IXNode
    {
        public string Name { get; set; }
        private XType _Type = XType.Text;
        public XType Type
        {
            get { return _Type; }
            set { _Type = value; }
        }
        public string Text { get; set; }
        public string Note { get; set; }

        /// <summary>
        /// 以 bool 类型获取值
        /// </summary>
        public bool Bool
        {
            get
            {
                if (Text == null || Text == "0" || Text.ToLower() == "false")
                    return false;
                else
                    return true;
            }
            set
            {
                this.Text = value ? "1" : "0";
            }
        }

        /// <summary>
        /// 以 decimal 类型获取值
        /// </summary>
        public decimal Num
        {
            get
            {
                if (Text == null)
                    return 0;

                decimal temp = 0;
                decimal.TryParse(Text, out temp);

                return temp;
            }
            set
            {
                Text = value.ToString();
            }
        }

        /// <summary>
        /// 以 int 类型获取值
        /// </summary>
        public int NumInt
        {
            get
            {
                if (Text == null)
                    return 0;

                int temp = 0;
                int.TryParse(Text, out temp);

                return temp;
            }
            set
            {
                Text = value.ToString();
            }
        }

        /// <summary>
        /// 以时间类型获取值
        /// </summary>
        public DateTime Time
        {
            get 
            {
                if (Text == null)
                    return DateTime.MinValue;
                

                DateTime temp;
                DateTime.TryParse(Text, out temp);

                return temp;
            }
            set
            {
                Text = value.ToString("yyyy-MM-dd hh:mm:ss");
            }
        }

        protected string _NewText;
        /// <summary>
        /// 格式化后的值 文件值
        /// </summary>
        public string NewText
        {
            get 
            {
                if (textWt != null)
                {
                    _NewText = textWt.ToString();                    
                    textWt = null;
                }

                return _NewText;
            }
        }

        private bool _IsCDATA = false;

        /// <summary>
        /// 去除<![CDATA[]]>代码
        /// </summary>
        /// <returns></returns>
        public XItem CDATA()
        {
            if (_IsCDATA == false)
                this.Text = XDom.CDATA(this.Text);

            return this;
        }        

        public string OuterText
        {
            get { return string.Format("<{0}>{1}</{0}>", Name, Text); }
        }

        public XItem()
        {
            this.Type = XType.Text;
        }

        public XItem(string name)
        {
            this.Name = name;
            this.Type = XType.Text;
        }

        public XItem(string name, string text)
        {
            this.Name = name;
            this.Text = text;
            this.Type = XType.Text;
        }

        public override string ToString()
        {
            return this.Text;
        }

        public string ToXml()
        {
            StringBuilder sb = new StringBuilder();

            WriteXml(sb);

            return sb.ToString();
        }

        #region 支持获取多级数据的扩展
        private XDom _Items;
        public XDom Items
        {
            get 
            {
                if (_Items == null)
                {
                    if (string.IsNullOrEmpty(Text))
                        _Items = new XDom();
                    else
                        _Items = XDom.LoadOps(OuterText);
                }

                return _Items;
            }
            internal set
            {
                _Items = value;
            }
        }

        /// <summary>
        /// 获取子项，如果不存在，则自动添加一项
        /// </summary>
        /// <param name="name">项名称</param>
        /// <returns></returns>
        public XItem this[string name]
        {
            get { return Items[name]; }
        }

        public bool Contains(string name)
        {
            return Items.Contains(name);
        }

        #endregion

        #region 为模板功能提供支持

        /// <summary>
        /// 以Text为模板,进行格式化
        /// ------------------------
        /// 当以模板引擎的形式时,使用
        /// </summary>
        public string Format(params object[] args)
        {
            textWt = new StringBuilder(this.Text);

            textWt.Replace("\r\n", "").Replace("\t", "");

            return (_NewText = string.Format(textWt.ToString(), args));
        }

        public XItem Set(XDom data)
        {
            if (textWt == null)
                textWt = new StringBuilder(this.Text);

            foreach (XItem item in data)
                textWt.Replace("{" + item.Name + "}", item.Text);

            return this;
        }
        

        StringBuilder textWt;
        public XItem Set(string key, string val)
        {
            if (textWt == null)
                textWt = new StringBuilder(this.Text);

            textWt.Replace(key, val);

            return this;
        }

        #endregion

        #region 内部方法

        internal void WriteXml(StringBuilder sb)
        {
            sb.AppendFormat("<{0}>",this.Name );

            if (_Items != null)
            {
                foreach (XItem xi in _Items)
                    xi.WriteXml(sb);
            }
            else
            {
                if (string.IsNullOrEmpty(Note) == false)
                {
                    sb.Append("<Note>").Append(this.Note).Append("</Note>");
                    sb.Append("<Text>").Append(this.Text).Append("</Text>");
                }
                else
                    sb.Append(this.Text);
            }

            sb.AppendFormat("</{0}>",this.Name );
        }     

        private  string EscapeText()
        {
            if (this.Type == XType.Xml)
                return this.Text;
            else
                return Escape.XmlEscape(this.Text);
        }

        private void UnescapeText(string text)
        {
            if (this.Type == XType.Xml)
                this.Text = text;
            else
                this.Text = Escape.XmlUnEscape(text);
        }

        #endregion
    }

    public enum XType
    {
        /// <summary>
        /// 文本（需要进行XML特殊字符转换）
        /// </summary>
        Text,
        /// <summary>
        /// XML数据（不需要进行XML特殊字符转换）
        /// </summary>
        Xml,
    }
}
