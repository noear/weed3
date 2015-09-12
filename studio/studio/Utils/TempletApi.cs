using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using Weed.OPS;

namespace weedstudio.Utils {
    public static class TempletApi {
        public static List<String> templets() {
            List<String> list = new List<string>();
            foreach (string fileName in Directory.GetFiles(Config.DIR_TEMPLET , "*.xml")) {
                string[] temp1 = fileName.Split('/', '\\');

                if (temp1[temp1.Length - 1][0] != '_')
                    list.Add(temp1[temp1.Length - 1]);
            }

            return list;
        }

        public static Templet getTempletByFile(string file) {
            var opsXml = File.ReadAllText(Config.DIR_TEMPLET + "/" + file, UTF8Encoding.UTF8);

            Templet temp = new Templet();
            XDom.Bind(temp, opsXml);

            return temp;
        }
    }
}
