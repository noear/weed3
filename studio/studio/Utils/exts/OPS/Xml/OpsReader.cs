
namespace Weed.OPS
{
    public partial class OpsReader
    {
        public OpsReader()
        {
            this.opsText = "";
            this.index = 0;
            this.Name = "";
            this.Data = "";
        }

        private string opsText = null;
        private int index = 0;
        public string Name { get; set; }
        public string Data { get; set; }

        private string _RootName = "";
        /// <summary>
        /// 根节点名称
        /// </summary>
        public string RootName
        {
            get
            {
                return _RootName;
            }
        }

        /// <summary>
        /// 加载级一级的opsXml
        /// </summary>
        /// <param name="p_ops">上一级opsXml</param>
        /// <param name="tagName">标记名称</param>
        public void LoadOps(string p_ops, string tagName)
        {
            this.index = 0;

            if (p_ops == null)
                return;

            this._RootName = tagName;

            string tagStart = "<" + tagName + ">";
            string tagEnd = "</" + tagName + ">";

            int startPoint = TagIndexOf(p_ops, tagStart, 0);
            int endPoint = TagLastIndexOf(p_ops, tagEnd, p_ops.Length - tagStart.Length);

            int tagLen = tagStart.Length;

            this.opsText = p_ops.Substring(startPoint + tagLen, endPoint - startPoint - tagLen);
        }

        /// <summary>
        /// 加载级一级的opsXml(自动抽取根节点名)
        /// </summary>
        /// <param name="p_ops">上一级opsXml</param>
        public void LoadOps(string p_ops)
        {
            string temp = p_ops.Trim();

            string tagName = GetFirstName(temp);

            this.LoadOps(temp, tagName);
        }

        public static bool IsArrayOfXml(string opsXml)
        {
            string temp = GetFirstName(opsXml);

            return temp.IndexOf("ArrayOf") == 0;
        }        

        public static string GetFirstName(string opsXml)
        {
            if (opsXml.Length < 7)//说明不够开有<></>
                return "";

            int potStart = opsXml.IndexOf('<', 0);

            if (potStart != 0)
                return "";
            else if (opsXml[potStart + 1] == '?')
                potStart = opsXml.IndexOf('<', potStart + 1);

            //再判断一次
            if (potStart < 0)
                return "";

            int potEnd = opsXml.IndexOf('>', potStart + 1);

            if (potEnd > potStart)
                return opsXml.Substring(potStart + 1, potEnd - potStart - 1);
            else
                return "";
        }

        public bool Read()
        {
            int potStart = this.opsText.IndexOf('<', this.index);
            if (potStart >= 0)
            {
                if (this.opsText[potStart + 1] == '!') //充许没有"<|>"的注释
                    potStart = this.opsText.IndexOf('<', potStart + 1);
            }

            if (potStart < 0)
                return false;
            else
            {
                int potEnd = this.opsText.IndexOf('>', potStart);


                this.Name = this.opsText.Substring(potStart + 1, potEnd - potStart - 1);
                this.Data = this.GetData(potStart);

                this.index = this.index + this.Name.Length + 3;

                //设置第一个节点名
                //
                this.FirstName = GetFirstName(this.Data);

                return true;
            }
        }

        /// <summary>
        /// 首节点名字
        /// </summary>
        public string FirstName { get; set; }
        
        private string GetData(int tagStartIndex)
        {
            string tagStart = "<" + this.Name + ">";
            string tagEnd = "</" + this.Name + ">";

            int potStart = tagStartIndex;//TagIndexOf(this.opsText, tagStart, this.index);

            if (potStart < 0)
                return "";
            else
            {
                int potEnd = GetPotEnd(this.opsText, tagStart, tagEnd, potStart);
                int tagLen = tagStart.Length;

                this.index = potEnd;

                
                return this.opsText.Substring(potStart + tagLen, potEnd - potStart - tagLen);
            }
        }

        private static int GetPotEnd(string opsXml, string tagStart, string tagEnd, int potStart)
        {
            int endIndex = TagIndexOf(opsXml, tagEnd, potStart + tagStart.Length);//第一次取

            if (endIndex - potStart > tagStart.Length)
            {
                int startIndex = TagIndexOf(opsXml, tagStart, potStart + 2, endIndex - potStart - tagStart.Length);//已是第二次取


                if (startIndex > 0)
                {
                    while (startIndex < endIndex)
                    {
                        endIndex = TagIndexOf(opsXml,tagEnd, endIndex + 2);//n+1

                        startIndex = TagIndexOf(opsXml, tagStart, startIndex + 2, endIndex - startIndex - 2);	//n+2

                        if (startIndex < 0)
                            return endIndex;
                    }
                }
            }

            return endIndex;
        }

        

        #region TagIndexOf,TagLastIndexOf
        private static int TagIndexOf(string opsXml, string tagName, int index, int count)
        {
            int idx = opsXml.IndexOf('<', index, count);

            if (idx < 0)
                return -1;
            else
            {
                if (opsXml.Substring(idx, tagName.Length) == tagName)
                    return idx;
                else
                {
                    count = count - idx - 1;

                    if (count > 0)
                        return TagIndexOf(opsXml, tagName, idx + 1, count);
                    else
                        return -1;
                }
            }
        }

        private static int TagIndexOf(string opsXml, string tagName, int index)
        {
            int idx = opsXml.IndexOf('<', index);

            if (idx < 0)
                return -1;
            else
            {
                if (opsXml.Substring(idx, tagName.Length) == tagName)
                    return idx;
                else
                    return TagIndexOf(opsXml, tagName, idx + 1);
            }
        }

        private static int TagLastIndexOf(string opsXml, string tagName, int index)
        {
            int idx = opsXml.LastIndexOf('<', index);

            if (idx < 0)
                return -1;
            else
            {
                if (opsXml.Substring(idx, tagName.Length) == tagName)
                    return idx;
                else
                {
                    idx--;
                    if (idx > 0)
                        return TagLastIndexOf(opsXml, tagName, idx);
                    else
                        return -1;
                }
            }
        }
        #endregion
    }
}
