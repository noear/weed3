using System;
using System.Collections.Generic;
using System.Configuration;

namespace weedstudio.exts {
    public class DbManager {
        static Dictionary<String, DbContextEx> cache = null;
        static DbManager() {
            cache = new Dictionary<string, DbContextEx>();

            var list = ConfigurationManager.ConnectionStrings;

            for (int i = 0, len = list.Count; i < len; i++) {
                ConnectionStringSettings p = list[i];

                DbContextEx temp = new DbContextEx(p.Name, p.Name);
                cache.Add(p.Name, temp);
            }
        }
        
        public static IEnumerable<String> sources() {
            return cache.Keys;
        }

        public static DbContextEx get(String name) {
            if (cache.ContainsKey(name))
                return cache[name];
            else
                return null;
        }

    }
}
