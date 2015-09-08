using System;
using System.Collections.Generic;

namespace Noear.Weed {
    internal static class BinderMapping {
        static Dictionary<string, IBinder> CLASS_CACHE;
        static void tryInit() {
            if (CLASS_CACHE == null) {
                CLASS_CACHE = new Dictionary<string, IBinder>();
            }
        }

        public static T getBinder<T>() where T :IBinder{
            tryInit();

            string key = typeof(T).ToString();
            lock (CLASS_CACHE) {
                if (CLASS_CACHE.ContainsKey(key)) {
                    return (T)CLASS_CACHE[key];
                }
                else {
                    T temp = Activator.CreateInstance<T>();
                    CLASS_CACHE.Add(key, temp);
                    return temp;
                }
            }
        }
    }
}
