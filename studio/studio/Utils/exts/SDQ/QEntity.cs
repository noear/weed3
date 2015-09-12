using System;
using System.Collections.Specialized;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;
using System.Data;
using System.Reflection;

using Weed.OPS;

namespace Weed.SDQ
{
    /// <summary>
    /// 数据实体基类
    /// --------------------------
    /// 创建:谢月甲,20080429
    /// </summary>
    [Serializable]
    public abstract partial class QEntity 
    {
        public QEntity()
        {
            OnInit();
        }

        protected virtual void OnInit()
        {
 
        }

        #region IQData 成员

        public object GetData(string key)
        {
            return _GetData(key);
        }

        #endregion

        

        protected string __InvalidKey = "";
        

        /// <summary>
        /// 数据强制转换失败时，具体提供信息的异常
        /// </summary>
        /// <typeparam name="T">强制转换的类型</typeparam>
        /// <param name="val">数据</param>
        protected InvalidCastException __InvalidCastException<T>(object val, bool isEval)
        {
            return new InvalidCastException(__InvalidKey + "[" + val.ToString() + "（" + val.GetType().Name + "）]" + (isEval ? "强制" : "") + "转换为（" + typeof(T).Name + "）失败！");
        }

          

        #region _GetData & _SetData

        /// <param name="propName">支持大写小</param>
        internal object _GetData(string propName)
        {
            PropertyInfo prop = _GetProp(propName);

            if (prop != null)
                return prop.GetValue(this, null);
            else
                return null;
        }

        /// <summary>
        /// 用于DataReader,DataRow的绑定
        /// </summary>
        /// <param name="propName">支持大写小</param>
        /// <param name="data"></param>
        private void _SetData(string propName, object data)
        {
            if (data == DBNull.Value)
                return;

            PropertyInfo prop = _GetProp(propName);

            if (prop != null)
            {
                try
                {
                    prop.SetValue(this, data, null);
                }
                catch (Exception ex)
                {
                    throw new ArgumentException("[" + propName + "]绑定失败：" + ex.Message);
                }
            }
        }
        private void _SetData(PropertyInfo prop, object data)
        {
            if (data == DBNull.Value)
                return;

            try
            {
                prop.SetValue(this, data, null);
            }
            catch (Exception ex)
            {
                throw new ArgumentException("[" + prop.Name + "]绑定失败：" + ex.Message);
            }
        }

        private Type _TYPE;
        private PropertyInfo _GetProp(string name)
        {
            if (_TYPE == null)
                _TYPE = this.GetType();

            return _TYPE.GetProperty(name,
                BindingFlags.Public |
                BindingFlags.Instance |
                BindingFlags.IgnoreCase);
        }

        private IEnumerable<PropertyInfo> _GetProps()
        {
            if (_TYPE == null)
                _TYPE = this.GetType();

            return _TYPE.GetProperties(BindingFlags.Public | BindingFlags.Instance);
        }


        #endregion

        #region SetText,GetText

        protected void SetText(string key, string text) {
            PropertyInfo temp = _GetProp(key);

            if (temp != null) {
                TypeCode type = Type.GetTypeCode(temp.PropertyType);

                if (text == null || text.Length == 0) {
                    if (type == TypeCode.String)
                        temp.SetValue(this, "", null);

                    return;
                }

                if (type == TypeCode.Boolean)
                    temp.SetValue(this, (text == "1"), null);
                else
                    temp.SetValue(this, Convert.ChangeType(text, temp.PropertyType), null);
            }
        }

        protected string GetText(PropertyInfo prop, string timeFormat) {
            TypeCode type = Type.GetTypeCode(prop.PropertyType);

            object data = prop.GetValue(this, null);

            if (data == null)
                return "";

            if (type == TypeCode.Boolean)
                return data.Equals(true) ? "1" : "0";
            else if (type == TypeCode.DateTime) {
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

        #region IOpsSerializable 成员

        public void Bind(string opsText)
        {
            if (opsText == null || opsText.Length == 0)
                return;

            OpsReader reader = new OpsReader();
            reader.LoadOps(opsText);

            while (reader.Read())
            {
                SetText(reader.Name, Escape.XmlUnEscape(reader.Text()));
            }
        }

        [NonSerialized]
        private string _TimeFormat = null;
        [NonSerialized]
        private bool _IsXmlEscape = false;
        public QEntity XFormat(string timeFormat)
        {
            _TimeFormat = timeFormat;

            return this;
        }
        public QEntity XFormat(bool isXmlEscape)
        {
            _IsXmlEscape = isXmlEscape;

            return this;
        }

        public string ToXml()
        {
            StringBuilder xml = new StringBuilder();

            xml.Append("<" + GetType().Name + ">");

            foreach (PropertyInfo prop in _GetProps())
            {
                if (_IsXmlEscape)
                    xml.AppendFormat("<{0}>{1}</{0}>", prop.Name, Escape.Xml(GetText(prop, _TimeFormat)));
                else
                    xml.AppendFormat("<{0}>{1}</{0}>", prop.Name, GetText(prop, _TimeFormat));
            }

            xml.Append("</" + GetType().Name + ">");

            return xml.ToString();
        }

        

        public IEnumerator<XItem> GetEnumerator()
        {
            foreach (PropertyInfo prop in _GetProps())
                yield return new XItem(prop.Name, GetText(prop, _TimeFormat));
        }

        #endregion
        
    }
}
