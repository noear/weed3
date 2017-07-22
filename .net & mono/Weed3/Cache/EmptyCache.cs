using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Noear.Weed.Cache {
    public class EmptyCache : ICacheService {

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
    }
}
