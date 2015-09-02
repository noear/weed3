using Noear.Weed.Cache;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web;

namespace Weed3Demo.config {
    public class AspCache : ICacheService {
        string _keyHead;
        int _defSeconds;

        public AspCache(string keyHead, int defSeconds) {
            _keyHead = keyHead;
            _defSeconds = defSeconds;
        }

        public object get(string key) {
            return HttpRuntime.Cache.Get(key);
        }

        public string getCacheKeyHead() {
            return _keyHead;
        }

        public int getDefalutSeconds() {
            return _defSeconds;
        }

        public void remove(string key) {
            HttpRuntime.Cache.Remove(key);
        }

        public void store(string key, object obj, int seconds) {
            HttpRuntime.Cache.Insert(key, obj, null, DateTime.Now.AddSeconds(seconds), TimeSpan.Zero);
        }
    }
}
