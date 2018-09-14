using System;
using System.Collections.Generic;
using System.Text;

namespace Weed.OPS
{
    public partial class OpsReader
    {
        #region Text(),Note(),Type()

        public static string Text(string opsData)
        {
            int startPoint = opsData.IndexOf("<Text>");

            if (startPoint < 0)
                return opsData;
            else
            {
                int endPoint = GetPotEnd(opsData, "<Text>", "</Text>", startPoint);

                return opsData.Substring(startPoint + 6, endPoint - startPoint - 6);
            }
        }

        public static string Note(string opsData)
        {
            int startPoint = opsData.IndexOf("<Note>");

            if (startPoint < 0)
                return "";
            else
            {
                int endPoint = opsData.IndexOf("</Note>", startPoint);

                return opsData.Substring(startPoint + 6, endPoint - startPoint - 6);
            }
        }

        public static XType Type(string opsData)
        {
            int startPoint = opsData.IndexOf("<Type>");

            if (startPoint < 0)
                return XType.Text;
            else
            {
                int endPoint = opsData.IndexOf("</Type>", startPoint);

                if (opsData.Substring(startPoint + 6, endPoint - startPoint - 6).ToLower().IndexOf("xml") >= 0)
                    return XType.Xml;
                else
                    return XType.Text;
            }
        }

        #endregion

        public string Text()
        {
            if (IsTextOnly())
                return this.Data;
            else
                return Text(this.Data);
        }

        public string Note()
        {
            return Note(this.Data);
        }

        public XType Type()
        {
            return Type(this.Data);
        }

        /// <summary>
        /// 节点内是否只有Text内容
        /// </summary>
        /// <returns></returns>
        public bool IsTextOnly()
        {
            if (FirstName == "Note" || FirstName == "Text" || FirstName == "Type")
                return false;
            else
                return true;
        }
    }
}
