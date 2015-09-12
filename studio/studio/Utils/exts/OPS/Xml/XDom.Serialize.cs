using System;
using System.Collections.Generic;
using System.Text;
using System.Reflection;
using System.Xml;

namespace Weed.OPS
{
    public partial class XDom
    {
        /// <summary>
        /// 过时的方法,请改用 LoadUri
        /// </summary>
        /// <param name="fileUri"></param>
        /// <returns></returns>
        public static XDom Load(string fileUri)
        {
            return LoadUri(fileUri);
        }

        public static XDom LoadUri(string fileUri)
        {
            return LoadUri(fileUri, Encoding.Default);
        }

        public static XDom LoadUri(string fileUri, Encoding encoding)
        {
            XDom temp = new XDom();
            temp.Load(fileUri, encoding);

            return temp;
        }

        public static XDom LoadOps(string opsXml)
        {
            XDom temp = new XDom();
            temp.Add(opsXml);

            return temp;
        }

        public static XDom LoadXml(XmlNode node)
        {
            XDom temp = new XDom();

            foreach(XmlAttribute a in node.Attributes)
                temp.Add(a.Name, a.Value);

            foreach (XmlNode n in node.ChildNodes)
                temp.Add(n.Name, n.InnerXml);

            return temp;
        }

        public static XDom LoadXml(string xml, bool isDoc)
        {
            return LoadXml(XDom.ParseXml(xml, isDoc));
        }
        
        public static void Bind(object obj, string opsXml)
        {
            if (opsXml == null || opsXml.Length == 0)
                return;

            Type type = obj.GetType();

            OpsReader reader = new OpsReader();
            reader.LoadOps(opsXml);

            while (reader.Read())
            {
                SetText(type, obj, reader.Name, Escape.XmlUnEscape(reader.Text()));
            }
        }

        public static string ToXml(object obj)
        {
            Type type = obj.GetType();
            StringBuilder xml = new StringBuilder();

            xml.Append("<" + type.Name + ">");

            foreach (PropertyInfo prop in _GetProps(type))
            {
                if (prop.CanRead && prop.Name != "Item")
                    xml.AppendFormat("<{0}>{1}</{0}>", prop.Name, Escape.XmlEscape(GetText(prop, obj)));
            }

            xml.Append("</" + type.Name + ">");

            return xml.ToString();
        }

        #region

        private static string GetText(PropertyInfo prop, object obj)
        {
            TypeCode type = Type.GetTypeCode(prop.PropertyType);

            object data = prop.GetValue(obj, null);

            if (data == null)
                return "";

            if (type == TypeCode.Boolean)
                return data.Equals(true) ? "1" : "0";
            else
                return data.ToString();
        }

        private static void SetText(Type objT ,object obj,string key, string text)
        {
            PropertyInfo temp = _GetProp(objT, key);

            if (temp != null)
            {
                TypeCode type = Type.GetTypeCode(temp.PropertyType);

                if (text == null || text.Length == 0)
                {
                    if (type == TypeCode.String)
                        temp.SetValue(obj, "", null);

                    return;
                }

                if (type == TypeCode.Boolean)
                    temp.SetValue(obj, (text == "1"), null);
                else
                    temp.SetValue(obj, Convert.ChangeType(text, temp.PropertyType), null);
            }
        }

        private static IEnumerable<PropertyInfo> _GetProps(Type type)
        {
            return type.GetProperties(BindingFlags.Public | BindingFlags.Instance);
        }

        private static PropertyInfo _GetProp(Type type, string name)
        {
            return type.GetProperty(name,
                BindingFlags.Public |
                BindingFlags.Instance |
                BindingFlags.IgnoreCase);
        }

        #endregion
    }
}
