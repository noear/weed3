using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Noear.Weed.Cache {
    public class EmptyCache : ICacheServiceEx {

        public void store(String s, Object o, int i) {

        }


        public Object get(String s) {
            return null;
        }


        public void remove(String s) {

        }


        public int getDefalutSeconds() {
            return 0;
        }


        public String getCacheKeyHead() {
            return "";
        }

        //===================
        //
        public CacheTags tags() {
            return new CacheTags(this);
        }

        public void clear(string tag) {
            tags().clear(tag);
        }

        public void update<T>(string tag, Func<T, T> setter) where T:class {
            tags().update<T>(tag, setter);
        }
    }
}
