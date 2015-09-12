using System;
using System.Collections.Generic;
using System.Text;

namespace Weed.Json
{
    public class JDom
    {
        public JDom()
        {
            _Items = new Dictionary<string, JItem>();
        }

        Dictionary<string, JItem> _Items;

        public JItem this[string name]
        {
            get
            {
                if (_Items.ContainsKey(name) == false)
                {
                    JItem temp = new JItem();
                    _Items.Add(name, temp);

                    return temp;
                }
                else
                    return _Items[name];
            }
        }

        public string ToJson()
        {
            StringBuilder sb = new StringBuilder();

            WriteTo(sb);

            return sb.ToString();
        }

        internal void WriteTo(StringBuilder sb)
        {
            sb.Append("{");

            foreach (KeyValuePair<string, JItem> kv in _Items)
            {
                sb.Append("\"").Append(kv.Key).Append("\"");
                sb.Append(":");
                kv.Value.WriteTo(sb);
                sb.Append(",");
            }

            if (sb.Length > 1)
                sb.Remove(sb.Length - 1, 1);

            sb.Append("}");
        }
    }
}
