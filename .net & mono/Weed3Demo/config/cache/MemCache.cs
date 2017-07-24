using System;
using System.Collections.Generic;
using System.Text;
using MemcachedClientLibrary;

using Noear.Weed.Cache;

namespace Weed3Demo.config 
{
    internal class MemCache : ICacheService
    {
        MemcachedClient _cache = null;

        public MemCache(string keyHead, int defSeconds, string host) {
            this._CacheKeyHead = keyHead;
            this._DefalutSeconds = defSeconds;

            InitMemCache(host);
        }

        private string _CacheKeyHead;
        public string getCacheKeyHead()
        {
            return _CacheKeyHead; 
        }

        private int _DefalutSeconds;
        public int getDefalutSeconds()
        {
            return _DefalutSeconds; 
        }

        public object get(string key)
        {
                return _cache.Get(key);
        }

        public void remove(string key)
        {
                _cache.Delete(key);
        }

        public void store(string key, object obj, int seconds)
        {
                _cache.Set(key, obj, DateTime.Now.AddSeconds(seconds));
        }


        void InitMemCache(string host) //ip:poart
        {
            //以下代码，要以具体使用的 Memcached Client 来处理
            //
            string[] servers = { host };

            //缓存服务设置，配置优先级高，无配置使用默认值

            if (!MemcachedClient.Exists(host)) {
                MemcachedClient.Setup(host, servers);
            }

            //实例化
            _cache = MemcachedClient.GetInstance(host);


            //检查缓存服务是否开启

            _cache.SendReceiveTimeout = 1000;
            _cache.MinPoolSize = 10;
            _cache.MaxPoolSize = 100;
        }
    }
}
