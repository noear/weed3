using System;
using System.Collections.Generic;
using System.Text;

using Weed.Addins;

namespace Weed
{
    /// <summary>
    /// 插件统一管理器
    /// ---------------------
    /// 创建:谢月甲,20080515
    /// </summary>
    public static class Addin
    {
        private static AddinCache _Cache = AddinCache.G;
        /// <summary>
        /// 获取一个全局唯一的接口
        /// </summary>
        /// <typeparam name="T">接口类型</typeparam>
        /// <returns>接口</returns>
        public static T Get<T>()
        {
            AddinItem temp = _Cache[typeof(T).FullName];

            if (temp == null)
                temp = Mapping.Get<T>();

            if (temp == null)
                throw new ApplicationException(typeof(T).FullName + "不存在对应的配置或实现DLL");
            else
                return temp.Get<T>();
        }

        /// <summary>
        /// 获取一个全局唯一的接口
        /// </summary>
        /// <param name="fullName">接口的完整名称</param>
        /// <typeparam name="T">接口类型</typeparam>
        /// <returns>接口</returns>
        public static T Get<T>(string fullName)
        {
            return _Cache[fullName].Get<T>();
        }

        /// <summary>
        /// 获取一个新实例化的接口
        /// </summary>
        /// <typeparam name="T">接口类型</typeparam>
        /// <returns>接口</returns>
        public static T New<T>()
        {
            AddinItem temp = _Cache[typeof(T).FullName];

            if (temp == null)
                temp = Mapping.Get<T>();

            if (temp == null)
                throw new ApplicationException(typeof(T).FullName + "不存在对应的配置或实现DLL");
            else
                return temp.New<T>();
        }

        /// <summary>
        /// 获取一个新实例化的接口
        /// </summary>
        /// <param name="fullName">接口的完整名称</param>
        /// <typeparam name="T">接口类型</typeparam>
        /// <returns>接口</returns>
        public static T New<T>(string fullName)
        {
            return _Cache[fullName].New<T>();
        }

        //---------------------------------
        /// <summary>
        /// 解析一个接口的实例化
        /// </summary>
        /// <param name="caseTypeName">实现类的完整名称</param>
        /// <typeparam name="T">接口类型</typeparam>
        /// <returns>接口</returns>
        public static T Eval<T>(string caseTypeName)
        {
            if (Contains(caseTypeName) == false)
                Add(caseTypeName);

            return Get<T>(caseTypeName);
        }
        
        #region 获取插件项
        /// <summary>
        /// 获取一个插件配置项
        /// </summary>
        /// <typeparam name="T">接口类型</typeparam>
        /// <returns>配置项</returns>
        public static AddinItem Item<T>()
        {
            return Item(typeof(T).FullName);
        }

        /// <summary>
        /// 获取一个插件配置项
        /// </summary>
        /// <param name="fullName">接口的完整名称</param>
        /// <returns>配置项</returns>
        public static AddinItem Item(string fullName)
        {
            if (_Cache.Contains(fullName))
                return _Cache[fullName];
            else
                return null;
        }

        public static AddinItem Item(int index)
        {
            return _Cache[index];
        }

        public static IEnumerable<AddinItem> Find(string prop, string value)
        {
            foreach (KeyValuePair<string, AddinItem> kv in _Cache.Items)
            {
                if (kv.Value[prop] == value)
                    yield return kv.Value;
            }
        }

        public static IEnumerable<AddinItem> Find(string groupName)
        {
            foreach (KeyValuePair<string, AddinItem> kv in _Cache.Items)
            {
                if (kv.Value.GroupName == groupName)
                    yield return kv.Value;
            }
        }

        public static AddinItem FindFirst(string prop, string value)
        {
            foreach (KeyValuePair<string, AddinItem> kv in _Cache.Items)
            {
                if (kv.Value[prop] == value)
                    return kv.Value;
            }

            return null;
        }

        public static IEnumerable<AddinItem> Items
        {
            get
            {
                foreach (KeyValuePair<string, AddinItem> kv in _Cache.Items)
                    yield return kv.Value;
            }
        }

        public static bool Contains(string caseTypeName)
        {
            return  _Cache.Contains(caseTypeName);
        }

        public static bool Contains<T>()
        {
            return _Cache.Contains(typeof(T).FullName);
        }

        public static void Load(string configSection)
        {
            _Cache.Load(configSection);
        }

        public static AddinItem Add<T>(string caseTypeName)
        {
            return _Cache.Add(typeof(T).FullName, caseTypeName);
        }

        public static void Add(string caseTypeName)
        {
            _Cache.Add(caseTypeName, caseTypeName);
        }

        #endregion        
    }
}
