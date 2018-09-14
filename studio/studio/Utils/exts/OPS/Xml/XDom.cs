using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Text;
using System.IO;
using System.Data;

using Weed.SDQ;

namespace Weed.OPS
{
    public partial class XDom:IXNode,IEnumerable<XItem>
    {
        /// <summary>
        /// 不可能设定默认值!（AsItem)时
        /// </summary>
        public string Name { get; set; }

        #region XDom

        private SortedList<string,XItem> _Items;
        public SortedList<string,XItem> Items
        {
            get { return _Items; }
        }

        public XDom()
        {
            _Items = new SortedList<string, XItem>(StringComparer.OrdinalIgnoreCase);
        }

        /// <summary>
        /// 获取子项，如果不存在，则自动添加一项
        /// </summary>
        /// <param name="name">项名称</param>
        /// <returns></returns>
        public XItem this[string name]
        {
            get
            {
                int index = Items.IndexOfKey(name);

                if (index < 0)
                {
                    XItem temp = new XItem(name);
                    Items.Add(name, temp);

                    return temp;
                }
                else
                    return Items.Values[index];

            }
        }

        public XItem this[int index]
        {
            get { return Items.Values[index]; }
        }        

        public int Count
        {
            get { return Items.Count; }
        }

        public void Clear()
        {
            Items.Clear();
        }

        public bool Contains(string name)
        {
            return Items.ContainsKey(name);
        }

        #endregion

        #region Add

        public XDom Add(IOpsSerializable data)
        {
            foreach (XItem item in data)
                Add(item);

            return this;
        }

        public XDom Add(string opsXml)
        {
            Bind(opsXml);

            return this;
        }

        public XDom Add(string name, string text)
        {
            return Add(new XItem(name, text));
        }

        public XDom Add(XItem item)
        {
            if (item != null)
                if (!Items.ContainsKey(item.Name))
                    Items.Add(item.Name, item);

            return this;
        }

        public XDom Add(IEnumerable<XItem> items)
        {
            foreach (XItem item in items)
                Add(item);

            return this;
        }

        public XDom Add(string name, IEnumerable<XItem> items)
        {
            int index = Items.IndexOfKey(name);
            XItem item;
            if (index < 0)
            {
                item = new XItem(name);
                Add(item);
            }
            else
                item = Items.Values[index];

            item.Items.Add(items);

            return this;
        }
        

        public XDom Add(NameValueCollection coll)
        {
            for (int i = 0, len = coll.Count; i < len; i++)
                Add(coll.GetKey(i), coll.Get(i));

            return this;
        }

        public XDom Add(DataRow dRow)
        {
            foreach (DataColumn col in dRow.Table.Columns)
                Add(new XItem(col.ColumnName, dRow[col.ColumnName].ToString()));

            return this;
        }

        public XDom Add(IDataReader dReader)
        {
            try
            {
                if (dReader.Read())
                {
                    object temp;
                    for (int i = 0; i < dReader.FieldCount; i++)
                    {
                        temp = dReader.GetValue(i);

                        if (temp != DBNull.Value)
                            this.Add(dReader.GetName(i), temp.ToString());
                    }
                }
            }
            finally
            {
                dReader.Close();
            }

            return this;
        }

        public void Remove(string name)
        {
            _Items.Remove(name);
        }

        #endregion

        #region Load

        public void Load(string uri, Encoding code)
        {
            Add(File.ReadAllText(uri, code));
        }

        #endregion

        #region IOpsSerializable 成员

        public void Bind(string opsXml)
        {
            if (string.IsNullOrEmpty(opsXml))
                return;

            OpsReader reader = new OpsReader();
            reader.LoadOps(opsXml);

            this.Name = reader.RootName;

            while (reader.Read())
            {
                XItem item = new XItem();
                item.Name = reader.Name;

                if (reader.IsTextOnly())
                {
                    item.Type = XType.Xml;
                    item.Text = reader.Data;
                }
                else
                {
                    item.Type = reader.Type();
                    item.Note = reader.Note();
                    item.Text = reader.Text();
                }

                Add(item); 
            }
        }

       

        public XDom AsName(string name)
        {
            this.Name = name;

            return this;
        }

        public string ToXml()
        {
            string nodeName = this.Name;
            if (string.IsNullOrEmpty(nodeName))
                nodeName = "OPS";

            StringBuilder xml = new StringBuilder();

            xml.Append("<" + nodeName + ">");

            foreach (XItem item in this.Items.Values)
                item.WriteXml(xml);

            xml.Append("</" + nodeName + ">");

            return xml.ToString();
        }

        public string InnerXml()
        {
            StringBuilder xml = new StringBuilder();

            foreach (XItem item in this.Items.Values)
                item.WriteXml(xml);

            return xml.ToString();

        }


        public IEnumerator<XItem> GetEnumerator()
        {
            return Items.Values.GetEnumerator();
        }
        
        #endregion

        public void Save(string path)
        {
            File.WriteAllText(path, this.ToXml());
        }

        public void Save(string path, Encoding encoding)
        {
            File.WriteAllText(path, this.ToXml(), encoding);
        }

        #region IXData 成员

        public string GetText(string key)
        {
            int index = Items.IndexOfKey(key);

            if (index < 0)
                return null;
            else
                return Items.Values[index].Text;
        }

        #endregion

        #region IEnumerable 成员

        IEnumerator IEnumerable.GetEnumerator()
        {
            return Items.Values.GetEnumerator();
        }

        #endregion

        public XItem AsItem()
        {
            if (this.Name == null)
                return null;

            XItem temp = new XItem(Name);

            temp.Items = this;

            return temp;
        }
    }
}
