using Noear.Weed.Cache;
using Weed3Demo.config;

namespace Weed3Demo {
    public class CacheUtil {
        ICacheService data = new AspCache("data", 60);
        ICacheService text = new MemCache("text", 60, "127.0.0.0:8001");
        ICacheService xxxx = new LocalCache(keyHeader: "xxxx", defSeconds: 60);
    }
}
