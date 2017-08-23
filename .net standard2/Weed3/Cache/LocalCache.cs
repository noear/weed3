using System;
using System.Collections.Generic;

namespace Noear.Weed.Cache {
    public class LocalCache : ICacheServiceEx {
        private String _cacheKeyHead;
        private int _defaultSeconds;

        private int _max = 50000;
        private int _count = 0;

        private List<int> mks = new List<int>(); //key的顺序记录
        private Dictionary<int, LocalCacheRecord> mcc = new Dictionary<int, LocalCacheRecord>();   //缓存存储器

        public LocalCache(String keyHeader, int defSeconds) : this(keyHeader, defSeconds, 50000) {

        }

        public LocalCache(String keyHeader, int defSeconds, int recordMax) {
            _cacheKeyHead = keyHeader;
            _defaultSeconds = defSeconds;
            _max = recordMax;
        }

        public void store(String key, Object obj, int seconds) {
            int hashKey = (_cacheKeyHead + "$" + key).GetHashCode();
            LocalCacheRecord val = new LocalCacheRecord(hashKey, obj, seconds);
            mcc.Add(hashKey, val);
            mks.Add(hashKey);

            _count++;
            if (_count > _max) //总量控制
            {
                int k = mks[0];
                mcc.Remove(k);
                mks.Remove(0);
                _count--;
            }
        }

        public Object get(String key) {
            int hashKey = (_cacheKeyHead + "$" + key).GetHashCode();

            LocalCacheRecord val = null;
            if (mcc.ContainsKey(hashKey))
                val = mcc[hashKey];

            if (val == null)
                return null;

            if (val.time < new DateTime().Ticks) {
                mcc.Remove(hashKey);
                mks.Remove(hashKey);
                _count--;
                return null;
            }
            else
                return val.data;
        }

        public void remove(String key) {
            int hashKey = (_cacheKeyHead + "$" + key).GetHashCode();

            mcc.Remove(hashKey);
            mks.Remove(hashKey);
            _count--;
        }

        public int getDefalutSeconds() {
            return _defaultSeconds;
        }

        public String getCacheKeyHead() {
            return _cacheKeyHead;
        }


        //===================
        //
        public CacheTags tags() {
            return new CacheTags(this);
        }

        public void clear(string tag) {
            tags().clear(tag);
        }

        public void update<T>(string tag, Func<T, T> setter) where T : class {
            tags().update<T>(tag, setter);
        }
    }
}
