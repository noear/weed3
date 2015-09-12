using System;
using System.IO;
using System.Collections.Generic;

using Weed.SDQ;
using Weed.OPS;

using weedstudio.exts;

namespace weedstudio.Utils {
    public class ObjectItem
    {
        public string Table { get; set; }
        public string View { get; set; }
        public string StoredProcedure { get; set; }
        public string Database { get; set; }
    }

    public class DefaultItem
    {
        public string Char { get; set; }
        public string NUM { get; set; }
        public string Time { get; set; }
        public string Bool { get; set; }

        public string Get(QDbType type)
        {
            if (type == QDbType.NUM)
                return this.NUM;
            else if (type == QDbType.Text)
                return this.Char;
            else if (type == QDbType.Time)
                return this.Time;
            else
                return this.Bool;
        }
    }

    public class PropertyItem {
        public string Table { get; set; }
        public string View { get; set; }
        public string StoredProcedure { get; set; }
    }

    public class SourceConfig {
        private static Dictionary<string,XDom> __Config;
        static SourceConfig() {
            __Config = new Dictionary<string, XDom>();

            foreach (var f1 in Directory.GetFiles(Config.DIR_BUILDER)) {
                var temp = XDom.Load(f1);
                var name = Path.GetFileName(f1).Replace(".xml", "");

                __Config.Add(name, temp);
            }
        }

        public static ObjectItem GetObject(string name, DbContextEx db) {
            ObjectItem temp = new ObjectItem();

            string opsXml = __Config[name]["Object"].OuterText;

            if (opsXml.IndexOf("{db}") > 0)
                opsXml = opsXml.Replace("{db}", db.getSchema());

            XDom.Bind(temp, opsXml);

            return temp;
        }

        public static DefaultItem GetDefault(string name) {
            DefaultItem temp = new DefaultItem();

            XDom.Bind(temp, __Config[name]["Default"].OuterText);

            return temp;
        }

        public static PropertyItem GetProperty(string name, DbContextEx db) {
            PropertyItem temp = new PropertyItem();

            string opsXml = __Config[name]["Property"].OuterText;

            if (opsXml.IndexOf("{db}") > 0)
                opsXml = opsXml.Replace("{db}", db.getSchema());

            XDom.Bind(temp, opsXml);

            if (temp.View.Trim().IndexOf(" ") < 0)
                temp.View = temp.Table;

            return temp;
        }
    }
}
