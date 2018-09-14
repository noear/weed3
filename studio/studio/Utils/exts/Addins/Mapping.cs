using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.Configuration;

namespace Weed.Addins
{
    public delegate bool MapinCheck(string mapFullname);
    internal class Mapin
    {
        public string Name{get;set;}
        public string Type{get;set;}
        public char Split { get; set; }
        public MapinCheck Check;

        /// <summary>
        /// 根据映射关系,和传入的接口名称进行映射
        /// </summary>
        /// <param name="faceFullName">接口全名称</param>
        /// <returns>caseTypeFullName</returns>
        public string Mapping(string faceFullName)
        {
            string[] elems1 = faceFullName.Split(Split);//face   
            string[] elems2 = this.Name.Split(Split);//expr

            if(elems1.Length < elems2.Length)
                return null;

            int len2 = elems2.Length;

            string[] elems3 = null;//out

            if (elems1.Length > elems2.Length)//将前前项,合并为一项(以"."拼接)
            {
                elems3 = new string[len2];

                int _len = elems1.Length - elems2.Length;
                string _temp = "";
                for (int i = 0; i < _len + 1; i++)
                    _temp += (Split + elems1[i]);

                elems3[0] = _temp.TrimStart(Split);
                for (int i = _len + 1; i < elems1.Length; i++)
                    elems3[i - _len] = elems1[i];
            }
            else
                elems3 = elems1;

            for (int i = 0; i < len2; i++)
            {
                if (elems2[i].Length != 3)
                {
                    int temp = elems2[i].IndexOf('{');
                    
                    elems3[i] = elems3[i].Substring(temp);
                }
            }

            string mapFullName = this.Type;
            for (int i = 0; i < len2; i++)
                mapFullName = mapFullName.Replace("{" + i + "}", elems3[i]);

            if (Check(mapFullName))
                return mapFullName;
            else
                return null;
        }
    }

    public static class Mapping
    {
        static Mapping()
        {
            Items = new List<Mapin>();

            Add("{0}.I{1}", "{0}.{1},{0}");
            Add("{0}.I{1}", "{0}.{1},{0}.V1");
            Add("{0}.I{1}", "{0}.{1},{0}.V2");
            Add("{0}.I{1}", "{0}.{1},{0}.V3");
            Add("{0}.I{1}", "{0}.{1},{0}.V4");
            Add("{0}.I{1}", "{0}.{1},{0}.V5");
            Add("{0}.I{1}", "{0}.{1},{0}.V6");

            Add("{0}.I{1}", "{0}.{1}Service,{0}");

            Add("{0}.{1}.I{2}", "{0}.{1}.{1},{0}.{1}.V6");
            Add("{0}.{1}.I{2}", "{0}.{1}.{1},{0}.{1}.V5");
            Add("{0}.{1}.I{2}", "{0}.{1}.{1},{0}.{1}.V4");
            Add("{0}.{1}.I{2}", "{0}.{1}.{1},{0}.{1}.V3");
            Add("{0}.{1}.I{2}", "{0}.{1}.{1},{0}.{1}.V2");
            Add("{0}.{1}.I{2}", "{0}.{1}.{1},{0}.{1}.V1");
            Add("{0}.{1}.I{2}", "{0}.{1}.{1},{0}.{1}");
        }

        public static void Load(string configSection, MapinCheck check)
        { 
             XmlNode section = ConfigurationManager.GetSection(configSection) as XmlNode;

            //WinForm在设计时,会运行此段代码
            //
            if (section == null)
                return;
                //throw new WeedException("不存在[" + configSection + "]配置节");

            //------------------------------------------------------------
            Load(section, check);
            
        }
        public static void Load(XmlNode section, MapinCheck check)
        {
            if (section == null)
                return;            

            XmlNodeList list1 = section.SelectNodes(".//map");//获取所有的map节点

            foreach (XmlNode node in list1)
            {
                XmlAttribute temp = node.Attributes["split"];

                if (temp == null)
                    Mapping.Add(node.Attributes["name"].Value,
                            node.Attributes["type"].Value, check);
                else
                    Mapping.Add(node.Attributes["name"].Value,
                                node.Attributes["type"].Value, temp.Value[0], check);
            }
        }

        internal static List<Mapin> Items = null;

        public static void Add(string name, string type)
        {
            Add(name, type, (mapFullName) =>
            {
                return System.Type.GetType(mapFullName, false) != null;
            });
        }
        public static void Add(string name, string type, MapinCheck check)
        {
            Add(name, type, '.', check);
        }
        public static void Add(string name, string type, char split, MapinCheck check)
        {
            lock (Items)
            {
                foreach (Mapin map in Items)
                {
                    if (map.Name == name && map.Type == type)
                        return;
                }

                if (split > 0)
                    split = '.';

                Items.Add(new Mapin { Name = name, Type = type, Split = split, Check = check });
            }
        }

        public static string GetMapping(string name)
        {
            string mapFullName = null;

            foreach (Mapin map in Items)
            {
                mapFullName = map.Mapping(name);

                if (mapFullName != null)
                    break;
            }

            return mapFullName;
        }

        public static AddinItem Get<T>()
        {
            string faceFullName = typeof(T).FullName;

            string caseTypeFullName = GetMapping(faceFullName);

            if (caseTypeFullName == null)
                return null;
            else
                return Addin.Add<T>(caseTypeFullName);
        }
    }
}
