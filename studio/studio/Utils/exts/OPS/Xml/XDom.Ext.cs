using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;

namespace Weed.OPS
{
    public partial class XDom
    {
        public static XmlNode ParseXml(string xml, bool isDoc)
        {
            XmlDocument doc = new XmlDocument();

            if (isDoc)
                doc.LoadXml(xml);
            else
                doc.LoadXml("<root>" + xml + "</root>");

            return doc.DocumentElement;
        }

        /// <summary>
        /// 去除<![CDATA[]]>代码
        /// </summary>
        /// <param name="xmlText"></param>
        /// <returns></returns>
        public static string CDATA(string xml)
        {
            string temp = xml ?? "";

            if (temp.Length < 12)
                return temp;

            int begIndex = -1;
            if ((begIndex = temp.IndexOf("<![CDATA[")) >= 0)
            {
                //如果这之前,有一个<,则不处理
                //
                if (temp.IndexOf('<') - begIndex != 0)
                    return temp;

                begIndex += 9;
                int endIndex = temp.LastIndexOf("]]>");

                if (endIndex > 0 && endIndex >= begIndex)
                    temp = temp.Substring(begIndex, endIndex - begIndex);
            }

            return temp;
        }
    }
}
