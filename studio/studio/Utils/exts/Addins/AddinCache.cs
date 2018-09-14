using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.Configuration;

namespace Weed.Addins
{
    /// <summary>
    /// 插件缓存器
    /// </summary>
    public class AddinCache
    {
        internal static AddinCache G = new AddinCache();

        private AddinCache()
        {
            _Items = new Dictionary<string, AddinItem>(20, StringComparer.OrdinalIgnoreCase);

            Load("addinsSettings");
        }

        private Dictionary<string, AddinItem> _Items;
        internal Dictionary<string, AddinItem> Items
        {
            get
            {
                return _Items;
            }
        }

        internal bool Contains(string name)
        {
            return Items.ContainsKey(name);
        }

        internal AddinItem this[string name]
        {
            get 
            {
                if (Items.ContainsKey(name))
                    return Items[name];
                else
                    return null;
            }
        }

        internal AddinItem this[int index]
        {
            get
            {
                int temp1 = 0;

                foreach (KeyValuePair<string, AddinItem> kv in Items)
                {
                    if (temp1 == index)
                        return kv.Value;

                    temp1++;
                }

                return null;
            }
        }

        internal AddinItem Add(string interfaceName, string caseTypeName)
        {
            lock (_Items)
            {
                AddinItem temp = new AddinItem(caseTypeName);

                if (_Items.ContainsKey(interfaceName) == false)
                    _Items.Add(interfaceName, temp);

                return temp;
            }
        }

        #region Build

        public void Load(string configSection)
        {
            XmlNode section = ConfigurationManager.GetSection(configSection) as XmlNode;

            //WinForm在设计时,会运行此段代码
            //
            if (section == null)
                return;
                //throw new WeedException("不存在[" + configSection + "]配置节");

            //------------------------------------------------------------
            Load(section);
        }        

        public void LoadXml(string xml)
        {
            if (string.IsNullOrEmpty(xml))
                return;

            XmlDocument xmlDoc = new XmlDocument();

            xmlDoc.LoadXml(xml);

            Load(xmlDoc.DocumentElement);
        }

        public void Load(XmlNode section)
        {
            if (section == null)
                return;

            XmlNodeList list = section.SelectNodes(".//add");//获取所有的add点

            //可能会被多次加载,多个配置信息
            //
            if (_Items == null)
                _Items = new Dictionary<string, AddinItem>(list.Count, StringComparer.OrdinalIgnoreCase);

            foreach (XmlNode node in list)
            {
                string name = node.Attributes["name"].Value;

                _Items.Add(name, new AddinItem(node));
            }

            XmlNodeList list1 = section.SelectNodes(".//map");//获取所有的map节点

            foreach (XmlNode node in list1)
            {
                Mapping.Add(node.Attributes["name"].Value, 
                            node.Attributes["type"].Value);
            }
        }


        #endregion
    }
}
