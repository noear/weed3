using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.IO;

using Weed.SDQ;

namespace Weed.OPS
{
    public class XList<T> : List<T> where T :IOpsSerializable
    {
        #region 实例化
        public XList() : base() { }

        public XList(int capacity) : base(capacity) { }

        public XList(string opsXml) : base() { Bind(opsXml); }

        public XList(IEnumerable<T> collection) : base(collection) { }
        #endregion

        #region IOpsSerializable 成员

        public void Bind(string opsXml)
        {
            OpsReader read = new OpsReader();
            read.LoadOps(opsXml);

            while (read.Read())
            {
                T dom = Activator.CreateInstance<T>();

                dom.Bind("<" + read.Name + ">" + read.Data + "</" + read.Name + ">");

                this.Add(dom);
            }
        }

        public string ToXml()
        {
            StringBuilder sb = new StringBuilder();
            string entName = typeof(T).Name;

            sb.AppendFormat("<ArrayOf{0}>", entName);

            if (_IsXmlEscape && this.Count > 0)
            {
                T obj1 = this[0];

                foreach (T obj in this)
                        sb.Append(obj.ToXml());
            }
            else
            {
                foreach (T obj in this)
                    sb.Append(obj.ToXml());
            }

            sb.AppendFormat("</ArrayOf{0}>", entName);

            return sb.ToString();
        }

        #endregion

        public void Load(string uri, Encoding code)
        {
            Bind(File.ReadAllText(uri, code));
        }

        public static XList<T> Load(string fileUri)
        {
            XList<T> temp = new XList<T>();
            temp.Load(fileUri, Encoding.Default);

            return temp;
        }

        private bool _IsXmlEscape = false;
        private string _TimeFormat = null;
        public XList<T> XFormat(bool isXmlEscape)
        {
            _IsXmlEscape = isXmlEscape;

            return this;
        }

        public XList<T> XFormat(string timeFormat)
        {
            _TimeFormat = timeFormat;

            return this;
        }

        public static XList<T> LoadOps(string opsXml)
        {
            XList<T> temp = new XList<T>();

            temp.Bind(opsXml);

            return temp;
        }
        
    }
}
