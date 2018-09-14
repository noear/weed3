using System;

namespace Noear.Weed.Cache {
    class LocalCacheRecord {
        public int hash;
        public Object data;
        public long time;

        public LocalCacheRecord(int hash, Object val, int seconds) {
            this.hash = hash;
            this.time = new DateTime().AddSeconds(seconds).Ticks;
            this.data = val;
        }
    }
}
