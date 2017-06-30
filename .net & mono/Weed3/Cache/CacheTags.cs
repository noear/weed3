using System;
using System.Collections.Generic;

namespace Noear.Weed.Cache {
    public class CacheTags {
        private ICacheService _Cache;

        public CacheTags(ICacheService caching) {
            _Cache = caching;
        }

        //#region 异步 Add
        private List<String> asynTags = null;
        /// <summary>
        /// 为缓存添加一个标签（异步 Add{begin}）
        /// </summary>
        /// <param name="tag">标签</param>
        /// <param name="val">标签值</param>
        public void beginAdd(String tag) {
            if (asynTags == null)
                asynTags = new List<String>();

            asynTags.Add(tag);
        }

        /// <summary>
        /// 为缓存添加一个标签（异步 Add{end}）
        /// </summary>
        /// <param name="target">目标</param>
        public void endAdd(IWeedKey target) {
            endAdd(target.getWeedKey());
        }

        /// <summary>
        /// 为缓存添加一个标签（异步 Add{end}）
        /// </summary>
        /// <param name="targetCacheKey">目标缓存键</param>
        public void endAdd(String targetCacheKey) {
            if (asynTags == null)
                return;

            if (targetCacheKey != null && targetCacheKey.Length > 0) {
                foreach (String tag in asynTags) {
                    add(tag, targetCacheKey);
                }
            }
            asynTags.Clear();
        }
        //#endregion
        //#region 同步 Add


        /// <summary>
        /// 为缓存添加一个标签（同步 Add）
        /// </summary>
        /// <param name="tag">标签</param>
        /// <param name="val">标签值</param>
        /// <param name="targetCacheKey">目标缓存键</param>
        public void add(String tag, String targetCacheKey) {
            List<String> temp = this[KEY(tag)];
            if (temp.Contains(targetCacheKey))
                return;

            temp.Add(targetCacheKey);

            this[KEY(tag)] = temp;
        }

        //#endregion



        /// <summary>
        /// 清空[@tag=val]相关的所有缓存
        /// </summary>
        public CacheTags clear(String tag) {
            List<String> keys = this[KEY(tag)];

            foreach (String cacheKey in keys)
                _Cache.remove(cacheKey);

            _Cache.remove(KEY(tag));

            return this;
        }

        public void update<T>(string tag, Func<T,T> setter) where T:class{
            var keys = getCacheKeys(tag);

            foreach (string key in keys) {
                var temp = _Cache.get(key);
                if (temp == null) {
                    continue;
                }
                
                var obj = temp as T;
                if (obj != null) {
                    obj = setter(obj);
                    _Cache.store(key, obj, _Cache.getDefalutSeconds());
                }
            }
        }

        public int count(String tag) {
            return this[KEY(tag)].Count;
        }


        public String getCacheKey(String tag, int index) {
            List<String> temp = this[KEY(tag)];

            if (temp.Count > index)
                return temp[index];
            else
                return null;
        }


        /// <summary>
        /// 获取一个标签里的内容
        /// </summary>
        /// <param name="tag"></param>
        /// <param name="val"></param>
        /// <returns></returns>
        public List<String> getCacheKeys(String tag) {
            return this[KEY(tag)];
        }


        public void removeTag(String tag, String val, IWeedKey target) {
            removeTag(tag, val, (target.getWeedKey()));
        }

        public void removeTag(String tag, String val, String targetCacheKey) {
            List<String> temp = this[KEY(tag)];
            temp.Remove(targetCacheKey);
            this[KEY(tag)] = temp;

            _Cache.remove(targetCacheKey);
        }

        private List<String> this[String key] {
            get {
                Object temp = _Cache.get(key);

                if (temp == null)
                    return new List<String>();
                else
                    return (List<String>)temp;
            }
            set {
                _Cache.store(key, value, _Cache.getDefalutSeconds());
            }
        }


        private String KEY(String tag) {
            return ("@" + tag).ToUpper();
        }
        
    }
}
