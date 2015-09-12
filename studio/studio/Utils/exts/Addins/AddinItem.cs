using System;
using System.Collections.Generic;
using System.Text;
using System.Reflection;
using System.Xml;

namespace Weed.Addins
{
    /// <summary>
    /// 创建:谢月甲
    /// 修改:谢月甲,20080529（将类型实例化，放到调用的时候运行；同时可将出错的机率降低）
    /// </summary>
    public class AddinItem
    {
        internal AddinItem(string caseTypeName)
        {
            this.__TypeName = caseTypeName;
        }

        internal AddinItem(XmlNode configNode)
        {
            this.__XmlNode = configNode;

            if (configNode.ParentNode != null)
                this.GroupName = configNode.ParentNode.Name;

            __TypeName = GetAttr("type");

            __ServiceTypeName = GetAttr("service");
            __ContractTypeName = GetAttr("contract");
            
            this.Address = GetAttr("address");

        }
        /// <summary>
        /// 获取XML属性
        /// </summary>
        private string GetAttr(string arrtName)
        {
            XmlAttribute temp = __XmlNode.Attributes[arrtName];

            if (temp != null)
                return temp.Value;
            else
                return null;
        }
        /// <summary>
        /// 解析类型
        /// </summary>
        private Type GetType(string typeName, bool doDebug)
        {
            try
            {
                if (typeName == null)
                    return null;
                else
                    return Type.GetType(typeName, true, true);
            }
            catch (Exception ex)
            {
                if (doDebug)
                    throw ex;
                else
                    return null;
            }
        }

        private XmlNode __XmlNode = null;
        private string __TypeName = null;
        private string __ServiceTypeName = null;
        private string __ContractTypeName = null;
        private bool __IsInited = false;
        private object __locker = new object();

        public void EvalType(bool doDebug)
        {
            this.Type = GetType(__TypeName, doDebug);
            this.ServiceType = GetType(__ServiceTypeName, doDebug);
            this.ContractType = GetType(__ContractTypeName, doDebug);
        }

        private void __TryInit()
        {
            lock (__locker)
            {
                //1.如果已初始化，则直接返回
                //
                if (__IsInited)
                    return;

                //2.开始实始化类型及实例
                //
                try
                {
                    if (this.Type == null)
                        this.Type = Type.GetType(__TypeName, true, true);
                }
                catch (Exception ex)
                {
                    throw new ApplicationException("类型化[" + __TypeName + "]失败，无法生成Addin!具体描述：" + ex.Message, ex);
                }

                this.Case = Activator.CreateInstance(this.Type);

                this.IsCloneable = (this.Case is ICloneable);

                //3.将初始化的标志置为true
                //
                __IsInited = true;
            }
        }

        private bool IsCloneable { get; set; }
        public Type Type { get; private set; }
        private object Case { get; set; }
        
        //Wcf Host 需要的三个属性
        //
        public Type ServiceType { get; private set; }
        public Type ContractType { get; private set; }
        public object Host { get; set; }
        public string Address { get; set; }

        public T Get<T>()
        {
            __TryInit();

            return (T)Case;
        }

        public T New<T>()
        {
            __TryInit();

            if (IsCloneable)
                return (T)((ICloneable)Case).Clone();
            else
                return (T)Activator.CreateInstance(this.Type);
        }

        private string _Name;
        public string Name
        {
            get
            {
                if(_Name == null)
                    _Name = this["name"];

                return _Name;
            }
        }

        public string GroupName { get; private set; }

        public string this[string attrName]
        {
            get
            {
                XmlAttribute temp = __XmlNode.Attributes[attrName];

                if (temp == null)
                    return "";
                else
                    return temp.Value;
            }
        }

        public T Get<T>(string attrName) where T : class
        {
            string attr = GetAttr(attrName);

            if (attr == null)
                return null;
            else
                return Activator.CreateInstance(System.Type.GetType(attr)) as T;
        }
    }
}
