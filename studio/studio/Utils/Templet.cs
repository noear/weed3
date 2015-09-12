using System;
using System.Text;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.Xml;

using Weed.OPS;
using Weed.SDQ;

namespace weedstudio.Utils {
    public class ReplaceItem {
        public ReplaceItem(XmlNode node) {
            this.Old = node.Attributes["old"].Value;
            this.New = node.Attributes["new"].Value;
        }

        public string Old { get; set; }
        public string New { get; set; }
    }

    /// <summary>
    /// 如果只是用于OPS-XML序列化,不需要override[Clone][Bind]，从而使代码更加干净
    /// </summary>
    public class Templet {
        private string _Main;
        public string Main {
            get {
                return _Main;
            }
            set {
                _Main = value;

                Regex reg = new Regex(@"\{\w*\}");
                MatchCollection mList = reg.Matches(value);

                if (mList.Count == 0)
                    return;

                foreach (Match m in mList)
                    MainKeyList.Add(m.Groups[0].Value.Trim('{', '}'));
            }
        }

        public List<ReplaceItem> ReplaceItems = new List<ReplaceItem>();

        private string _Replace;
        public string Replace {
            get { return _Replace; }
            set {
                _Replace = value;

                if (string.IsNullOrEmpty(value) == false) {
                    ReplaceItems.Clear();

                    foreach (XmlNode node in XDom.ParseXml(value, false).ChildNodes)
                        ReplaceItems.Add(new ReplaceItem(node));
                }
            }
        }

        private string _Items;
        public string Items {
            get {
                return _Items;
            }
            set {
                _Items = value;

                ItemFormatList.Add("<root>" + _Items + "</root>");
            }
        }

        private string _File;
        public string File {
            get { return _File; }
            set {
                string[] temp = value.Split('|');

                _File = temp[0].Trim();

                if (temp.Length > 1 && temp[1].ToLower() == "append")
                    IsAppend = true;
                else
                    IsAppend = false;
            }
        }
        public bool IsAppend { get; private set; }

        private List<string> MainKeyList = new List<string>();
        private XDom ItemFormatList = new XDom();

        public IEnumerable<string> Keys {
            get { return MainKeyList; }
        }

        public string GetItemFormat(string name) {
            string temp = name.TrimEnd('s');

            int index = ItemFormatList.Items.IndexOfKey(temp);

            if (index < 0)
                return null;
            else
                return ItemFormatList.Items.Values[index].Text;
        }
    }
}
